package com.bankz.pojo;

public class Branch {
	private String name;
	private int branchId;
	private String iFSCcode;
	private String address;
	private int numOfActiveEmployees;
	private int numOfActiveCustomers;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getiFSCcode() {
		return iFSCcode;
	}
	public void setiFSCcode(String iFSCcode) {
		this.iFSCcode = iFSCcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getNumOfActiveEmployees() {
		return numOfActiveEmployees;
	}
	public void setNumOfActiveEmployees(int numOfActiveEmployees) {
		this.numOfActiveEmployees = numOfActiveEmployees;
	}
	public int getNumOfActiveCustomers() {
		return numOfActiveCustomers;
	}
	public void setNumOfActiveCustomers(int numOfActiveCustomers) {
		this.numOfActiveCustomers = numOfActiveCustomers;
	}
	
}
