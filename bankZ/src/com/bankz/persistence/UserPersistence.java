package com.bankz.persistence;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.pojo.User;
import com.bankz.utilities.InvalidInputException;

public class UserPersistence {
	DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
	public User login(int userId)throws SQLException,InvalidInputException{
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<Integer, Object>resultMap=dbTasks.fetchRecords("User", keyMap);
		return (User)resultMap.get(userId);
	}
	
	public User getPersonalInfo(int userId) throws SQLException, InvalidInputException{
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<Integer, Object> resultMap=dbTasks.fetchRecords("User", keyMap);
		return (User)resultMap.get(userId);
	}
	
	public void modifyInfo(int userId,String field,Object value) throws InvalidInputException,SQLException{
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<String, Object> updateMap=null;
		switch(field) {
		case "Name":
			updateMap=getMap("NAME", value);
			break;
		case "DOB":
			updateMap=getMap("DOB", value);
			break;
		case "Email":
			updateMap=getMap("EMAIL", value);
			break;
		case "Address":
			updateMap=getMap("ADDRESS", value);
			break;
		case "Contact Number":
			updateMap=getMap("CONTACT_NUMBER", value);
			break;
		}
		dbTasks.modifyRecord("User", keyMap, updateMap);
	}
	
	public void changePassword(String password,int userId) throws SQLException,InvalidInputException, NoSuchAlgorithmException{
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<String , Object> updateMap=getMap("PASSWORD", password);
		dbTasks.modifyRecord("User", keyMap, updateMap);
	}
	
	public String checkStatus(int userId) throws SQLException,InvalidInputException{
		List<String> columnList=new ArrayList<String>();
		columnList.add("STATUS");
		Map<String, Object> keyMap=getMap("USER_ID", userId);
		Map<Integer,Object>resultMap= dbTasks.fetchRecords("User", keyMap);
		return((User)resultMap.get(userId)).getStatus();
	}
	
	public  List<Object> getTransactionDetails(int userId) throws SQLException,InvalidInputException{
		Map<String, Object> keyMap=getMap("CUSTOMER_ID", userId);
		return dbTasks.fetchMultipleRecords("Transaction", keyMap).get(userId);
		
	}
	
	protected Map<String, Object> getMap(String key,Object value){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put(key, value);
		return map;
	}
	
}
