package de.dhbw.ml.data;

import java.util.List;

public class AttributeSet {
	protected int numberOfAttributes;
	protected List<Attribute> attributes;
	
	
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
	
	
}
