package com.wildfirechat.wallet.service;

import org.springframework.http.ResponseEntity;

import com.wildfirechat.wallet.request.WalletBalanceRequest;
import com.wildfirechat.wallet.request.WalletStatusRequest;
import com.wildfirechat.wallet.response.WalletResponse;

public interface WalletService {
	
	ResponseEntity<WalletResponse> createWallet(String userId);
	
	ResponseEntity<WalletResponse> walletStatusChange(WalletStatusRequest walletStatusRequest);
	
	ResponseEntity<WalletResponse> getAllWallet();
	
	ResponseEntity<WalletResponse> getWalletById(Long walletId);
	
	ResponseEntity<WalletResponse> updateWalletBalance(WalletBalanceRequest walletBalanceRequest);
	
	ResponseEntity<WalletResponse> getWalletByUserId(String userId);
}
