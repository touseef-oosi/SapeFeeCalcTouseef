package com.sapient.feecalc;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sapient.feecalc.inputfilereader.CSVFileReader;
import com.sapient.feecalc.inputfilereader.ExcelFileReader;
import com.sapient.feecalc.inputfilereader.InputFileReader;
import com.sapient.feecalc.inputfilereader.TextFileReader;
import com.sapient.feecalc.inputfilereader.XMLFileReader;

public class CalculateFees {

	// To maintain sorted order, used TreeMap.
	public static Map<String, List<Transaction>> feeData = new TreeMap<String, List<Transaction>>();

	public static void main(String args[]) {
		System.out.println("Program to calculate transaction fee.");
		readInputFiles();
		displayTransaction();
	}

	private static void readInputFiles() {
		String inputFilePath = "C:\\Users\\ItzMe\\Desktop\\Sapient\\input PS_FeeCalculator.csv";
		String fileExtension = getInputFileExtension(inputFilePath);

		if (fileExtension == null) {
			System.out.println("Input file is not one of known extension or doesn't have extension");
			// Note : We can take appropriate action. I am terminating the
			// program for time constrain.
			System.exit(1);
		}

		InputFileReader fileReader = null;
		if (fileExtension.equalsIgnoreCase(Constants.CSV_FORMAT)) {
			fileReader = new CSVFileReader(inputFilePath);
		} else if (fileExtension.equalsIgnoreCase(Constants.TEXT_FORMAT)) {
			fileReader = new TextFileReader(inputFilePath);
		} else if (fileExtension.equalsIgnoreCase(Constants.XML_FORMAT)) {
			fileReader = new XMLFileReader(inputFilePath);
		} else if (fileExtension.equalsIgnoreCase(Constants.EXCEL_FORMAT)) {
			fileReader = new ExcelFileReader(inputFilePath);
		} else {
			System.out.println("This type is not handled yet. We will handle it soon. ");
			// We can take appropriate action. I am terminating here.
			System.exit(2);
		}

		fileReader.readFiles();
	}

	private static String getInputFileExtension(String inputFilePath) {
		int fileExtensionSeperatorIndex = inputFilePath.lastIndexOf(Constants.FILE_EXTENSION_SEPERATOR);
		if (fileExtensionSeperatorIndex != -1 && fileExtensionSeperatorIndex != 0) {
			return inputFilePath.substring(fileExtensionSeperatorIndex + 1);
		}
		return null;
	}

	/*
	 * We can use many way to display. We can group all the clientId and show sum of Fee applicable. 
	 * However, I just printed as mentioned in Output.csv given. 
	 */
	private static void displayTransaction() {
		System.out.println("Client Id,Transaction Type,Transaction Date,Priority,Processing Fee");
		for (Map.Entry<String, List<Transaction>> eachEntry : feeData.entrySet()) {
			String clientId = eachEntry.getKey();
			for (Transaction eachList : eachEntry.getValue()) {
				System.out.println(clientId + Constants.CSV_SEPERATOR
						+ eachList.getTransactionType() + Constants.CSV_SEPERATOR
						+ eachList.getTransactionDate().toString() + Constants.CSV_SEPERATOR
						+ eachList.isPriority() + Constants.CSV_SEPERATOR
						+ eachList.getTransactionFee());
			}
		}
	}

}
