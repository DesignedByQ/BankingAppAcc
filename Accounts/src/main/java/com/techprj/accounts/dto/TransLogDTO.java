package com.techprj.accounts.dto;

import java.time.LocalDate;

public class TransLogDTO {
	
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

}
