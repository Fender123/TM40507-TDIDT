package de.dhbw.ml.data;

/**
 * Ein Paar bestehend aus Attributname und Attributwert
 * @author Michael
 *
 */
public class AttributeValuePair {
	protected String attribute;
	protected String value;
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return attribute + "=" + value;
	}
}
