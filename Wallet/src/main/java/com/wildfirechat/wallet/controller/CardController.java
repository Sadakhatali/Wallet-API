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

import com.wildfirechat.wallet.request.CardRequest;
import com.wildfirechat.wallet.request.CashoutRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.CardServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
@Slf4j
public class CardController {
	private final CardServices cardServices;
	private final Logger logger = LoggerFactory.getLogger("mainlogger");
	private static final String CLAZZ = "CardController.java - ";
	
	/**
	 * Description - addCard API is use to add the customer cards into the database
	 * @param cardRequest
	 * @return
	 */
	@PostMapping("/add/card")
	public ResponseEntity<WalletResponse> addCard(@RequestBody CardRequest cardRequest) {
		logger.info(CLAZZ+"addCard API has been invoked ");
		return cardServices.addCustomerCard(cardRequest);
	}
	
	/**
	 * Description - getAllCards API is use to load all the cards irrespective of userId
	 * @return
	 */
	@GetMapping("/get/all/cards")
	public ResponseEntity<WalletResponse> getAllCards() {
		logger.info(CLAZZ+"getAllCards API has been invoked ");
		return cardServices.loadAllCards();
	}
	
	/**
	 * Description - getAllCardsUsingUserId API is use to load all the cards associated with userId
	 * @return
	 */
	@GetMapping("/get/all/cards/{userId}")
	public ResponseEntity<WalletResponse> getAllCardsUsingUserId(@PathVariable String userId) {
		logger.info(CLAZZ+"getAllCards API has been invoked ");
		return cardServices.loadAllCardsByUserId(userId);
	}
	
	/**
	 * Description - cashWithdraw API is use to withdraw cash from the wallet
	 * @param cashoutRequest
	 * @return
	 */
	@PostMapping("/cashout")
	public ResponseEntity<WalletResponse> cashWithdraw(@RequestBody CashoutRequest cashoutRequest) {
		logger.info(CLAZZ+"cashWithdraw API has been invoked ");
		return cardServices.cashout(cashoutRequest);
	}
}
