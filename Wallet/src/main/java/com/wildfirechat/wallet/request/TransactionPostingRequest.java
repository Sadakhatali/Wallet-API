package com.wildfirechat.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class TransactionPostingRequest {
	
	 private String orderNumber;
	 private Float amount;
	 private String remarks;
	 private String cardNumber;

}
