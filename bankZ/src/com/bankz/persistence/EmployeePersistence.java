package com.bankz.persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.pojo.Branch;

import com.bankz.pojo.Employee;
import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;


public class EmployeePersistence extends UserPersistence{
		DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
		QueryBuilder queryBuilder=new QueryBuilder();
		
		public Employee getPersonalInfo(int userId) throws SQLException, InvalidInputException{
			Map<String, Object> keyMap=getMap("USER_ID", userId);
			Map<Integer, Object> resultMap=dbTasks.fetchRecords("Employee", keyMap);
			return (Employee)resultMap.get(userId);
		}
		
		public void addAccount(List<Object> accountDetailsList) throws SQLException,InvalidInputException{
			UtilityTasks.checkNull(accountDetailsList);
			List<String> fieldList=dbTasks.fetchColumnList("Accounts");
			dbTasks.addRecords("Accounts", fieldList, accountDetailsList);
			
		}
		
		public void removeAccount(long accountNum) throws SQLException,InvalidInputException{
			Map<String, Object> keyMap=getMap("ACCOUNT_NUMBER", accountNum);
			dbTasks.deleteRecords("Accounts", keyMap);
		}
		
		public void blockAccount(long accountNum) throws SQLException,InvalidInputException{
			Map<String, Object> keyMap=getMap("ACCOUNT_NUMBER", accountNum);
			Map<String, Object> updateMap=getMap("STATUS", "INACTIVE");
			dbTasks.modifyRecord("Accounts", keyMap, updateMap);
		}
		
		public void  addUser(List<Object> userDetailsList) throws SQLException,InvalidInputException {
			UtilityTasks.checkNull(userDetailsList);
			List<String>fieldList=dbTasks.fetchColumnList("User");
			dbTasks.addRecords("User", fieldList, userDetailsList);
		}
		
		public void  addCustomer(List<Object> customerDetailsList) throws SQLException,InvalidInputException {
			UtilityTasks.checkNull(customerDetailsList);
			List<String>fieldList=dbTasks.fetchColumnList("Customer");
			dbTasks.addRecords("Customer", fieldList, customerDetailsList);
		}
		
		public void removeCustomer(int userId) throws SQLException,InvalidInputException {
			Map<String, Object> keyMap=getMap("CUSTOMER_ID", userId);
			dbTasks.deleteRecords("Customer", keyMap);
		}
		
		public Branch getBranch(int branchId)throws SQLException,InvalidInputException{
			Map<String,Object>keyMap= getMap("BRANCH_ID", branchId);
			return (Branch)dbTasks.fetchRecords("Branch", keyMap).get(branchId);
		}
		
		public Branch getBranch(Object...objects) throws SQLException,InvalidInputException{
			UtilityTasks.checkNull(objects);
			String query=queryBuilder.demoQuery("Branch", objects);
			return  (Branch)dbTasks.fetchRecords("Branch",query).get(objects[1]);
		}
		
		public void addAccountCount(Branch branch)throws SQLException,InvalidInputException{
			UtilityTasks.checkNull(branch);
			Map<String, Object>keyMap=getMap("BRANCH_ID", branch.getBranchId());
			Map<String, Object>updateMap=getMap("NUM_OF_ACTIVE_CUSTOMERS", branch.getNumOfActiveCustomers());
			dbTasks.modifyRecord("Branch", keyMap, updateMap);
		}
		
		public void addEmployeeCount(Branch branch)throws SQLException,InvalidInputException{
			UtilityTasks.checkNull(branch);
			Map<String, Object>keyMap=getMap("BRANCH_ID", branch.getBranchId());
			Map<String, Object>updateMap=getMap("NUM_OF_ACTIVE_EMPLOYEES", branch.getNumOfActiveEmployees());
			dbTasks.modifyRecord("Branch", keyMap, updateMap);
		}
		
		
		
		
}
