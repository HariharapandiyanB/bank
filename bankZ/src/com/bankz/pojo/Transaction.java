package com.bankz.pojo;

import java.time.LocalDateTime;

public class Transaction {
	private int transactionId;
	private String transcationType;
	private int transcationAmount;
	private int balance;
	private long timeStamp;
	private int customerId;
	private long primaryAccNum;
	private long secondaryAccNum;
	private String description;
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getTranscationType() {
		return transcationType;
	}
	public void setTranscationType(String transcationType) {
		this.transcationType = transcationType;
	}
	public int getTranscationAmount() {
		return transcationAmount;
	}
	public void setTranscationAmount(int transcationAmount) {
		this.transcationAmount = transcationAmount;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public long getPrimaryAccNum() {
		return primaryAccNum;
	}
	public void setPrimaryAccNum(long primaryAccNum) {
		this.primaryAccNum = primaryAccNum;
	}
	public long getSecondaryAccNum() {
		return secondaryAccNum;
	}
	public void setSecondaryAccNum(long secondaryAccNum) {
		this.secondaryAccNum = secondaryAccNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
