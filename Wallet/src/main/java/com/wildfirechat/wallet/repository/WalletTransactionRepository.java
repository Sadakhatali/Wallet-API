package com.wildfirechat.wallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wildfirechat.wallet.model.TWalletTransactionHistory;

public interface WalletTransactionRepository extends JpaRepository<TWalletTransactionHistory, Long>{
	
	List<TWalletTransactionHistory> findByWalletId(Long walletId);

	List<TWalletTransactionHistory> findByUserId(String userId);
	
	Optional<TWalletTransactionHistory> findByOrderNumber(String orderNumber);
}