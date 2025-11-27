package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.dto.DepositRequest;
import org.chaitanya.onlinebankapp.dto.WithdrawalRequest;


import org.chaitanya.onlinebankapp.model.Transaction;

public interface TransactionService {
    Transaction deposit(DepositRequest request);
    Transaction withdraw(WithdrawalRequest request);


}
