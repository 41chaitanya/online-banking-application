package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.model.Account;
import org.chaitanya.onlinebankapp.model.AccountType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account createAccount(UUID userId, AccountType accountType, BigDecimal initialBalance);

    Account getAccountById(UUID accountId);

    List<Account> getAccountsByUser(UUID userId);

    Account updateAccount(UUID accountId, AccountType accountType);

    void deleteAccount(UUID accountId);

}
