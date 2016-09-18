package de.dhbw.ml.data;

import java.util.List;

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
	public AttributeValuePair getAttributeByName(String name){
		for (AttributeValuePair attributeValuePair : attributes) {
			if(attributeValuePair.getAttribute().equals(name)){
				return attributeValuePair;
			}
		}
		return null;	//should never happen
	}
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
