package com.bankz.pojo;



public class Employee extends User{
	
	
	private String department;
	private String branch;
		
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
}
