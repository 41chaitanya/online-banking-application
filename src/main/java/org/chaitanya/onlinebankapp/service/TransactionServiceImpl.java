package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.configAndSecurity.SecurityUtil;
import org.chaitanya.onlinebankapp.dto.DepositRequest;
import org.chaitanya.onlinebankapp.dto.WithdrawalRequest;
import org.chaitanya.onlinebankapp.model.Account;
import org.chaitanya.onlinebankapp.model.Transaction;
import org.chaitanya.onlinebankapp.model.TransactionStatus;
import org.chaitanya.onlinebankapp.model.TransactionType;
import org.chaitanya.onlinebankapp.repository.AccountRepository;
import org.chaitanya.onlinebankapp.repository.TransactionRepository;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Transaction deposit(DepositRequest request) {
        Account account = accountRepository.getReferenceById(request.getAccountId());
        BigDecimal amount = request.getAmount();
        String loggedEmail = SecurityUtil.getLoggedInUserEmail();

        if (!account.getUser().getEmail().equalsIgnoreCase(loggedEmail)) {
            throw new RuntimeException("You are not authorized to deposit into this account");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        return transactionRepository.save(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Transaction withdraw(WithdrawalRequest request) {
        Account account = accountRepository.getReferenceById(request.getAccountId());
        BigDecimal amount = request.getAmount();
        String description = request.getDescription();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            Transaction failedTxn = Transaction.builder()
                    .account(account)
                    .amount(amount)
                    .type(TransactionType.WITHDRAW)
                    .status(TransactionStatus.FAILED)
                    .description("Insufficient balance: " + description)
                    .createdAt(LocalDateTime.now())
                    .build();

            transactionRepository.saveAndFlush(failedTxn);  // force save before rollback
            throw new RuntimeException("Insufficient balance");
        }


        // Deduct only if above condition passed
        BigDecimal updatedBalance = account.getBalance().subtract(amount);
        account.setBalance(updatedBalance);
        accountRepository.save(account);


        Transaction tx = Transaction.builder()
                .account(account)
                .amount(amount)
                .status(TransactionStatus.SUCCESS)
                .type(TransactionType.WITHDRAW)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        return transactionRepository.save(tx);
    }


}
