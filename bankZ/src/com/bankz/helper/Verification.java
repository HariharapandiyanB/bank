package com.bankz.helper;


import java.util.regex.Pattern;

import com.bankz.utilities.InvalidInputException;
import com.bankz.utilities.UtilityTasks;

public class Verification {
	/*public boolean validPassWord(String string) throws InvalidInputException{
		UtilityTasks.checkNull(string);
		if(Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\p{Punct}).{8,}$", string)){
			return  true;
		}else {
			throw new InvalidInputException("Invalid Password");
		}
	}*/
	public boolean validEmail(String email) throws InvalidInputException{
		UtilityTasks.checkNull(email);
		if(Pattern.matches("^.+[@].+[.].+$", email)) {
			return true;
		}else {
			throw new InvalidInputException("Invalid EmailId..");		}
	}
	
	public boolean validMobileNumber(long number) throws InvalidInputException{
		UtilityTasks.checkNull(number);
		String mobileNumber=String.valueOf(number);
		if( Pattern.matches("^[^0-5]\\d{9}$", mobileNumber)) {
			return true ;
		}else {
			throw new InvalidInputException("Invalid contact Number...");		}
	}
}
