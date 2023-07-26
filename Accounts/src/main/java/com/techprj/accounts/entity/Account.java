package com.techprj.accounts.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Table;

//import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.techprj.accounts.dto.TransLogDTO;

@Entity
@Table(name="accounts")
public class Account implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Account.class.getName());
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long accountId;
	private Long sortCode;
	private String type;
	private Double balance;
	@Column(name = "user_profileid")
	private Long[] userProfileID = new Long[2];
	@OneToMany(mappedBy="account", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransLog> translog = new ArrayList<>();
	
	public Account() {
		super();
	}

	public Account(Long accountId, Long sortCode, String type, Double balance, Long[] userProfileID, List<TransLog> translog) {
		super();
		this.accountId = accountId;
		this.sortCode = sortCode;
		this.type = type;
		this.balance = balance;
		this.userProfileID = userProfileID;
		this.translog = translog;
	}
	
//	@Autowired
//	ModelMapper modelMapper;

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

	public Long[] getUserProfileID() {
		return userProfileID;
	}

	public void setUserProfileID(Long[] userProfileID) {
		this.userProfileID = userProfileID;
	}

	public List<TransLog> getTranslog() {
		return translog;
	}
	
	public List<TransLogDTO> getTranslogDTO() {
		
		List<TransLogDTO> tdtol = new ArrayList();
		
		for(TransLog t: translog) {
			
			TransLogDTO tdto = new TransLogDTO();
			
			tdto.setAmount(t.getAmount());
			tdto.setDate(t.getDate());
			tdto.setFrom(t.getFrom());
			tdto.setNewBal(t.getNewBal());
			tdto.setOldBal(t.getOldBal());
			tdto.setReference(t.getReference());
			tdto.setTo(t.getTo());
			tdto.setTransLogId(t.getTransLogId());
			
			tdtol.add(tdto);
			
		}	
		
		return tdtol;
		
	}

	public void setTranslog(List<TransLog> translog) {
		this.translog = translog;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", sortCode=" + sortCode + ", type=" + type + ", balance=" + balance + ", userProfileID="
				+ Arrays.toString(userProfileID) + ", translog=" + translog + "]";
	}
	
    public static Account deserialize(byte[] bytes) {
        LOGGER.info("Deserialization started.");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            Account obj = (Account) in.readObject();
            LOGGER.info("Deserialization successful.");
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

	
}
