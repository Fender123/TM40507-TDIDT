package de.dhbw.ml.data;

import java.io.BufferedReader;
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
		
		Character seperator = guessSeparator(sourceFile);
		
		FileReader reader = new FileReader(sourceFile);
		
		CSVReader csvReader = new CSVReader(reader, seperator);
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
		
		csvReader.close();
	}

	protected Character guessSeparator(File file) throws IOException {
		//guess separator (either , or ;)
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = bufferedReader.readLine();
		Character separator = ';';
		if(line != null){
			int countColon = line.length() - line.replace(",", "").length();
			int countSemiColon = line.length() - line.replace(";", "").length();
			if(countColon > countSemiColon){
				separator = ',';
			}
		}
		bufferedReader.close();
		return separator;
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

	public DataSet[] split(int num) {
		DataSet[] sets = new DataSet[2];
		int size = items.size() / num;
		for(int i = 0; i < num; i++){
			sets[i] = new DataSet();
			sets[i].setItems(items.subList(i * size, (i + 1) * size));
			sets[i].setNumberOfExamples(size);
			int numPos = 0;
			int numNeg = 0;
			List<DataItem> sItems = sets[i].getItems();
			for(int j = 0; j < sItems.size(); j++){
				if(sItems.get(j).teacher){
					numPos++;
				}else{
					numNeg++;
				}
			}
			sets[i].setNumberOfPositives(numPos);
			sets[i].setNumberOfNegatives(numNeg);
		}
		return sets;
	}

	public List<DataItem> getItemsWithAttribute(String name, String attribValue) {
		List<DataItem> filteredItems = new ArrayList<>();
		for (DataItem di : items) {
			for (AttributeValuePair avp : di.getAttributes()) {
				if(avp.getAttribute().equals(name)){
					if(avp.getValue().equals(attribValue)){
						filteredItems.add(di);
					}
					break;
				}
			}
		}
		return filteredItems;
	}
	
	public void updateCounts(){
		numberOfExamples = items.size();
		numberOfNegatives = 0;
		numberOfPositives = 0;
		for (DataItem dataItem : items) {
			if(dataItem.getTeacher()){
				numberOfPositives++;
			}else{
				numberOfNegatives++;
			}
		}
	}
}
