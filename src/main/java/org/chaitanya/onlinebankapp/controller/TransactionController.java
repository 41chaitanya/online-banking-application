package org.chaitanya.onlinebankapp.controller;

import org.chaitanya.onlinebankapp.dto.DepositRequest;
import org.chaitanya.onlinebankapp.dto.FundTransferRequest;
import org.chaitanya.onlinebankapp.dto.WithdrawalRequest;
import org.chaitanya.onlinebankapp.model.FundTransfer;
import org.chaitanya.onlinebankapp.model.Transaction;
import org.chaitanya.onlinebankapp.service.FundTransferService;
import org.chaitanya.onlinebankapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private FundTransferService fundTransferService;

    @PostMapping("/deposit")
    public Transaction deposit(@RequestBody DepositRequest request) {
        return transactionService.deposit(request);
    }
    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestBody WithdrawalRequest request) {
        return  transactionService.withdraw(request);
    }
    @PostMapping("/transfer")
    public String transfer(@RequestBody FundTransferRequest request) {
        fundTransferService.transfer(request);
        return request.getDescription()+" -- > Transfer successful";
    }


}