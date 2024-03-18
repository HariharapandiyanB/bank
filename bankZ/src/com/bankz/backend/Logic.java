package com.bankz.backend;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.enums.UserStatus;
import com.bankz.helper.Supplement;
import com.bankz.helper.Verification;
import com.bankz.persistence.AdminPersistence;
import com.bankz.persistence.CustomerPersistence;
import com.bankz.persistence.EmployeePersistence;
import com.bankz.persistence.UserPersistence;
import com.bankz.pojo.Account;
import com.bankz.pojo.Branch;
import com.bankz.pojo.Customer;
import com.bankz.pojo.Employee;
import com.bankz.pojo.Transaction;
import com.bankz.pojo.User;
import com.bankz.utilities.BankException;
import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;

public class Logic{
	CustomerPersistence cp=new CustomerPersistence();
	EmployeePersistence ep=new EmployeePersistence();
	UserPersistence up=new UserPersistence();
	AdminPersistence ap=new AdminPersistence();
	Verification verify=new Verification();
	
	public User login(int userId,String password)throws InvalidInputException, NoSuchAlgorithmException, SQLException{
		UtilityTasks.checkNull(password);
		UtilityTasks.checkNull(userId);
		String encryptedPassword=encryptPassword(password);
		User user=cp.login(userId);
		if(user.getPassword().equals(encryptedPassword)) {
			return user;
		}else {
			throw new InvalidInputException("Incorrect Credentials!");
		}
	}
	
	public String encryptPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest mD=MessageDigest.getInstance("SHA-256");
		mD.update(password.getBytes());
		byte[] bytes =mD.digest();
		StringBuilder encryptedPassword= new StringBuilder();
		int numOfBytes=bytes.length;
		for(int i=0;i<numOfBytes;i++) {
			encryptedPassword.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return encryptedPassword.toString();
	}
	
	public List<Account> getAccounts(int userId,int pageNum) throws SQLException, InvalidInputException {
		UtilityTasks.checkNull(userId);
		return cp.getAccountInfo(userId,pageNum);
	}
	public Customer getCustomerPersonalInfo(int userId) throws SQLException, InvalidInputException{
		UtilityTasks.checkNull(userId);
		return cp.getPersonalInfo(userId);
	}
	
	public Employee getEmployeePersonalInfo(int userId) throws SQLException, InvalidInputException{
		UtilityTasks.checkNull(userId);
		return ep.getPersonalInfo(userId);
	}
	public void deposit(int userId,long accountNum,int amount) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(userId);
		UtilityTasks.checkNull(amount);
		Account account = cp.getSingleAccountInfo(accountNum);
		account.setBalance(account.getBalance()+amount);
		long millisTime=Supplement.currentTimeInMillis();
		
