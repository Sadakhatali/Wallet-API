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
public class CardDTO {
	
	private Long id;
	private String userId;
	private String cardNumber;
	private String cardName;
	private String bankName;
	private Date createDate;
    private Date modifyDate;
    
}
