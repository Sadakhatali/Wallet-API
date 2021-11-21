package com.wildfirechat.wallet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wildfirechat.wallet.request.WalletBalanceRequest;
import com.wildfirechat.wallet.request.WalletStatusRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
@Slf4j
public class WalletController {
	
	private final WalletService walletService;
	private final Logger logger = LoggerFactory.getLogger("mainlogger");
	private static final String CLAZZ = "WalletController.java - ";
	
	/**
	 * Description - Below API is use to create the wallet as disabled
	 * @param walletRequest
	 * @return
	 */
	@PostMapping("/user/create/wallet/{userId}")
	public ResponseEntity<WalletResponse> createWallet(@PathVariable String userId) {
		logger.info(CLAZZ+"createWallet API has been invoked "+userId);
		return walletService.createWallet(userId);
	}
	
	/**
	 * Description - Below API is use to change the status of the wallet
	 * @param walletStatusRequest
	 * @return
	 */
	
	/*@PutMapping("/user/wallet/status/change")
	public ResponseEntity<WalletResponse> walletStatusChange(@RequestBody WalletStatusRequest walletStatusRequest) {
		logger.info(CLAZZ+"walletStatusChange API has been invoked"
				+" status:: "+walletStatusRequest.getStatus()
				+" userId:: "+walletStatusRequest.getUserId());
		return walletService.walletStatusChange(walletStatusRequest);
	}*/
	
	/**
	 * Description - Below API is use to load all the wallets from the database
	 * @return
	 */
	@GetMapping("/users/wallet")
	public ResponseEntity<WalletResponse> getAllWallet() {
		logger.info(CLAZZ+"getAllWallet API has been invoked ");
		return walletService.getAllWallet();
	}
	
	/**
	 * Description - Below API is use to load specific wallet by walletId from the database
	 * @return
	 */
	@GetMapping("/users/wallet/{walletId}")
	public ResponseEntity<WalletResponse> getWallet(@PathVariable Long walletId) {
		logger.info(CLAZZ+"getWallet API has been invoked "+walletId);
		return walletService.getWalletById(walletId);
	}
	
	/**
	 * Description - Below API is use to load all the specific wallet by userId from the database
	 * @return
	 */
	@GetMapping("/users/{userId}/wallet")
	public ResponseEntity<WalletResponse> getWalletInfoByUserId(@PathVariable String userId) {
		logger.info(CLAZZ+"getWalletInfoByUserId API has been invoked "+userId);
		return walletService.getWalletByUserId(userId);
	}
	
	/**
	 * Description - Below API is use to update the balance in wallet
	 * @return
	 */
	@PutMapping("/user/wallet/blance")
	public ResponseEntity<WalletResponse> updateAvailableBalance(@RequestBody WalletBalanceRequest walletBalanceRequest) {
		logger.info(CLAZZ+"updateAvailableBalance API has been invoked ");
		return walletService.updateWalletBalance(walletBalanceRequest);
	}
}
