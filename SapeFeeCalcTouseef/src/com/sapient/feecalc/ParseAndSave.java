package com.sapient.feecalc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParseAndSave {

	private String[] arrayElements;
	private String transId;
	private String clientId;
	private String secId;
	private String type;
	private Date date;
	private double value;
	private boolean priority;
	private boolean intraday = false;
	private SimpleDateFormat sdf = new SimpleDateFormat(Constants.SDF_PATTERN);
	private Transaction trans = new Transaction();

	public ParseAndSave(String[] arrayElements) {
		this.arrayElements = arrayElements;
	}

	public void parse() {
		try {
			transId = arrayElements[0];
			clientId = arrayElements[1];
			secId = arrayElements[2];
			type = arrayElements[3];
			date = sdf.parse(arrayElements[4]);
			value = Double.parseDouble(arrayElements[5]);

			String priorityInString = arrayElements[6];
			if (priorityInString.equalsIgnoreCase(Constants.PRIORITY_YES_FLAG)) {
				priority = true;
			} else if (priorityInString.equalsIgnoreCase(Constants.PRIORITY_NO_FLAG)) {
				priority = false;
			} else {
				System.out.println("Other unsupported priority. Default FALSE will be set");
			}
			saveData();
		} catch (ParseException e) {
			System.out.println("The input date is not in format of dd/MM/yyyy" + arrayElements[4]);
		} catch (Exception ie) {
			System.out.println("General Exception while reading line." + arrayElements);
		}
	}

	private void saveData() {
		trans.setExternalTransactionId(transId);
		trans.setClientId(clientId);
		trans.setSecurityId(secId);
		trans.setTransactionType(type);
		trans.setTransactionDate(sdf.format(date));
		trans.setMarketValue(value);
		if (priority) {
			trans.setPriority(Constants.PRIORITY_YES_FLAG);
		} else {
			trans.setPriority(Constants.PRIORITY_NO_FLAG);
		}
		
		intraday = isIntraday();

		calculateFee();

		if (CalculateFees.feeData.containsKey(clientId)) {
			List<Transaction> tempList = CalculateFees.feeData.get(clientId);
			tempList.add(trans);
			CalculateFees.feeData.put(clientId, tempList);
		} else {
			List<Transaction> tempList = new ArrayList<Transaction>();
			tempList.add(trans);
			CalculateFees.feeData.put(clientId, tempList);
		}
	}

	private void calculateFee() {
		if (intraday) {
			trans.setTransactionFee(Constants.INTRADAY_FEE);
		} else if (priority) {
			trans.setTransactionFee(Constants.HIGH_PRIORITY_FEE);
		} else if (type.equalsIgnoreCase(Constants.SELL) || type.equalsIgnoreCase(Constants.WITHDRAW)) {
			trans.setTransactionFee(Constants.NORMAL_SELL_WITHDRAW_FEE);
		} else if (type.equalsIgnoreCase(Constants.BUY) || type.equalsIgnoreCase(Constants.DEPOSIT)) {
			trans.setTransactionFee(Constants.NORMAL_BUY_DEPOSIT_FEE);
		} else {
			System.out.println("ERROR : Unsupported Transaction Type. " + type);
		}
	}

	private boolean isIntraday() {
		if (CalculateFees.feeData.containsKey(clientId)) {
			List<Transaction> checkList = CalculateFees.feeData.get(clientId);
			for (Transaction tran : checkList) {
				if (tran.getTransactionDate().equals(date.toString())) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

}
