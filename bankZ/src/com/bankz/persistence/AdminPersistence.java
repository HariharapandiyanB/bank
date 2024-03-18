package com.bankz.persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;

public class AdminPersistence extends EmployeePersistence{
	public void addEmployee(List<Object> employeeDetailsList) throws SQLException,InvalidInputException{
		UtilityTasks.checkNull(employeeDetailsList);
		List<String>fieldList=dbTasks.fetchColumnList("Employee");
		dbTasks.addRecords("Employee", fieldList, employeeDetailsList);
	}
	
	public void removeEmployee(int userId) throws SQLException,InvalidInputException{
		Map<String, Object>keyMap=new HashMap<String, Object>();
		keyMap.put(dbTasks.fetchColumnList("User").get(0), userId);
		dbTasks.deleteRecords("User", keyMap);
	}
}
