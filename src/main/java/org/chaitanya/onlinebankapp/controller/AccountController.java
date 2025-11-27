package org.chaitanya.onlinebankapp.controller;

import org.chaitanya.onlinebankapp.dto.CreateAccountRequest;
import org.chaitanya.onlinebankapp.model.Account;
import org.chaitanya.onlinebankapp.model.AccountType;
import org.chaitanya.onlinebankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public Account createAccount(@RequestBody CreateAccountRequest req) {
        Account account = accountService.createAccount(req.getUserId(), req.getAccountType(), req.getInitialBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(account).getBody();

    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable UUID accountId) {
        return accountService.getAccountById(accountId);
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/user/{userId}")
    public List<Account> getUserAccounts(@PathVariable UUID userId) {
        return accountService.getAccountsByUser(userId);
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable UUID accountId,
                                 @RequestParam AccountType accountType) {
        return accountService.updateAccount(accountId, accountType);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{accountId}")
    public String deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
        return "Account deleted successfully";
    }
}
