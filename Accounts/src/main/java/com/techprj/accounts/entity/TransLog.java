package com.techprj.accounts.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.logging.Logger;
import javax.persistence.Table;

//import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.techprj.accounts.dto.TransLogDTO;
import com.techprj.banking.entity.UserProfile;

import jakarta.persistence.JoinColumn;

@Entity
@Table(name="trans")
public class TransLog implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(TransLog.class.getName());
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long transLogId;
	private LocalDate date;
	private Double oldBal;
	private Long fromAcc;
	private Double amount;
	private String reference;
	private Double newBal;
	private Long toAcc;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="account_id", nullable=false)
	private Account account;
	
	public TransLog() {
		super();
	}

	public TransLog(Long transLogId, LocalDate date, Double oldBal, Long from, Double amount, String reference,
			Double newBal, Long to, Account account) {
		super();
		this.transLogId = transLogId;
		this.date = date;
		this.oldBal = oldBal;
		this.fromAcc = from;
		this.amount = amount;
		this.reference = reference;
		this.newBal = newBal;
		this.toAcc = to;
		this.account = account;
	}

	public Long getTransLogId() {
		return transLogId;
	}

	public void setTransLogId(Long transLogId) {
		this.transLogId = transLogId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getOldBal() {
		return oldBal;
	}

	public void setOldBal(Double oldBal) {
		this.oldBal = oldBal;
	}

	public Long getFrom() {
		return fromAcc;
	}

	public void setFrom(Long from) {
		this.fromAcc = from;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Double getNewBal() {
		return newBal;
	}

	public void setNewBal(Double newBal) {
		this.newBal = newBal;
	}

	public Long getTo() {
		return toAcc;
	}

	public void setTo(Long to) {
		this.toAcc = to;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "TransLog [transLogId=" + transLogId + ", date=" + date + ", oldBal=" + oldBal + ", fromAcc=" + fromAcc
				+ ", amount=" + amount + ", reference=" + reference + ", newBal=" + newBal + ", toAcc=" + toAcc
				+ ", account=" + account + "]";
	}
	
    public static TransLog deserialize(byte[] bytes) {
        LOGGER.info("Deserialization started.");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            TransLog obj = (TransLog) in.readObject();
            LOGGER.info("Deserialization successful.");
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
