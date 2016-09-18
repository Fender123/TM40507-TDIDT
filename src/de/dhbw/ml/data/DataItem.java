package de.dhbw.ml.data;

import java.util.List;

public class DataItem {
	protected boolean teacher;
	protected List<AttributeValuePair> attributes;
	
	public boolean isTeacher() {
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
}
