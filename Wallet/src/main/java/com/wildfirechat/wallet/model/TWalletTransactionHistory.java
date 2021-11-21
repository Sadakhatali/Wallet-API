package com.wildfirechat.wallet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "t_wallet_transaction_history")
@AllArgsConstructor
@NoArgsConstructor
public class TWalletTransactionHistory implements Serializable{
	
	private static final long serialVersionUID = 653340835536911380L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long walletId;
	private String userId;
	private Float quantity; // Quantity
	private Float unitPrice;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	private String orderNumber;
	private String merchantName;
	private String merchantId;
	private String merchantNickName;
	private String bankName;
	private String accountNumber;
	private String transactionStatus;
	private String transactingUsername;
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date orderDateTime;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;
    
}
