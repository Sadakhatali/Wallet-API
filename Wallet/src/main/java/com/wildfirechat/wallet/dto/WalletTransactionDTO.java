package com.wildfirechat.wallet.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.wildfirechat.wallet.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class WalletTransactionDTO implements Serializable{

	private static final long serialVersionUID = -8188379292462796382L;
	
	private Long id;
	private Long walletId;
	private String userId;
	private Float quantity; // txnAmount
	private Float unitPrice;
	private TransactionType transactionType;
	private String orderNumber;
	private String merchantName;
	private String merchantId;
	private String merchantNickName;
	private String bankName;
	private String accountNumber;
	private String transactionStatus;
	private Date orderDateTime;
    private Date modifyDate;
	
}
