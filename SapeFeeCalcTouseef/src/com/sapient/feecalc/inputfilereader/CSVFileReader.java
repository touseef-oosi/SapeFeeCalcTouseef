package com.sapient.feecalc.inputfilereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.sapient.feecalc.Constants;
import com.sapient.feecalc.ParseAndSave;

public class CSVFileReader implements InputFileReader{

	String filePath;
	boolean isHeader = true;
	
	public CSVFileReader(String inputFilePath) {
		this.filePath = inputFilePath;
	}

	@Override
	public void readFiles() {
		File inputFile = new File(filePath);
		try {
			// We could use batch file processing as well. Just keeping as given. 
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String eachLine;
			while ((eachLine = br.readLine()) != null){
				// We can use Threads to process in parallel. Just not going there. 
				if(!isHeader) {
					String[] arrayElements = eachLine.split(Constants.CSV_SEPERATOR);
					ParseAndSave ps = new ParseAndSave(arrayElements);
					ps.parse();
				} else {
					isHeader = false;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found. " +e);
		} catch (IOException e1) {
			System.out.println("IOException occurred. " +e1);
		}
	}

}
