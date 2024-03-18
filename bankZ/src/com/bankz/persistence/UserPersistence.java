package com.bankz.persistence;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.enums.UserStatus;
import com.bankz.pojo.User;
import com.bankz.utilities.InvalidInputException;

public class UserPersistence {
	DatabaseTasks dbTasks=new DatabaseTasks("jdbc:mysql://localhost:3306/bankZ","root","hari03@mySql");
	public User login(int userId)throws SQLException,InvalidInputException{
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<Integer, Object>resultMap=dbTasks.fetchRecords("User", keyMap);
		return (User)resultMap.get(userId);
	}
	
	public User getPersonalInfo(int userId) throws SQLException, InvalidInputException{
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<Integer, Object> resultMap=dbTasks.fetchRecords("User", keyMap);
		return (User)resultMap.get(userId);
	}
	
	public void modifyInfo(int userId,String field,Object value) throws InvalidInputException,SQLException{
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<String, Object> updateMap=null;
		switch(field) {
		case "Name":
			updateMap=getMap(dbTasks.fetchColumnList("User").get(2), value);
			break;
		case "DOB":
			updateMap=getMap(dbTasks.fetchColumnList("User").get(4), value);
			break;
		case "Email":
			updateMap=getMap(dbTasks.fetchColumnList("User").get(3), value);
			break;
		case "Address":
			updateMap=getMap(dbTasks.fetchColumnList("User").get(5), value);
			break;
		case "Contact Number":
			updateMap=getMap(dbTasks.fetchColumnList("User").get(8), value);
			break;
		}
		dbTasks.modifyRecord("User", keyMap, updateMap);
	}
	
	public void changePassword(String password,int userId) throws SQLException,InvalidInputException, NoSuchAlgorithmException{
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<String , Object> updateMap=getMap(dbTasks.fetchColumnList("User").get(1), password);
		dbTasks.modifyRecord("User", keyMap, updateMap);
	}
	
	public int checkStatus(int userId) throws SQLException,InvalidInputException{
		
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("User").get(0), userId);
		Map<Integer,Object>resultMap= dbTasks.fetchRecords("User", keyMap);
		return((User)resultMap.get(userId)).getStatus();
	}
	
	public  List<Object> getTransactionDetails(int userId,int pageNum) throws SQLException,InvalidInputException{
		Map<String, Object> keyMap=getMap(dbTasks.fetchColumnList("Transaction").get(1), userId);
		return dbTasks.fetchMultipleRecords("Transaction", keyMap,pageNum,5).get(userId);
		
	}
	
	protected Map<String, Object> getMap(String key,Object value){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put(key, value);
		return map;
	}
	
}
