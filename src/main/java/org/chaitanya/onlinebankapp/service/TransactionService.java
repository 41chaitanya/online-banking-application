package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.dto.DepositRequest;
import org.chaitanya.onlinebankapp.dto.WithdrawalRequest;
import org.chaitanya.onlinebankapp.model.Transaction;

import java.util.UUID;

public interface TransactionService {
    Transaction deposit(DepositRequest request);
    Transaction withdraw(WithdrawalRequest request);

    }
