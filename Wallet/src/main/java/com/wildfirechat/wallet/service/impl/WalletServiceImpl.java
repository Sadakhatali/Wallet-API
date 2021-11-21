package com.wildfirechat.wallet.service.impl;

import static com.wildfirechat.wallet.constants.WalletConstants.CASHOUT;
import static com.wildfirechat.wallet.constants.WalletConstants.ERROR_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.SUCCESS_CODE;
import static com.wildfirechat.wallet.constants.WalletConstants.TOPUP;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_ACTIVATED;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_BAL_UPDATE;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_CREATED;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_DATA_FOUND;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_DEACTIVATED;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_FAILED;
import static com.wildfirechat.wallet.constants.WalletConstants.WALLET_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wildfirechat.wallet.dto.WalletDTO;
import com.wildfirechat.wallet.dto.WalletRequestDTO;
import com.wildfirechat.wallet.model.TWallet;
import com.wildfirechat.wallet.model.TWalletConfig;
import com.wildfirechat.wallet.repository.WalletConfigRepository;
import com.wildfirechat.wallet.repository.WalletRepository;
import com.wildfirechat.wallet.request.WalletBalanceRequest;
import com.wildfirechat.wallet.request.WalletStatusRequest;
import com.wildfirechat.wallet.response.WalletResponse;
import com.wildfirechat.wallet.service.WalletService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private WalletConfigRepository walletConfigRepository;
	
	private final Logger logger = LoggerFactory.getLogger("mainlogger");
	public static final String CLAZZ = "WalletServiceImpl.java - ";
	
	@Override
	public ResponseEntity<WalletResponse> createWallet(String userId) {
		logger.info(CLAZZ+"createWallet implementation has been started ");
		ResponseEntity<WalletResponse> result = null;
		Optional<TWallet> walletData =  walletRepository.findByUserId(userId);
		if(!walletData.isPresent()) {
			TWallet tWallet = walletRepository.save(requestToModelMapper(userId));
			
			result = ResponseEntity.ok()
						.body(WalletResponse.builder()
								.code(SUCCESS_CODE)
								.msg(messageSource.getMessage(WALLET_CREATED, null, Locale.US))
								.result(modelDTOMapper(tWallet, userId))
								.build());
		} else {
			TWallet tWallet = walletRepository.findByUserId(userId).get();
			
			result = ResponseEntity.ok()
					.body(WalletResponse.builder()
							.code(SUCCESS_CODE)
							.msg("Wallet already exits")
							.result(modelDTOMapper(tWallet, userId))
							.build());
				
		}
		logger.info(CLAZZ+"createWallet implementation has been ended ");
		return result;
	}
	
	private WalletDTO modelDTOMapper(TWallet tWallet, String userId) {
		List<TWalletConfig> walletConfig = walletConfigRepository.findAll();
		String username = walletConfigRepository.loadUsername(userId);
		List<Float> topupValues = new ArrayList<Float>();
		
		for(TWalletConfig topupVal: walletConfig) {
			topupValues.add(topupVal.getTopupValues());
		}
		
		return WalletDTO.builder()
				.userId(tWallet.getUserId())
				.userName(username)
				.balance(tWallet.getAvailableBalance())
				.minValue(Optional.ofNullable(walletConfig).get().get(0).getMinValue())
				.maxValue(Optional.ofNullable(walletConfig).get().get(0).getMaxValue())
				.topupValue(topupValues)
				.build();
	}
	
	public ResponseEntity<WalletResponse> walletStatusChange(WalletStatusRequest walletStatusRequest) {
		logger.info(CLAZZ+"walletStatusChange implementation has been started ");
		ResponseEntity<WalletResponse> result = null;
		// Load the wallet object
		Optional<TWallet> walletData =  walletRepository.findByUserId(walletStatusRequest.getUserId());
		if(walletData.isPresent()) {
			TWallet wallet = walletData.get();
			wallet.setStatus(walletStatusRequest.getStatus());
			
			String message = messageSource.getMessage(WALLET_ACTIVATED, null, Locale.US); 
			if(walletStatusRequest.getStatus() == Boolean.FALSE) {
				message = messageSource.getMessage(WALLET_DEACTIVATED, null, Locale.US);
			}
			
			result = ResponseEntity.ok()
						.body(WalletResponse.builder()
								.code(SUCCESS_CODE)
								.msg(message)
								.result(modelToDTOMapper(walletRepository.save(wallet)))
								.build());
		} else {
			result = ResponseEntity.badRequest()
					.body(WalletResponse.builder()
							.code(ERROR_CODE)
							.msg(messageSource.getMessage(WALLET_NOT_FOUND, null, Locale.US))
							.build());
		}
		logger.info(CLAZZ+"walletStatusChange implementation has been ended ");
		return result;
	}
	
	public ResponseEntity<WalletResponse> getAllWallet() {
		logger.info(CLAZZ+"getAllWallet implementation has been started ");
		List<TWallet> walletList = walletRepository.findAll();
		List<WalletRequestDTO> wallet = 
				walletList.stream()
					.map(model -> modelToDTOMapper(model))
					.collect(Collectors.toList());
		
		logger.info(CLAZZ+"getAllWallet implementation has been ended ");
		
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(WALLET_DATA_FOUND, null, Locale.US))
						.result(wallet)
						.build());
		
	}
	
	public ResponseEntity<WalletResponse> getWalletById(Long walletId) {
		logger.info(CLAZZ+"getWalletById implementation has been started ");
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(WALLET_DATA_FOUND, null, Locale.US))
						.result(modelToDTOMapper(walletRepository.findById(walletId).get()))
						.build());
	}
	
	public ResponseEntity<WalletResponse> updateWalletBalance(WalletBalanceRequest walletBalanceRequest) {
		logger.info(CLAZZ+"updateWalletBalance implementation has been started ");
		Optional<TWallet> walletData = walletRepository.findByUserId(walletBalanceRequest.getUserId());
		TWallet wallet = walletData.get();
		
		if(Objects.equals(walletBalanceRequest.getTxnType().toString(), TOPUP)) {
			wallet.setAvailableBalance(wallet.getAvailableBalance()+walletBalanceRequest.getAmount());
		} else if(Objects.equals(walletBalanceRequest.getTxnType().toString(), CASHOUT)) {
			wallet.setAvailableBalance(wallet.getAvailableBalance()-walletBalanceRequest.getAmount());
		}
		logger.info(CLAZZ+"updateWalletBalance implementation has been ended ");
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(WALLET_BAL_UPDATE, null, Locale.US))
						.result(modelToDTOMapper(walletRepository.save(wallet)))
						.build());
	}
	
	public ResponseEntity<WalletResponse> getWalletByUserId(String userId) {
		Optional<TWallet> wallet = walletRepository.findByUserId(userId);
		List<TWalletConfig> walletConfig = walletConfigRepository.findAll();
		String username = walletConfigRepository.loadUsername(userId);
		List<Float> topupValues = new ArrayList<Float>();
		
		for(TWalletConfig topupVal: walletConfig) {
			topupValues.add(topupVal.getTopupValues());
		}
		
		WalletDTO walletDTO = WalletDTO.builder()
				.userId(wallet.get().getUserId())
				.userName(username)
				.balance(wallet.get().getAvailableBalance())
				.minValue(Optional.ofNullable(walletConfig).get().get(0).getMinValue())
				.maxValue(Optional.ofNullable(walletConfig).get().get(0).getMaxValue())
				.topupValue(topupValues)
				.build();
		
		return ResponseEntity.ok()
				.body(WalletResponse.builder()
						.code(SUCCESS_CODE)
						.msg(messageSource.getMessage(WALLET_DATA_FOUND, null, Locale.US))
						.result(walletDTO)
						.build());
		
	}
	
	private TWallet requestToModelMapper(String userId) {
		return TWallet.builder()
					.userId(userId)
					.status(Boolean.FALSE)
					.availableBalance(0f)
					.build();
	}
	
	private WalletRequestDTO modelToDTOMapper(TWallet tWallet) {
		return WalletRequestDTO.builder()
					.id(tWallet.getId())
					.userId(tWallet.getUserId())
					.status(tWallet.getStatus())
					.availableBalance(tWallet.getAvailableBalance())
					.createDate(tWallet.getCreateDate())
					.modifyDate(tWallet.getModifyDate())
					.build();
	}

}
