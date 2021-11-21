package com.wildfirechat.wallet.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDTO {
	
	private Long id;
	private String userId;
	private Float availableBalance;
	private Boolean status;
	private Date createDate;
    private Date modifyDate;
	
}
