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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class ResultDTO implements Serializable {

	private boolean success;
	private String msg;
	private Integer code;
	private long timestamp = System.currentTimeMillis();
	private TopupInfoDTO data;
}