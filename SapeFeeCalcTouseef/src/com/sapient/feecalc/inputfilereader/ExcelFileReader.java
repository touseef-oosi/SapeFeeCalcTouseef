package com.sapient.feecalc.inputfilereader;

public class ExcelFileReader implements InputFileReader {

	String filePath;
	
	public ExcelFileReader(String inputFilePath) {
		this.filePath = inputFilePath;
	}

	@Override
	public void readFiles() {
		// TODO Auto-generated method stub
		System.out.println("NOT IMPLEMENTED as of now");
	}

}
