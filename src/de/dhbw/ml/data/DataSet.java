package de.dhbw.ml.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class DataSet {
	protected int numberOfExamples = 0;
	protected int numberOfPositives = 0;
	protected int numberOfNegatives = 0;
	protected List<DataItem> items;
	
	public DataSet(){
		items = new ArrayList<>();
	}
	
	public DataSet(File sourceFile) throws IOException {
		this();
		
		CSVReader csvReader = new CSVReader(new FileReader(sourceFile), ';');
		String[] line;
		Boolean firstLine = true;
		String[] headlines = null;
		while((line = csvReader.readNext())	!= null){
			if(firstLine){
				firstLine = false;
				headlines = line.clone();
				continue;
			}
			
			DataItem di = new DataItem();
			Boolean teacher = line[line.length - 2].trim().equals("ja");
			di.setTeacher(teacher);
			List<AttributeValuePair> avps = new ArrayList<>();
			int i;
			for (i = 0; i < line.length - 2; i++) {
				AttributeValuePair avp = new AttributeValuePair();
				avp.setAttribute(headlines[i].trim());
				avp.setValue(line[i].trim());
				avps.add(avp);
			}
			di.setAttributes(avps);
			
			items.add(di);
			numberOfExamples++;
			if(teacher){
				numberOfPositives++;
			}else{
				numberOfNegatives++;
			}
		}
	}
	
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	public void setNumberOfExamples(int numberOfExamples) {
		this.numberOfExamples = numberOfExamples;
	}
	public int getNumberOfPositives() {
		return numberOfPositives;
	}
	public void setNumberOfPositives(int numberOfPositives) {
		this.numberOfPositives = numberOfPositives;
	}
	public int getNumberOfNegatives() {
		return numberOfNegatives;
	}
	public void setNumberOfNegatives(int numberOfNegatives) {
		this.numberOfNegatives = numberOfNegatives;
	}
	public List<DataItem> getItems() {
		return items;
	}
	public void setItems(List<DataItem> items) {
		this.items = items;
	}
	
	
}
