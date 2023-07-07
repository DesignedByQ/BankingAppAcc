package com.techprj.accounts.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long accountId;
	private String type;
	private Double balance;
	private Long[] userProfileID = new Long[2];
	@OneToMany(mappedBy="account", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransLog> translog = new ArrayList<>();
	
	public Account() {
		super();
	}

	public Account(Long accountId, String type, Double balance, Long[] userProfileID, List<TransLog> translog) {
		super();
		this.accountId = accountId;
		this.type = type;
		this.balance = balance;
		this.userProfileID = userProfileID;
		this.translog = translog;
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

	public Long[] getUserProfileID() {
		return userProfileID;
	}

	public void setUserProfileID(Long[] userProfileID) {
		this.userProfileID = userProfileID;
	}

	public List<TransLog> getTranslog() {
		return translog;
	}

	public void setTranslog(List<TransLog> translog) {
		this.translog = translog;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", type=" + type + ", balance=" + balance + ", userProfileID="
				+ Arrays.toString(userProfileID) + ", translog=" + translog + "]";
	}
	
}
