package com.wildfirechat.wallet.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TopupInfoDTO implements Serializable{
	private String orderNumber;
	private String cardName;
	private String merchantId;
	private String nickName;
	private String bankName;
	private String cardNumber;
}
