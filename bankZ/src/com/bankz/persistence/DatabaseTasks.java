package com.bankz.persistence;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.bankz.pojo.Account;
import com.bankz.pojo.Branch;
import com.bankz.pojo.Customer;
import com.bankz.pojo.Employee;
import com.bankz.pojo.Transaction;
import com.bankz.pojo.User;
import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;


public class DatabaseTasks {
	private String url;
	private String username;
	private String password;
	QueryBuilder queryBuilder=new QueryBuilder();
	public DatabaseTasks() {

	}

	public DatabaseTasks(String url, String username, String password) {
		this.url = url;
		this.password = password;
		this.username = username;
	}

	public Map<Integer, Object>fetchRecords(String tableName,Map<String, Object> keyMap)throws InvalidInputException,SQLException{
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(keyMap);
		try(Connection connection=DriverManager.getConnection(url,username,password)) {
			String query=queryBuilder.fetchQuery(tableName, keyMap);
			try(PreparedStatement statement=connection.prepareStatement(query);
					ResultSet rs= statement.executeQuery();){
				Map<Integer, Object> resultMap=new HashMap<Integer, Object>();
				switch(tableName) {
				case "User":
					resultMap=fetchUserData(rs);
					break;
				case "Customer":
					resultMap=fetchCustomerData(rs);
					break;
				case "Employee":
					resultMap=fetchEmployeeData(rs);
					break;
				
				case "Branch":
					resultMap=fetchBranchData(rs);
					break;
				default:
					resultMap=null;
				}
			return resultMap;
			}
	}
	}
	
	public Map<Integer, Object>fetchRecords(String tableName,String query)throws InvalidInputException,SQLException{
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(query);
		try(Connection connection=DriverManager.getConnection(url,username,password)) {
			System.out.println(query);
			try(PreparedStatement statement=connection.prepareStatement(query);
					ResultSet rs= statement.executeQuery();){
				Map<Integer, Object> resultMap=new HashMap<Integer, Object>();
				switch(tableName) {
				case "User":
					resultMap=fetchUserData(rs);
					break;
				case "Customer":
					resultMap=fetchCustomerData(rs);
					break;
				case "Employee":
					resultMap=fetchEmployeeData(rs);
					break;
				
				case "Branch":
					resultMap=fetchBranchData(rs);
					break;
				default:
					resultMap=null;
				}
			return resultMap;
			}
	}
	}
	
