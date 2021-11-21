package com.wildfirechat.wallet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "t_wallet_config")
@AllArgsConstructor
@NoArgsConstructor
public class TWalletConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "min_value")
	private Float minValue;
	
	@Column(name = "max_value")
	private Float maxValue;

	@Column(name = "topup_values")
	private Float topupValues;	
}

