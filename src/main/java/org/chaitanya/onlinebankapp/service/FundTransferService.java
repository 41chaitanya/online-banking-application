package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.dto.FundTransferRequest;
import org.chaitanya.onlinebankapp.model.FundTransfer;

public interface FundTransferService {
    FundTransfer transfer(FundTransferRequest request);
}