	public  Map<Integer, List<Object>>fetchMultipleRecords(String tableName,Map<String, Object>keyMap) 
																				throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(keyMap);
		try(Connection connection=DriverManager.getConnection(url,username,password)){
			String query=queryBuilder.fetchQuery(tableName, keyMap);
			try(PreparedStatement statement=connection.prepareStatement(query.toString());
					ResultSet rs= statement.executeQuery();){
				Map<Integer, List<Object>> resultMap=new HashMap<Integer, List<Object>>();
				switch(tableName) {
				case "Transaction":
					  resultMap=fetchTransactionData(rs);
				case "Accounts":
					resultMap=fetchAccountData(rs);
				}return resultMap;
		}
		}
	}
	
	public Map<Integer, Object> fetchSpecificRecordValues(String tableName,List<String> columnList,Map<String, Object> conditionMap) throws SQLException,InvalidInputException {
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(conditionMap);
		try(Connection connection=DriverManager.getConnection(url,username,password)){
			String query=queryBuilder.fetchSpecificValuesQuery(tableName, conditionMap, columnList);
			try(PreparedStatement statement=connection.prepareStatement(query.toString());
					ResultSet rs=statement.executeQuery()){
				Map<Integer, Object> resultMap=new HashMap<Integer, Object>();
				switch(tableName) {
				case "User":
					resultMap=fetchUserData(rs);
					break;
				case "Customer":
					resultMap=fetchCustomerData(rs);
					break;
				case "Employee":
					resultMap=fetchEmployeeData(rs);
					break;
				
				case "Branch":
					resultMap=fetchBranchData(rs);
					break;
				
				default:
					resultMap=null;
				}
				
			return resultMap;
			}
		}
	}
	
	public void deleteRecords(String tableName,Map<String, Object> conditionMap) throws SQLException,InvalidInputException {
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(conditionMap);
		try(Connection connection=DriverManager.getConnection(url,username,password);
				Statement statement=connection.createStatement()){
			String query=queryBuilder.deleteQuery(tableName, conditionMap);
			statement.executeUpdate(query.toString());
		}
	}
	
	public void addRecords(String tableName, List<String> fieldsList, List<Object> valuesList)
			throws SQLException, InvalidInputException {
		UtilityTasks.checkNull(fieldsList);
		UtilityTasks.checkNull(valuesList);
		try (Connection connection = DriverManager.getConnection(url, username, password);
				Statement statement = connection.createStatement()) {
			StringBuilder fields = new StringBuilder();
			for (String field : fieldsList) {
				fields.append(field + ",");
			}
			String fieldString=fields.toString().substring(0, fields.length()-1);
			System.out.println(fieldString);
			StringBuilder recordValues = new StringBuilder();
			for (Object recordValue : valuesList) {
				if (recordValue instanceof String) {
					recordValues.append("\'" + recordValue + "\',");
				} else {
					recordValues.append(recordValue+",");
				}
			}String recordString=recordValues.toString().substring(0, recordValues.length()-1);
			String query = "INSERT INTO " + tableName + " (" + fieldString + ") VALUES(" + recordString
					+ ");";
			
			System.out.println(query);
			statement.executeUpdate(query);

		}
	}

	public void modifyRecord(String tableName,Map<String, Object> keyMap,Map<String, Object>updateMap) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(tableName);
		UtilityTasks.checkNull(keyMap);
		UtilityTasks.checkNull(updateMap);
		try(Connection connection=DriverManager.getConnection(url,username,password);
				Statement statement=connection.createStatement()){
			String query=queryBuilder.modifyQuery(tableName, updateMap, keyMap);
		statement.executeUpdate(query.toString());
		}
	}
	
	public List<String> fetchColumnList(String tableName) throws SQLException,InvalidInputException{
		List<String> columnList=new ArrayList<String>();
		String query="SHOW COLUMNS FROM "+tableName;
		try(Connection connection=DriverManager.getConnection(url,username,password);
				Statement statement=connection.createStatement()){
			try(ResultSet rs= statement.executeQuery(query)){
				while(rs.next()) {
					columnList.add(rs.getString("Field"));
				}
			}
		}return columnList;
	}
	
	private Map<Integer, Object> fetchUserData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);

		Map<Integer, Object> userMap = new HashMap<Integer, Object>();
		for (Map<String, Object> recordMap : list) {
			User user = new User();
			user.setId((int) recordMap.get("USER_ID"));
			user.setPassword((String) recordMap.get("PASSWORD"));
			user.setName((String) recordMap.get("FULL_NAME"));
			user.setEmail((String) recordMap.get("EMAIL"));
			user.setContactNum((long) recordMap.get("CONTACT_NUM"));
			user.setDob((long) recordMap.get("DOB"));
			user.setAddress((String) recordMap.get("ADDRESS"));
			user.setType((String) recordMap.get("USER_TYPE"));
			user.setStatus((String) recordMap.get("STATUS"));
			userMap.put(user.getId(), user);
		}
		return userMap;
	}

	private Map<Integer, Object> fetchEmployeeData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);

		Map<Integer,Object> employeeMap = new HashMap<Integer, Object>();
		for (Map<String, Object> recordMap : list) {
			Employee employee = new Employee();
			employee.setId((int) recordMap.get("USER_ID"));
			employee.setPassword((String) recordMap.get("PASSWORD"));
			employee.setName((String) recordMap.get("FULL_NAME"));
			employee.setEmail((String) recordMap.get("EMAIL"));
			employee.setContactNum((long) recordMap.get("CONTACT_NUM"));
			employee.setDob((long) recordMap.get("DOB"));
			employee.setAddress((String) recordMap.get("ADDRESS"));
			employee.setType((String) recordMap.get("USER_TYPE"));
			employee.setStatus((String) recordMap.get("STATUS"));
			employee.setDepartment((String) recordMap.get("DEPARTMENT"));
			employee.setBranch((String) recordMap.get("BRANCH"));
			employeeMap.put(employee.getId(), employee);
		}
		return employeeMap;

	}

	private Map<Integer, Object> fetchCustomerData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);

		Map<Integer, Object> customerMap = new HashMap<Integer,Object>();
		for (Map<String, Object> recordMap : list) {
			Customer customer = new Customer();
			customer.setId((int) recordMap.get("USER_ID"));
			customer.setPassword((String) recordMap.get("PASSWORD"));
			customer.setName((String) recordMap.get("FULL_NAME"));
			customer.setEmail((String) recordMap.get("EMAIL"));
			customer.setContactNum((long) recordMap.get("CONTACT_NUM"));
			customer.setDob((long) recordMap.get("DOB"));
			customer.setAddress((String) recordMap.get("ADDRESS"));
			customer.setType((String) recordMap.get("USER_TYPE"));
			customer.setStatus((String) recordMap.get("STATUS"));
			customer.setAadharNum((long) recordMap.get("AADHAR_NUM"));
			customer.setPanNum((String) recordMap.get("PAN_NUM"));
			customerMap.put(customer.getId(), customer);
		}
		return customerMap;
	}

	@SuppressWarnings("unchecked")
	private  Map<Integer, List<Object>> fetchTransactionData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);

		List<Object> transactionList = new ArrayList<>();
		Map<Integer, List<Object>> transactionMap = new HashMap<Integer, List<Object>>();
		for (Map<String, Object> recordMap : list) {
			Transaction transaction = new Transaction();
			transaction.setTransactionId((int) recordMap.get("TRANSACTION_ID"));
			transaction.setCustomerId((int) recordMap.get("CUSTOMER_ID"));
			transaction.setTranscationType((String) recordMap.get("TYPE"));
			transaction.setTranscationAmount((int) recordMap.get("AMOUNT"));
			transaction.setBalance((int) recordMap.get("BALANCE"));
			transaction.setTimeStamp((LocalDateTime) recordMap.get("TIME"));
			transaction.setPrimaryAccNum((long) recordMap.get("PRIMARY_ACCOUNT_NUMBER"));
			transaction.setSecondaryAccNum((long) recordMap.get("SECONDARY_ACCOUNT_NUMBER"));
			transaction.setDescription((String) recordMap.get("DESCRIPTION"));

			int num = transaction.getCustomerId();
			if (transactionMap.containsKey(num)) {
				transactionList = transactionMap.get(num);
				transactionList.add(transaction);
			} else {
				transactionList = new ArrayList<>();
				transactionList.add(transaction);
				transactionMap.put(num, transactionList);
			}
		}
		return transactionMap;
	}

	@SuppressWarnings("unchecked")
	private   Map<Integer, List<Object>> fetchAccountData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);
		List<Object> accountList=new ArrayList<>();
		Map<Integer, List<Object>> accountMap = new HashMap<Integer, List<Object>>();
		for (Map<String, Object> recordMap : list) {
			Account account = new Account();
			account.setAccountNum((long) recordMap.get("ACCOUNT_NUMBER"));
			account.setCustomerId((int) recordMap.get("CUSTOMER_ID"));
			account.setType((String) recordMap.get("TYPE"));
			account.setBalance((int) recordMap.get("BALANCE"));
			account.setStatus((String) recordMap.get("STATUS"));
			account.setBranchId((int) recordMap.get("BRANCH_ID"));
			
			int num=account.getCustomerId();
			if(accountMap.containsKey(num)) {
				accountList=accountMap.get(num);
				accountList.add(account);
			}else {
				accountList=new ArrayList<>();
				accountList.add(account);
				accountMap.put(num,accountList);
			}
			
		}
		return accountMap;
	}

	private Map<Integer, Object> fetchBranchData(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = fetchDataFromResultSet(rs);
		Map<Integer,Object> branchMap = new HashMap<Integer,Object>();
		for (Map<String, Object> recordMap : list) {
			Branch branch = new Branch();
			branch.setBranchId((int) recordMap.get("BRANCH_ID"));
			branch.setName((String) recordMap.get("BRANCH_NAME"));
			branch.setiFSCcode((String) recordMap.get("IFSC_CODE"));
			branch.setAddress((String) recordMap.get("ADDRESS"));
			branch.setNumOfActiveCustomers((int) recordMap.get("NUM_OF_ACTIVE_CUSTOMERS"));
			branch.setNumOfActiveEmployees((int) recordMap.get("NUM_OF_ACTIVE_EMPLOYEES"));
			branchMap.put(branch.getBranchId(), branch);
		}
		return branchMap;
	}

	private List<Map<String, Object>> fetchDataFromResultSet(ResultSet rs) throws SQLException {
		int columnLen = rs.getMetaData().getColumnCount();
		String[] columns = new String[columnLen];
		for (int i = 1; i <= columnLen; i++) {
			columns[i - 1] = rs.getMetaData().getColumnLabel(i);
		}
		List<Map<String, Object>> list = new ArrayList<>();
		while (rs.next()) {
			Map<String, Object> resultMap = new HashMap<>();
			for (int i = 1; i <= columnLen; i++) {
				resultMap.put(columns[i - 1], rs.getObject(i));
			}
			list.add(resultMap);
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
