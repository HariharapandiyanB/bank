package com.bankz.pojo;

public class Account {
	private long accountNum;
	private int customerId;
	private String type;
	private int balance;
	private String status;
	private int branchId;
	public long getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(long accountNum) {
		this.accountNum = accountNum;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
}