		cp.selfTransaction(account);
		cp.transactionEntry(account, amount, null, millisTime, "Deposit");
	}
	public void withDrawAmount(int userId,long accountNum,int amount) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(userId);
		UtilityTasks.checkNull(amount);
		Account account=cp.getSingleAccountInfo(accountNum);
		int availableBalance=account.getBalance();
		if (availableBalance>amount) {
			account.setBalance(account.getBalance()-amount);
		}long millisTime=Supplement.currentTimeInMillis();
		
		cp.selfTransaction(account);
		cp.transactionEntry(account, amount, null, millisTime, "Withdrawal");
	}
	
	public void moneyTransfer(int userId,int amount,long senderAccountNum, long receiverAccountNum,String description)throws SQLException,InvalidInputException, BankException{
		UtilityTasks.checkNull(userId);
		UtilityTasks.checkNull(amount);
		UtilityTasks.checkNull(receiverAccountNum);
		UtilityTasks.checkNull(description);
		Account senderAccount=cp.getSingleAccountInfo(senderAccountNum);
		
		Account receiverAccount=cp.getSingleAccountInfo(receiverAccountNum);
		int availableBalance=senderAccount.getBalance();
		if (availableBalance>amount && receiverAccount!=null) {
			senderAccount.setBalance(availableBalance-amount);
			receiverAccount.setBalance(receiverAccount.getBalance()+amount);
			long millisTime=Supplement.currentTimeInMillis();
			
			cp.selfTransaction(senderAccount);
			cp.selfTransaction(receiverAccount);
			cp.transactionEntry(senderAccount, amount, receiverAccountNum, millisTime, description);
			
		}else if (availableBalance>amount) {
			senderAccount.setBalance(availableBalance-amount);
			long millisTime=Supplement.currentTimeInMillis();
			
			cp.selfTransaction(senderAccount);
			cp.transactionEntry(senderAccount, amount, receiverAccountNum, millisTime, description);
			
		}
		
		else {
			throw new BankException("Not Sufficient Balance");
		}
		
		
		
	}
	
	public List<Transaction> getTransactionDetails(int userId,int pageNum) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(userId);
		List<Transaction> transactionList = new ArrayList<>();
		for(Object object : cp.getTransactionDetails(userId,pageNum)) {
			transactionList.add((Transaction)object);
		}
		return transactionList;
	}
	
	public void modifyInfo(int userId,String field,Object value)throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(field);
		UtilityTasks.checkNull(value);
		UtilityTasks.checkNull(userId);
		up.modifyInfo(userId, field, value);
	}
	
	public void changePassword(int userId,String password) throws SQLException,InvalidInputException,NoSuchAlgorithmException{
		UtilityTasks.checkNull(userId);
		UtilityTasks.checkNull(password);
		String encryptedPassword=encryptPassword(password);
		if(up.getPersonalInfo(userId).getPassword().equals(encryptedPassword)) {
			throw new InvalidInputException("It is same as the existing password!");
		}else {
			up.changePassword(encryptedPassword, userId);
		}
		
	}
	
	public UserStatus checkStatus(int userId) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(userId);
		return UserStatus.values()[up.checkStatus(userId)];
	}
	
	
	
	public void addAccount(Account account) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(account);
		List<Object> accountDetailsList=Arrays.asList(account.getAccountNum(),account.getCustomerId(),
														account.getType(),account.getBalance(),
														account.getStatus(),account.getBranchId());
		ep.addAccount(accountDetailsList);
		Branch branch=getBranchDetails(account.getBranchId());
		branch.setNumOfActiveCustomers(branch.getNumOfActiveCustomers()+1);
		ep.addAccountCount(branch);
	}
	
	public void removeAccount(long accountNum) throws InvalidInputException,SQLException{
		UtilityTasks.checkNull(accountNum);
		ep.removeAccount(accountNum);
	}
	
	public void blockAccount(long accountNumber) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(accountNumber);
		ep.blockAccount(accountNumber);
	}
	
	public void addCustomer(Customer customer) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(customer);
		List<Object> customerDetailsList=new ArrayList<Object>();
		customerDetailsList.add(customer.getId());
	
		customerDetailsList.add(customer.getPassword());
		
		customerDetailsList.add(customer.getName());
		if(verify.validEmail(customer.getEmail())) {
			customerDetailsList.add(customer.getEmail());
		}
		customerDetailsList.add(customer.getDob());
		customerDetailsList.add(customer.getAddress());
		customerDetailsList.add(customer.getType());
		customerDetailsList.add(customer.getStatus());
		if(verify.validMobileNumber(customer.getContactNum())) {
			customerDetailsList.add(customer.getContactNum());
		}
		ep.addUser(customerDetailsList);
		customerDetailsList=new ArrayList<>();
		customerDetailsList.add(customer.getId());
		customerDetailsList.add(customer.getAadharNum());
		customerDetailsList.add(customer.getPanNum());
		ep.addCustomer(customerDetailsList);
	}
	
	public void removeCustomer( int userId) throws InvalidInputException,SQLException{
		UtilityTasks.checkNull(userId);
		ep.removeCustomer(userId);
	}
	
	public void addEmployee(Employee employee) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(employee);
		List<Object>employeeDetailsList=new ArrayList<Object>();
		employeeDetailsList.add(employee.getId());
		//if (verify.validPassWord(employee.getPassword())) {
			employeeDetailsList.add(employee.getPassword());
		//}
		employeeDetailsList.add(employee.getName());
		if(verify.validEmail(employee.getEmail())) {
			employeeDetailsList.add(employee.getEmail());
		}
		employeeDetailsList.add(employee.getDob());
		employeeDetailsList.add(employee.getAddress());
		employeeDetailsList.add(employee.getType());
		employeeDetailsList.add(employee.getStatus());
		if(verify.validMobileNumber(employee.getContactNum())) {
			employeeDetailsList.add(employee.getContactNum());
		}
		ap.addUser(employeeDetailsList);
		employeeDetailsList=new ArrayList<Object>();
		employeeDetailsList.add(employee.getId());
		employeeDetailsList.add(employee.getDepartment());
		employeeDetailsList.add(employee.getBranch());
		ap.addEmployee(employeeDetailsList);
		Branch branch=ep.getBranch("BRANCH_NAME",employee.getBranch());
		branch.setNumOfActiveEmployees(branch.getNumOfActiveEmployees()+1);
		ep.addEmployeeCount(branch);
		
	}
	
	public void removeEmployee(int userId) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(userId);
		ap.removeEmployee(userId);
	}
	
	public Branch getBranchDetails(int branchId) throws InvalidInputException,SQLException{
		UtilityTasks.checkNull(branchId);
		return ep.getBranch(branchId);
		
	}
	
}
