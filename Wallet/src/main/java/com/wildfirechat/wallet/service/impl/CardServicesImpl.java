package com.wildfirechat.wallet.service.impl;

import static com.wildfirechat.wallet.constants.WalletConstants.CARDS_LOADED;
import static com.wildfirechat.wallet.constants.WalletConstants.CARDS_NOT_EXISTS;
import static com.wildfirechat.wallet.constants.WalletConstants.CARD_ADDED;
import static com.wildfirechat.wallet.constants.WalletConstants.CARD_ALREADY_ADDED;
import static com.wildfirechat.wallet.constants.WalletConstants.ERROR_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.SUCCESS_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.TXN_CREATED_SUCCESS;
import static com.wildfirechat.wallet.constants.WalletConstants.SUCCESS;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wildfirechat.wallet.dto.CardDTO;
import com.wildfirechat.wallet.dto.WalletTransactionDTO;
import com.wildfirechat.wallet.enums.TransactionType;
import com.wildfirechat.wallet.model.TCards;
import com.wildfirechat.wallet.model.TWallet;
import com.wildfirechat.wallet.model.TWalletTransactionHistory;
import com.wildfirechat.wallet.repository.CardsRepository;
import com.wildfirechat.wallet.repository.WalletRepository;
import com.wildfirechat.wallet.repository.WalletTransactionRepository;
import com.wildfirechat.wallet.request.CardRequest;
import com.wildfirechat.wallet.request.CashoutRequest;
import com.wildfirechat.wallet.request.WalletBalanceRequest;
import com.wildfirechat.wallet.request.WalletTransactionRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.CardServices;
import com.wildfirechat.wallet.service.WalletService;
import com.wildfirechat.wallet.utility.WalletUtility;

@Service
public class CardServicesImpl implements CardServices {

