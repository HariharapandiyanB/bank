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
import com.bankz.utilities.BankException;
import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;

public class CustomerPersistence extends UserPersistence{
	DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
	
	
	
	public List<Account> getAccountInfo(int userId,int pageNum) throws SQLException, InvalidInputException{
		Map<String, Object> conditionMap=new HashMap<String, Object>();
		conditionMap.put(dbTasks.fetchColumnList("Customer").get(0), userId);
		Map<Integer, List<Object>>resultMap=dbTasks.fetchMultipleRecords("Accounts", conditionMap,pageNum,5);
		List<Account> accountList=new ArrayList<>();
		for (Object obj:resultMap.get(conditionMap.get(dbTasks.fetchColumnList("Customer").get(0)))) {
			accountList.add((Account)obj);
		}
		return accountList;
	}
	
	public Account getSingleAccountInfo(long accountNum) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(accountNum);
		Map<String,Object> keyMap=null;
		keyMap=getMap(dbTasks.fetchColumnList("Accounts").get(0), accountNum);
		return dbTasks.fetchSingleAccount("Accounts",keyMap);
	}
	
	public Customer getPersonalInfo(int userId) throws SQLException, InvalidInputException{
		UtilityTasks.checkNull(userId);
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<Integer, Object> resultMap=dbTasks.fetchRecords("Customer", keyMap);
		return (Customer)resultMap.get(userId);
	}
	
	public void selfTransaction(Account account) throws SQLException, InvalidInputException{
		UtilityTasks.checkNull(account);
		Map<String,Object> keyMap=getMap(dbTasks.fetchColumnList("Accounts").get(1), account.getCustomerId());
		Map<String, Object> updateMap=getMap(dbTasks.fetchColumnList("Accounts").get(3), account.getBalance());
		dbTasks.modifyRecord("Accounts", keyMap, updateMap);
	}
	
	public void transactionEntry(Account account,int amount,Long secondaryAccNum,long time,String description) 
																					throws SQLException,InvalidInputException{
		List<Object> recordValuesList=Arrays.asList(null,account.getCustomerId(),account.getType(),
													amount,account.getBalance(),
													time,account.getAccountNum(),
													secondaryAccNum,description);
		dbTasks.addRecords("Transaction", dbTasks.fetchColumnList("Transaction"), recordValuesList);
	}

	
	
	
	
	
	
	
}
	
	