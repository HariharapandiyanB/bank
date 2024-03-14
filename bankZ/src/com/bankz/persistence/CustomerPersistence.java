package com.bankz.persistence;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.pojo.Account;
import com.bankz.pojo.Customer;
import com.bankz.pojo.Transaction;
import com.bankz.pojo.User;
import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;
public class CustomerPersistence extends UserPersistence{
	DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
	
	
	
	public List<Account> getAccountInfo(Map<String, Object> conditionMap) throws SQLException, InvalidInputException{
		Map<Integer, List<Object>>resultMap=dbTasks.fetchMultipleRecords("Accounts", conditionMap);
		
		List<Account> accountList=new ArrayList<>();
		for (Object obj:resultMap.get(conditionMap.get("CUSTOMER_ID"))) {
			accountList.add((Account)obj);
		}
		return accountList;
	}
	
	public Customer getPersonalInfo(int userId) throws SQLException, InvalidInputException{
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<Integer, Object> resultMap=dbTasks.fetchRecords("Customer", keyMap);
		return (Customer)resultMap.get(userId);
	}
	
	public void selfTransaction(Account account) throws SQLException, InvalidInputException{
		UtilityTasks.checkNull(account);
		Map<String,Object> keyMap=getMap("CUSTOMER_ID", account.getCustomerId());
		Map<String, Object> updateMap=getMap("BALANCE", account.getBalance());
		dbTasks.modifyRecord("Accounts", keyMap, updateMap);
	}
	
	
	
	public void moneyTransfer(Account senderAccount,Account receiverAccount) throws SQLException,InvalidInputException{
		selfTransaction(senderAccount);
		Map<String, Object>keyMap=getMap("CUSTOMER_ID", receiverAccount.getCustomerId());
		Map<Integer, Object> resultMap=dbTasks.fetchRecords("Accounts", keyMap);
		if(resultMap!=null) {
			selfTransaction(receiverAccount);
		}
	}
	
	public void transactionEntry(Account account,int amount,Long secondaryAccNum,String time,String description) 
																					throws SQLException,InvalidInputException{
		List<Object> recordValuesList=Arrays.asList(account.getCustomerId(),account.getType(),
													amount,account.getBalance(),
													time,account.getAccountNum(),
													secondaryAccNum,description);
		dbTasks.addRecords("Transaction", dbTasks.fetchColumnList("Transaction"), recordValuesList);
	}

	
	
	
	
	
	
	
}
	
	