	@Autowired
	private CardsRepository cardsRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	
	@Override
	public ResponseEntity<WalletResponse> addCustomerCard(CardRequest cardRequest) {
		// verify card is already added for the user
		Optional<TCards> cardsObj = cardsRepository.findByCardNumber(cardRequest.getCardNumber());
		if(!cardsObj.isPresent()) {
			// Store the card
			TCards cards = cardsRepository.save(dtoModelMapper(cardRequest));
			return ResponseEntity.ok(
						WalletResponse.builder()
							.code(SUCCESS_CODE)
							.msg(messageSource.getMessage(CARD_ADDED, null, Locale.US))
							.result(dtoMapper(cards))
							.build());
		} else {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(ERROR_CODE)
						.msg(messageSource.getMessage(CARD_ALREADY_ADDED, null, Locale.US))
						.build());
		}
	}
	
	public ResponseEntity<WalletResponse> loadAllCards() {
		List<TCards> cardsList = cardsRepository.findAll();
		return ResponseEntity.ok(
				WalletResponse.builder()
					.code(SUCCESS_CODE)
					.msg(messageSource.getMessage(CARDS_LOADED, null, Locale.US))
					.result(cardsList.stream().map(card -> dtoMapper(card)).collect(Collectors.toList()))
					.build());
		
	}
	
	public ResponseEntity<WalletResponse> loadAllCardsByUserId(String userId) {
		
		List<TCards> cardsList = cardsRepository.findAllByUserId(userId);
		if(null != cardsList && cardsList.size()>0) {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(CARDS_LOADED, null, Locale.US))
						.result(cardsList.stream().map(card -> dtoMapper(card)).collect(Collectors.toList()))
						.build());
		} else {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(CARDS_NOT_EXISTS, null, Locale.US))
						.build());
		}
	}
	
	public ResponseEntity<WalletResponse> cashout(CashoutRequest cashoutRequest) {
		
		// validate the card
				Optional<TCards> card = cardsRepository.findById(Long.valueOf(cashoutRequest.getCardId()));
				if(card.isEmpty()) {
					return ResponseEntity.ok(
							WalletResponse.builder()
								.code(ERROR_CODE)
								.msg("Given card is not available")
								.build());
				}
		
		// validate the userId
		Optional<TCards> cardData = cardsRepository.findByIdAndUserId(Long.valueOf(cashoutRequest.getCardId()), cashoutRequest.getUserId());
		
		if(!Objects.equals(cardData.get().getUserId().toString(), cashoutRequest.getUserId())) {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(ERROR_CODE)
						.msg("Given userId not mapped with any cards")
						.build());
		}
		
		// validate the Balance
		Optional<TWallet> tWallet = walletRepository.findByUserId(cashoutRequest.getUserId());
		if(Float.compare(tWallet.get().getAvailableBalance(), 0f) == 0) {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(ERROR_CODE)
						.msg("Insufficient Balance")
						.build());
		} else if(Float.compare(tWallet.get().getAvailableBalance(), cashoutRequest.getAmout()) < 0) {
			return ResponseEntity.ok(
					WalletResponse.builder()
						.code(ERROR_CODE)
						.msg("Insufficient Balance")
						.build());
		}
		
		// update the user/wallet balance
		walletService.updateWalletBalance(
				WalletBalanceRequest.builder()
					.walletId(tWallet.get().getId())
					.userId(cashoutRequest.getUserId())
					.amount(cashoutRequest.getAmout())
					.txnType(TransactionType.CASHOUT)
					.build()
				);
		
		// insert the cash out record into the database
		WalletTransactionDTO walletDTO = new WalletTransactionDTO();
		walletDTO.setWalletId(tWallet.get().getId());
		walletDTO.setUserId(cashoutRequest.getUserId());
		walletDTO.setQuantity(cashoutRequest.getAmout());
		walletDTO.setUnitPrice(cashoutRequest.getUnitPrice());
		walletDTO.setTransactionType(TransactionType.CASHOUT);
		walletDTO.setMerchantName(card.get().getCardName());
		walletDTO.setMerchantNickName(card.get().getCardName());
		walletDTO.setBankName(card.get().getBankName());
		walletDTO.setAccountNumber(card.get().getCardNumber());
		walletDTO.setOrderNumber(UUID.randomUUID().toString());
		walletDTO.setTransactionStatus(SUCCESS);
		TWalletTransactionHistory txnHistory = walletTransactionRepository.save(requestToModelMapper(walletDTO));
		
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_CREATED_SUCCESS, null, Locale.US))
						.result(modelToDTOMapper(txnHistory))
						.build());
		
	}
	
	private WalletTransactionDTO modelToDTOMapper(TWalletTransactionHistory walletTransaction) {
		return WalletTransactionDTO.builder()
				.id(walletTransaction.getId())
				.walletId(walletTransaction.getWalletId())
				.userId(walletTransaction.getUserId())
				.quantity(walletTransaction.getQuantity()) // Txn amount
				.unitPrice(walletTransaction.getUnitPrice())
				.transactionType(walletTransaction.getTransactionType())
				.orderNumber(walletTransaction.getOrderNumber())
				.merchantName(walletTransaction.getMerchantName())
				.merchantId(walletTransaction.getMerchantId())
				.merchantNickName(walletTransaction.getMerchantNickName())
				.bankName(walletTransaction.getBankName())
				.accountNumber(walletTransaction.getAccountNumber())
				.transactionStatus(walletTransaction.getTransactionStatus())
				.orderDateTime(walletTransaction.getOrderDateTime())
				.modifyDate(walletTransaction.getModifyDate())
				.build();
	}
	
	private TWalletTransactionHistory requestToModelMapper(WalletTransactionDTO dto) {
		return TWalletTransactionHistory.builder()
				.walletId(dto.getWalletId())
				.userId(dto.getUserId())
				.quantity(dto.getQuantity()) // Txn amount
				.unitPrice(dto.getUnitPrice())
				.transactionType(dto.getTransactionType())
				.orderNumber(dto.getOrderNumber())
				.merchantName(dto.getMerchantName())
				.merchantId(dto.getMerchantId())
				.merchantNickName(dto.getMerchantNickName())
				.bankName(dto.getBankName())
				.accountNumber(dto.getAccountNumber())
				.transactionStatus(dto.getTransactionStatus())
				.build();
	}
	
	private TCards dtoModelMapper(CardRequest request) {
		return TCards.builder()
					.userId(request.getUserId())
					.cardNumber(request.getCardNumber())
					.cardName(request.getCardName())
					.bankName(request.getBankName())
					.maskedCardNumber(WalletUtility.maskCardNumber(request.getCardNumber()))
					.build();
	}
	
	private CardDTO dtoMapper(TCards card) {
		return CardDTO.builder()
					.id(card.getId())
					.userId(card.getUserId())
					.cardNumber(card.getMaskedCardNumber())
					.cardName(card.getCardName())
					.bankName(card.getBankName())
					.createDate(card.getCreateDate())
					.modifyDate(card.getModifyDate())
					.build();
	}
}
