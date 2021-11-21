package com.wildfirechat.wallet.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class WalletDTO implements Serializable {
	
	private String userId;
	private String userName;
	private Float balance;
	private Float minValue;
	private Float maxValue;
	private List<Float> topupValue;
	
}
