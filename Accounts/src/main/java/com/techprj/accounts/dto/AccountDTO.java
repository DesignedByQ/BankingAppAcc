package com.techprj.accounts.dto;

import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.techprj.banking.dto.UserProfileDTO;
import com.techprj.banking.dto.UserProfileDTODeserializer;

public class AccountDTO {
	
	private Long accountId;
	private String type;
	private Double balance;
	@JsonDeserialize(using = UserProfileDTODeserializer.class)
	private UserProfileDTO[] userProfileDTO = new UserProfileDTO[2];
	private TransLogDTO[] transLogDTO;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(Long accountId, String type, Double balance, UserProfileDTO[] userProfileDTO,
			TransLogDTO[] transLogDTO) {
		super();
		this.accountId = accountId;
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

	public UserProfileDTO[] getUserProfileDTO() {
		return userProfileDTO;
	}

	public void setUserProfileDTO(UserProfileDTO[] userProfileDTO) {
		this.userProfileDTO = userProfileDTO;
	}

	public TransLogDTO[] getTransLogDTO() {
		return transLogDTO;
	}

	public void setTransLogDTO(TransLogDTO[] transLogDTO) {
		this.transLogDTO = transLogDTO;
	}

	@Override
	public String toString() {
		return "AccountDTO [accountId=" + accountId + ", type=" + type + ", balance=" + balance + ", userProfileDTO="
				+ Arrays.toString(userProfileDTO) + ", transLogDTO=" + Arrays.toString(transLogDTO) + "]";
	}

}
