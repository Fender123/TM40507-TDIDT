package de.dhbw.ml.data;

import java.util.List;

/**
 * Datensatz aus der Quell CSV Datei mit Attributen und deren Werte sowie die Entscheidung des Teachers
 * @author Michael
 *
 */
public class DataItem {
	protected boolean teacher;
	protected List<AttributeValuePair> attributes;
	
	public boolean getTeacher() {
		return teacher;
	}
	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}
	public List<AttributeValuePair> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AttributeValuePair> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * gibt das {@link AttributeValuePair} mit dem gewünschten Namen zurück
	 * @param name
	 * @return
	 */
	public AttributeValuePair getAttributeByName(String name){
		for (AttributeValuePair attributeValuePair : attributes) {
			if(attributeValuePair.getAttribute().equals(name)){
				return attributeValuePair;
			}
		}
		return null;	//should never happen
	}
	
	/**
	 * String Repräsentation des Datensatzes 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DataItem: ");
		for (AttributeValuePair attributeValuePair : attributes) {
			sb.append("(");
			sb.append(attributeValuePair.toString());
			sb.append(") ");
		}
		sb.append("Class: ");
		sb.append(teacher ? "1" : "0");
		
		return sb.toString();
	}
}
