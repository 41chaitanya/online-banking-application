package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.configAndSecurity.SecurityUtil;
import org.chaitanya.onlinebankapp.model.Account;
import org.chaitanya.onlinebankapp.model.AccountType;
import org.chaitanya.onlinebankapp.model.User;
import org.chaitanya.onlinebankapp.repository.AccountRepository;
import org.chaitanya.onlinebankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;


    private String getLoggedInUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
    }

    private void checkUserOwnership(Account account) {

        String loggedEmail = SecurityUtil.getLoggedInUserEmail();

        if (loggedEmail == null) {
            throw new RuntimeException("Unauthorized user");
        }
        String role = getLoggedInUserRole();

        // MANAGER + ADMIN can access any
        if (!role.equals("USER")) {
            return;
        }
        // only USER
        if (!account.getUser().getEmail().equalsIgnoreCase(loggedEmail)) {
            throw new RuntimeException("Access denied: You do not own this account");
        }
    }

    @Override
    public Account createAccount(UUID userId, AccountType accountType, BigDecimal initialBalance) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (getLoggedInUserRole().equals("USER")) {
            String loggedEmail = SecurityUtil.getLoggedInUserEmail();

            if (!user.getEmail().equalsIgnoreCase(loggedEmail)) {
                throw new RuntimeException("You cannot create an account for another user");
            }
        }

        String generatedAccountNo = "0105" + System.currentTimeMillis();

        if (accountRepository.existsByAccountNumber(generatedAccountNo)) {
            throw new RuntimeException("Account number already exists");
        }

        Account account = new Account();
        account.setAccountNumber(generatedAccountNo);
        account.setAccountType(accountType);
        account.setBalance(initialBalance);
        account.setUser(user);

        return accountRepository.save(account);
    }


    @Override
    public Account getAccountById(UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        checkUserOwnership(account);
        account.getUser().setPassword(null);

        return account;
    }

    @Override
    public List<Account> getAccountsByUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (getLoggedInUserRole().equals("USER")) {
            String loggedEmail = SecurityUtil.getLoggedInUserEmail();

            if (!user.getEmail().equals(loggedEmail)) {
                throw new RuntimeException("You cannot view other user's accounts");
            }
        }

        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account updateAccount(UUID accountId, AccountType accountType) {

        Account account = getAccountById(accountId);

        account.setAccountType(accountType);

        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(UUID accountId) {

        Account account = getAccountById(accountId);

        accountRepository.delete(account);
    }
}
