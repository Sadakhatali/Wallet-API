package com.wildfirechat.wallet.service;

import org.springframework.http.ResponseEntity;

import com.wildfirechat.wallet.request.WalletTransactionRequest;
import com.wildfirechat.wallet.response.WalletResponse;

public interface WalletTransactionService {

	ResponseEntity<WalletResponse> createWalletTransaction(WalletTransactionRequest walletTransactionRequest);
	
	ResponseEntity<WalletResponse> getAllWalletTxns();
	
	ResponseEntity<WalletResponse> getWalletIdTxns(Long walletId);
	
	ResponseEntity<WalletResponse> getTxnsById(Long txnId);
	
	ResponseEntity<WalletResponse> getWalletTxnsByUserId(String userId);
	
	ResponseEntity<WalletResponse> getTopupInfo();
	
	ResponseEntity<WalletResponse> confirmTxn(String orderNumber);
}
