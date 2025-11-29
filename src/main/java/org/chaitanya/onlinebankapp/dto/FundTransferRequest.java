package org.chaitanya.onlinebankapp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FundTransferRequest {
    private UUID senderAccountId;
    private UUID receiverAccountId;
    private BigDecimal amount;
    private String description;
}