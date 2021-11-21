package com.wildfirechat.wallet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wildfirechat.wallet.request.WalletTransactionRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.WalletTransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
@Slf4j
public class WalletTransactionController {
	
	private final WalletTransactionService walletTransactionService;
	public static final String CLAZZ = "WalletTransactionController.java - ";
	private final Logger logger = LoggerFactory.getLogger("mainlogger");
	
	
	/**
	 * Description - Below API is use to create a transaction
	 * create transaction
	 * @param walletTransactionRequest
	 * @return
	 */
	@PostMapping("/user/wallet/transaction/create")
	public ResponseEntity<WalletResponse> createTransaction(@RequestBody WalletTransactionRequest walletTransactionRequest) {
		logger.info(CLAZZ+"createWalletTransaction API has been inoked");
		return walletTransactionService.createWalletTransaction(walletTransactionRequest);
	}
	
	/**
	 * Description - Below API is use to getAllWallet Transactions
	 * getAllWalletTarsnactions
	 * @return
	 */
	@GetMapping("/all/users/wallet/transactions")
	public ResponseEntity<WalletResponse> getAllWalletTxns() {
		logger.info(CLAZZ+"getAllWalletTxns API has been inoked");
		return walletTransactionService.getAllWalletTxns();
	}
	
	/**
	 * Description - Below API is use to load all transaction done by wallet
	 * getAllByWalletId Transactions
	 * @param walletId
	 * @return
	 */ 
	@GetMapping("/user/wallet/{walletId}/all/transactions")
	public ResponseEntity<WalletResponse> getWalletTxns(@PathVariable Long walletId) {
		logger.info(CLAZZ+"getWalletTxns API has been inoked "+walletId);
		return walletTransactionService.getWalletIdTxns(walletId);
	}
	
	/**
	 * Description - Below API is use to load all transaction done by User id
	 * getAllByWalletId Transactions
	 * @param walletId
	 * @return
	 */ 
	@GetMapping("/user/{userId}/wallet/all/transactions")
	public ResponseEntity<WalletResponse> getWalletTxnsDataByUserId(@PathVariable String userId) {
		logger.info(CLAZZ+"getWalletTxnsDataByUserId API has been inoked "+userId);
		return walletTransactionService.getWalletTxnsByUserId(userId);
	}
	
	/**
	 * Description - Below API is use to load the specific transaction data
	 * getTransactionById
	 * @param txnId
	 * @return
	 */
	@GetMapping("/user/wallet/transaction/{txnId}")
	public ResponseEntity<WalletResponse> getTxnById(@PathVariable Long txnId) {
		logger.info(CLAZZ+"getTxnById API has been inoked "+txnId);
		return walletTransactionService.getTxnsById(txnId);
	}
	
	/**
	 * Description - Below API is use to confirmTransaction the transaction
	 * @param orderNumber
	 * @return
	 */
	@PostMapping("/confirm/user/wallet/transaction/{orderNumber}")
	public ResponseEntity<WalletResponse> confirmTransaction(@PathVariable String orderNumber) {
		logger.info(CLAZZ+"confirmTransaction API has been inoked "+orderNumber);
		return walletTransactionService.confirmTxn(orderNumber);
	}
	
}
