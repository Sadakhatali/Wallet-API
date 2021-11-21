package com.wildfirechat.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wildfirechat.wallet.model.TWallet;

public interface WalletRepository extends JpaRepository<TWallet, Long>{
	
	Optional<TWallet> findByUserId(String userId);

	
}
