package com.wildfirechat.wallet.service.impl;

import static com.wildfirechat.wallet.constants.WalletConstants.CONFIRM;
import static com.wildfirechat.wallet.constants.WalletConstants.ERROR_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.PENDING;
import static com.wildfirechat.wallet.constants.WalletConstants.SUCCESS_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.TXN_CREATED_SUCCESS;
import static com.wildfirechat.wallet.constants.WalletConstants.TXN_DATA_LOADED;
import static com.wildfirechat.wallet.constants.WalletConstants.USER_ID_NOT_EXISTS;
import static com.wildfirechat.wallet.constants.WalletConstants.USER_ID_NULL;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wildfirechat.wallet.dto.ResultDTO;
import com.wildfirechat.wallet.dto.WalletTransactionDTO;
import com.wildfirechat.wallet.model.TWallet;
import com.wildfirechat.wallet.model.TWalletTransactionHistory;
import com.wildfirechat.wallet.repository.WalletRepository;
import com.wildfirechat.wallet.repository.WalletTransactionRepository;
import com.wildfirechat.wallet.request.TransactionPostingRequest;
import com.wildfirechat.wallet.request.WalletBalanceRequest;
import com.wildfirechat.wallet.request.WalletTransactionRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.WalletService;
import com.wildfirechat.wallet.service.WalletTransactionService;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${getCardBaseUrl}")
	private String cardBaseUrl;
	
	@Value("${topupBaseUrl}")
	private String topupBaseUrl;
	
	private final Logger logger = LoggerFactory.getLogger("mainlogger");
	public static final String CLAZZ = "WalletTransactionServiceImpl.java - ";
	
	@Override
	public ResponseEntity<WalletResponse> createWalletTransaction(WalletTransactionRequest walletTransactionRequest) {
		logger.info(CLAZZ+"createWalletTransaction implementation has been started ");
		
		// validate the user id
		if(null == walletTransactionRequest.getUserId()) {
			return ResponseEntity.ok()
					.body(WalletResponse.builder()
							.code(ERROR_CODE)
							.msg(messageSource.getMessage(USER_ID_NULL, null, Locale.US))
							.build());
		}
		
		Optional<TWallet> wallet = walletRepository.findByUserId(walletTransactionRequest.getUserId());
		
		if(!wallet.isPresent()) {
			return ResponseEntity.ok()
					.body(WalletResponse.builder()
							.code(ERROR_CODE)
							.msg(messageSource.getMessage(USER_ID_NOT_EXISTS, null, Locale.US))
							.build());
		}
		
		// Load wallet id if not given in the request
		if(null == walletTransactionRequest.getWalletId()) {
			walletTransactionRequest.setWalletId(wallet.get().getId());
		}
		
		// get the topup data
		String result = restTemplate.getForObject(cardBaseUrl, String.class);
		Gson gson = new Gson();
		ResultDTO responseResult = gson.fromJson(result, ResultDTO.class);
		walletTransactionRequest.setOrderNumber(responseResult.getData().getOrderNumber());
		walletTransactionRequest.setMerchantName(responseResult.getData().getCardName());
		walletTransactionRequest.setMerchantId(responseResult.getData().getMerchantId());
		walletTransactionRequest.setMerchantNickName(responseResult.getData().getNickName());
		walletTransactionRequest.setBankName(responseResult.getData().getBankName());
		walletTransactionRequest.setAccountNumber(responseResult.getData().getCardNumber());
		walletTransactionRequest.setTransactionStatus(PENDING);
		
		// create a transaction
		TWalletTransactionHistory txnHistory = walletTransactionRepository.save(requestToModelMapper(walletTransactionRequest));
		
		// update the user/wallet balance
		walletService.updateWalletBalance(
				WalletBalanceRequest.builder()
					.walletId(walletTransactionRequest.getWalletId())
					.userId(walletTransactionRequest.getUserId())
					.amount(walletTransactionRequest.getQuantity())
					.txnType(walletTransactionRequest.getTransactionType())
					.build()
				);
		
		logger.info(CLAZZ+"createWalletTransaction implementation has been ended ");
		return ResponseEntity.ok()
					.body(WalletResponse.builder()
							.code(SUCCESS_CODE)
							.msg(messageSource.getMessage(TXN_CREATED_SUCCESS, null, Locale.US))
							.result(modelToDTOMapper(txnHistory))
							.build());
	}
	
	public ResponseEntity<WalletResponse> getAllWalletTxns() {
		logger.info(CLAZZ+"getAllWalletTxns implementation has been started ");
		List<TWalletTransactionHistory> allWalletTxns = walletTransactionRepository.findAll();
		
		List<WalletTransactionDTO> txnList = allWalletTxns.stream()
					 .map(txnHistory -> modelToDTOMapper(txnHistory))
					 .collect(Collectors.toList());
		
		logger.info(CLAZZ+"getAllWalletTxns implementation has been ended ");
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_DATA_LOADED, null, Locale.US))
						.result(txnList)
						.build());
	}
	
	public ResponseEntity<WalletResponse> getWalletIdTxns(Long walletId) {
		logger.info(CLAZZ+"getWalletIdTxns implementation has been started ");
		List<TWalletTransactionHistory> walletTxns = walletTransactionRepository.findByWalletId(walletId);
		
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_DATA_LOADED, null, Locale.US))
						.result(walletTxns.stream().map(model -> modelToDTOMapper(model)).collect(Collectors.toList()))
						.build());
	}
	
	public ResponseEntity<WalletResponse> getWalletTxnsByUserId(String userId) {
		logger.info(CLAZZ+"getWalletTxnsByUserId implementation has been started ");
		List<TWalletTransactionHistory> walletTxns = walletTransactionRepository.findByUserId(userId);
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_DATA_LOADED, null, Locale.US))
						.result(walletTxns.stream().map(model -> modelToDTOMapper(model)).collect(Collectors.toList()))
						.build());
	}
	
	public ResponseEntity<WalletResponse> getTxnsById(Long txnId) {
		logger.info(CLAZZ+"getTxnsById implementation has been started ");
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_DATA_LOADED, null, Locale.US))
						.result(modelToDTOMapper(walletTransactionRepository.findById(txnId).get()))
						.build());
	}
	
	public ResponseEntity<WalletResponse> getTopupInfo() {
		logger.info(CLAZZ+"getTopupInfo implementation has been started ");
		String result = restTemplate.getForObject(cardBaseUrl, String.class);
		Gson gson = new Gson();
		ResultDTO responseResult = gson.fromJson(result, ResultDTO.class);
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg("Data Loaded successfully")
						.result(responseResult)
						.build());
	}
	
	public ResponseEntity<WalletResponse> confirmTxn(String orderNumber) {
		logger.info(CLAZZ+"confirmTxn implementation has been started ");
		Optional<TWalletTransactionHistory> txnHistoryObj = walletTransactionRepository.findByOrderNumber(orderNumber);
		TWalletTransactionHistory txnHistory = txnHistoryObj.get();
		txnHistory.setTransactionStatus(CONFIRM);
		TWalletTransactionHistory txnHist = walletTransactionRepository.save(txnHistory);
		
		TransactionPostingRequest tpRequest = new TransactionPostingRequest();
		tpRequest.setOrderNumber(txnHist.getOrderNumber());
		tpRequest.setAmount(txnHist.getQuantity());
		tpRequest.setCardNumber(txnHist.getAccountNumber());
		
		// send the order details to update 
		String responseResult = restTemplate.postForObject(topupBaseUrl,tpRequest,String.class);
		
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(TXN_CREATED_SUCCESS, null, Locale.US))
						.result(responseResult)
						.build());
		
	}
	
	private TWalletTransactionHistory requestToModelMapper(WalletTransactionRequest walletTransactionRequest) {
		return TWalletTransactionHistory.builder()
				.walletId(walletTransactionRequest.getWalletId())
				.userId(walletTransactionRequest.getUserId())
				.quantity(walletTransactionRequest.getQuantity()) // Txn amount
				.unitPrice(walletTransactionRequest.getUnitPrice())
				.transactionType(walletTransactionRequest.getTransactionType())
				.orderNumber(walletTransactionRequest.getOrderNumber())
				.merchantName(walletTransactionRequest.getMerchantName())
				.merchantId(walletTransactionRequest.getMerchantId())
				.merchantNickName(walletTransactionRequest.getMerchantNickName())
				.bankName(walletTransactionRequest.getBankName())
				.accountNumber(walletTransactionRequest.getAccountNumber())
				.transactionStatus(walletTransactionRequest.getTransactionStatus())
				.transactingUsername(walletTransactionRequest.getTransactingUsername())
				.build();
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

}
