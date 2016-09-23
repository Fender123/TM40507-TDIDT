package de.dhbw.ml.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Datenstruktur für alle vorhanden Attribute, deren möglichen Werte sowie die entsprechenden Anzahlen deren Vorkommen
 * 
 * @author Michael
 *
 */
public class AttributeSet implements Cloneable {
	protected int numberOfAttributes;
	protected List<Attribute> attributes;
	
	public AttributeSet() {
		attributes = new ArrayList<>();
	}
	
	public AttributeSet(File attribFile) throws Exception{
		this();
		//Zeilenweise auslesen
		try (BufferedReader br = new BufferedReader(new FileReader(attribFile))) {
		    String line = br.readLine();
		    if(line == null){
		    	throw new Exception("Invalid file provided (file is empty)");
		    }
		    //erste Zeile: Anzahl der Attribute
		    numberOfAttributes = Integer.parseInt(line);
		    //Alle x Attribute durchlaufen
		    for(int i = 0; i < numberOfAttributes; i++){
		    	line = br.readLine();
		    	if(line == null){
			    	throw new Exception("Invalid file provided (expecting name of attribute)");		    		
		    	}
		    	//Zeile: Attributname
		    	Attribute a = new Attribute();
		    	a.setName(line.trim());
		    	line = br.readLine();
		    	if(line == null){
			    	throw new Exception("Invalid file provided (expecting number of attributes)");		    		
		    	}
		    	//Zeile: Anzahl der möglichen Attributwerte
		    	a.setNumberOfValues(Integer.parseInt(line));
		    	List<String> values = new ArrayList<>();
		    	//Alle y Attributwerte durchlaufen
		    	for(int j = 0; j < a.getNumberOfValues(); j++){
		    		line = br.readLine();
			    	if(line == null){
				    	throw new Exception("Invalid file provided (expecting value of attribute)");		    		
			    	}
			    	//Zeile: Attributwert
		    		values.add(line.trim());
		    	}
		    	//Werte als String Array speichern
		    	a.setValues((String[]) values.toArray(new String[values.size()]));
		    	attributes.add(a);
		    }
		} catch (IOException e) {
			//IO Fehler abfangen
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
	
	/**
	 * Erstellt einen Klon dieser Datenstruktur wobei alle Elemente kopiert werden, so dass der Klon und die Originale Datenstruktur keine gemeinsamen Referenzen verwenden
	 */
	@Override
	public AttributeSet clone() throws CloneNotSupportedException {
		AttributeSet as = new AttributeSet();
		as.setNumberOfAttributes(getNumberOfAttributes());
		ArrayList<Attribute> clonedAttributes = new ArrayList<>();
		for (Attribute attribute : getAttributes()) {
			clonedAttributes.add(attribute);
		}
		as.setAttributes(clonedAttributes);
		return as;
	}
	
	/**
	 * String Repräsentation der Datenstruktur
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Attribute attribute : attributes) {
			sb.append("\t(");
			sb.append(attribute.toString());
			sb.append(")\n");
		}
		return String.format("AttributeSet (%d):\n%s", getNumberOfAttributes(), sb.toString());
	}
	
}
