package com.wildfirechat.wallet.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.wildfirechat.wallet.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WalletBalanceRequest {
	
	private Long walletId;
	private String userId;
	private Float amount;
	
	@Enumerated(EnumType.STRING)
	private TransactionType txnType;
	
}
