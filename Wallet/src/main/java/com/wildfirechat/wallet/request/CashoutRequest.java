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
public class CashoutRequest {

	private String cardId;
	private String userId;
	private Float amout;
	private Float unitPrice;

}
