package com.wildfirechat.wallet.service;

import org.springframework.http.ResponseEntity;

import com.wildfirechat.wallet.request.CardRequest;
import com.wildfirechat.wallet.request.CashoutRequest;
import com.wildfirechat.wallet.response.WalletResponse;

public interface CardServices {
	ResponseEntity<WalletResponse> addCustomerCard(CardRequest cardRequest);
	
	ResponseEntity<WalletResponse> loadAllCards();
	
	ResponseEntity<WalletResponse> loadAllCardsByUserId(String userId);
	
	ResponseEntity<WalletResponse> cashout(CashoutRequest cashoutRequest);
}
