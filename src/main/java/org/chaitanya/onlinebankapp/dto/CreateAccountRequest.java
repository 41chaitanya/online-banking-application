package org.chaitanya.onlinebankapp.dto;

import lombok.Data;
import org.chaitanya.onlinebankapp.model.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAccountRequest {
    private UUID userId;
    private AccountType accountType;
    private BigDecimal initialBalance;
}