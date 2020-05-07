package com.sapient.feecalc;

public enum TransactionType {

	BUY, SELL, DEPOSIT, WITHDRAW;
	
	public String getString(){
		return BUY.toString();
	}
}
