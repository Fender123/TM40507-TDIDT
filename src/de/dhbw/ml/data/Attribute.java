package de.dhbw.ml.data;

/**
 * Ein Attribut mit Namen und allen möglichen Werten
 * @author Michael
 *
 */
public class Attribute {
	protected String name;
	protected String[] values;
	protected int numberOfValues;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public int getNumberOfValues() {
		return numberOfValues;
	}
	public void setNumberOfValues(int numberOfValues) {
		this.numberOfValues = numberOfValues;
	}

	
	/**
	 * String Repräsentation der Datenstruktur
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : values) {
			if(!first){
				sb.append(", ");
			}
			if(first){
				first = false;
			}
			sb.append(s);
		}
		return String.format("%s (%d): %s", name, getNumberOfValues(), sb.toString());
	}
}
