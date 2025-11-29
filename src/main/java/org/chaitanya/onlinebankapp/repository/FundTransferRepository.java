package org.chaitanya.onlinebankapp.repository;

import org.chaitanya.onlinebankapp.model.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundTransferRepository extends JpaRepository<FundTransfer, UUID> {
}
