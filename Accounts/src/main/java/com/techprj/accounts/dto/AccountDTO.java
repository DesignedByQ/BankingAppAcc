package com.techprj.accounts.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.techprj.accounts.entity.TransLog;
import com.techprj.banking.dto.UserProfileDTO;
import com.techprj.banking.dto.UserProfileDTODeserializer;

public class AccountDTO implements Serializable {
	
	private Long accountId;
	private Long sortCode;
	private String type;
	private Double balance;
	//@JsonDeserialize(using = UserProfileDTODeserializer.class)
	private List<UserProfileDTO> userProfileDTO;
	private List<TransLogDTO> transLogDTO;
	
	@Autowired
	ModelMapper modelMapper;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(Long accountId, Long sortCode, String type, Double balance, List<UserProfileDTO> userProfileDTO,
			List<TransLogDTO> transLogDTO) {
		super();
		this.accountId = accountId;
		this.sortCode = sortCode;
		this.type = type;
		this.balance = balance;
		this.userProfileDTO = userProfileDTO;
		this.transLogDTO = transLogDTO;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getSortCode() {
		return sortCode;
	}

	public void setSortCode(Long sortCode) {
		this.sortCode = sortCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public List<UserProfileDTO> getUserProfileDTO() {
		return userProfileDTO;
	}

	public void setUserProfileDTO(List<UserProfileDTO> userProfileDTO) {
		this.userProfileDTO = userProfileDTO;
	}

	public List<TransLogDTO> getTransLogDTO() {
		
		return transLogDTO;
	}

	public void setTransLogDTO(List<TransLogDTO> list) {
		
		this.transLogDTO = list;
	}

	@Override
	public String toString() {
		return "AccountDTO [accountId=" + accountId + ", sortCode=" + sortCode + ", type=" + type + ", balance="
				+ balance + ", userProfileDTO=" + userProfileDTO + ", transLogDTO=" + transLogDTO + "]";
	}

}
