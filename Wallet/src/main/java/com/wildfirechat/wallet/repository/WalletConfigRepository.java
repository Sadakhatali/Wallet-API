package com.wildfirechat.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wildfirechat.wallet.model.TWalletConfig;

public interface WalletConfigRepository extends JpaRepository<TWalletConfig, Long>{
	
	@Query(value="select _name from t_user where _uid =:userId", nativeQuery = true)
	String loadUsername(@Param("userId") String userId);
	
}
