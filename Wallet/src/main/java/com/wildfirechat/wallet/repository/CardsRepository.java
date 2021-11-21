package com.wildfirechat.wallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wildfirechat.wallet.model.TCards;

public interface CardsRepository extends JpaRepository<TCards, Long>{
	
	Optional<TCards> findByCardNumber(String cardNumber);
	
	List<TCards> findAllByUserId(String userId);
	
	Optional<TCards> findByUserId(String userId);
	
	Optional<TCards> findByIdAndUserId(Long id, String userId);
	
}