package org.chaitanya.onlinebankapp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
public class WithdrawalRequest {
    private UUID accountId;
    private BigDecimal amount;
    private String description;
}
