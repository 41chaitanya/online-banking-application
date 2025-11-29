package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.configAndSecurity.SecurityUtil;
import org.chaitanya.onlinebankapp.dto.FundTransferRequest;
import org.chaitanya.onlinebankapp.model.*;
import org.chaitanya.onlinebankapp.repository.AccountRepository;
import org.chaitanya.onlinebankapp.repository.FundTransferRepository;
import org.chaitanya.onlinebankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FundTransferServiceImpl implements FundTransferService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository  transactionRepository;
    @Autowired
    private FundTransferRepository fundTransferRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FundTransfer transfer(FundTransferRequest request) {
        UUID senderAccountId = request.getSenderAccountId();
        UUID receiverAccountId = request.getReceiverAccountId();
        BigDecimal amount = request.getAmount();
        Account sender  = accountRepository.getReferenceById(senderAccountId);
        System.out.println(sender);
        Account receiver  = accountRepository.getReferenceById(receiverAccountId);
        System.out.println(receiver);
        String loggedEmail = SecurityUtil.getLoggedInUserEmail();

        if (!sender.getUser().getEmail().equalsIgnoreCase(loggedEmail)) {
            logFailedTransfer(sender, receiver, request, "Unauthorized access");
            throw new RuntimeException("You are not authorized to transfer from this account");
        }


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logFailedTransfer(sender, receiver, request, "Invalid amount");
            throw new RuntimeException("Amount must be positive");
        }
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            logFailedTransfer(sender, receiver, request, "Insufficient balance");
            throw new RuntimeException("Insufficient balance");
        }
        try{
//            deduct noney from sender
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            accountRepository.save(sender);

            Transaction senderTxn = Transaction.builder()
                    .account(sender)
                    .amount(request.getAmount())
                    .type(TransactionType.WITHDRAW)
                    .status(TransactionStatus.SUCCESS)
                    .description("Fund transfer to: " + receiver.getAccountNumber())
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.save(senderTxn);

//          money added to reciver
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));
            accountRepository.save(receiver);
            Transaction receiverTxn = Transaction.builder()
                    .account(receiver)
                    .amount(request.getAmount())
                    .type(TransactionType.DEPOSIT)
                    .status(TransactionStatus.SUCCESS)
                    .description("Fund received from: " + sender.getAccountNumber())
                    .createdAt(LocalDateTime.now())
                    .build();
            transactionRepository.save(receiverTxn);

            FundTransfer transfer = FundTransfer.builder()
                    .senderAccount(sender)
                    .receiverAccount(receiver)
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .status(TransactionStatus.SUCCESS)
                    .createdAt(LocalDateTime.now())
                    .build();
            return fundTransferRepository.save(transfer);


        }catch (Exception ex) {

            // 🔥 ANY ERROR → log failed transfer + rollback entire transaction
            logFailedTransfer(sender, receiver, request, ex.getMessage());

            throw new RuntimeException("Fund Transfer failed: " + ex.getMessage());
        }
    }
    private void logFailedTransfer(Account sender,
                                   Account receiver,
                                   FundTransferRequest request,
                                   String reason) {

        // Save failed FUND_TRANSFER entry
        FundTransfer failedRecord = FundTransfer.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(request.getAmount())
                .description("FAILED: " + reason)
                .status(TransactionStatus.FAILED)
                .createdAt(LocalDateTime.now())
                .build();

        fundTransferRepository.save(failedRecord);

        // Save failed TRANSACTION entry (sender side)
        Transaction failedTxn = Transaction.builder()
                .account(sender)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAW) // or FUND_TRANSFER
                .status(TransactionStatus.FAILED)
                .description("Fund Transfer FAILED: " + reason)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(failedTxn);
    }

}
