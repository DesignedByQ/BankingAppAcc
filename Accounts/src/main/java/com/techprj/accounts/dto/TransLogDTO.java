package com.techprj.accounts.dto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.logging.Logger;

public class TransLogDTO implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(TransLogDTO.class.getName());
	
	private Long transLogId;
	private LocalDate date;
	private Double oldBal;
	private Long from;
	private Double amount;
	private String reference;
	private Double newBal;
	private Long to;
	
	public TransLogDTO() {
		super();
	}

	public TransLogDTO(Long transLogId, LocalDate date, Double oldBal, Long from, Double amount, String reference,
			Double newBal, Long to) {
		super();
		this.transLogId = transLogId;
		this.date = date;
		this.oldBal = oldBal;
		this.from = from;
		this.amount = amount;
		this.reference = reference;
		this.newBal = newBal;
		this.to = to;
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
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
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
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "TransLogDTO [transLogId=" + transLogId + ", date=" + date + ", oldBal=" + oldBal + ", from=" + from
				+ ", amount=" + amount + ", reference=" + reference + ", newBal=" + newBal + ", to=" + to + "]";
	}
	
    public static TransLogDTO deserialize(byte[] bytes) {
        LOGGER.info("Deserialization started.");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            TransLogDTO obj = (TransLogDTO) in.readObject();
            LOGGER.info("Deserialization successful.");
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.warning("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
