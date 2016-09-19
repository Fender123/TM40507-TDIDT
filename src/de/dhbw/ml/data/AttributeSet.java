package de.dhbw.ml.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AttributeSet implements Cloneable {
	protected int numberOfAttributes;
	protected List<Attribute> attributes;
	
	public AttributeSet() {
		attributes = new ArrayList<>();
	}
	
	public AttributeSet(File attribFile) throws Exception{
		this();
		
		try (BufferedReader br = new BufferedReader(new FileReader(attribFile))) {
		    String line = br.readLine();
		    if(line == null){
		    	throw new Exception("Invalid file provided (file is empty)");
		    }
		    numberOfAttributes = Integer.parseInt(line);
		    for(int i = 0; i < numberOfAttributes; i++){
		    	line = br.readLine();
		    	if(line == null){
			    	throw new Exception("Invalid file provided (expecting name of attribute)");		    		
		    	}
		    	Attribute a = new Attribute();
		    	a.setName(line.trim());
		    	line = br.readLine();
		    	if(line == null){
			    	throw new Exception("Invalid file provided (expecting number of attributes)");		    		
		    	}
		    	a.setNumberOfValues(Integer.parseInt(line));
		    	List<String> values = new ArrayList<>();
		    	for(int j = 0; j < a.getNumberOfValues(); j++){
		    		line = br.readLine();
			    	if(line == null){
				    	throw new Exception("Invalid file provided (expecting value of attribute)");		    		
			    	}
		    		values.add(line.trim());
		    	}
		    	a.setValues((String[]) values.toArray(new String[values.size()]));
		    	attributes.add(a);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}
	public void setNumberOfAttributes(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public AttributeSet clone() throws CloneNotSupportedException {
		AttributeSet as = new AttributeSet();
		as.setNumberOfAttributes(getNumberOfAttributes());
		@SuppressWarnings("unchecked")
		ArrayList<Attribute> clonedAttributes = new ArrayList<>();
		for (Attribute attribute : getAttributes()) {
			clonedAttributes.add(attribute);
		}
		as.setAttributes(clonedAttributes);
		return as;
	}
	
}
