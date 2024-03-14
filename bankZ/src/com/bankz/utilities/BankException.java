package com.bankz.utilities;

public class BankException extends Exception{
	private static final long serialVersionUID = 1L;

	public BankException(String s){
		System.out.println(s);
	}
}
