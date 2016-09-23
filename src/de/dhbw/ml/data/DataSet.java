package de.dhbw.ml.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Datenstruktur für Quelldaten
 * 
 * @author Michael
 *
 */
public class DataSet {
	/**
	 * Anzahl Datensätze
	 */
	protected int numberOfExamples = 0;
	/**
	 * Anzahl Datensätze mit positiver Entscheidung
	 */
	protected int numberOfPositives = 0;
	/**
	 * Anzahl Datensätze mit negativer Entscheidung
	 */
	protected int numberOfNegatives = 0;
	/**
	 * Liste der Datensätze
	 */
	protected List<DataItem> items;
	
	public DataSet(){
		items = new ArrayList<>();
	}
	
	public DataSet(File sourceFile) throws IOException {
		this();
		
		//Trennezeichen ermitteln
		Character seperator = guessSeparator(sourceFile);
		
		FileReader reader = new FileReader(sourceFile);
		
		//CSVReader erstellen
		CSVReader csvReader = new CSVReader(reader, seperator);
		String[] line;	//aktuelle Zeile
		Boolean firstLine = true;
		String[] headlines = null;	//Überschriften
		while((line = csvReader.readNext())	!= null){
			//Datei Zeile für Zeile lesen
			if(firstLine){
				//Erste Zeile enthält die Überschriften
				firstLine = false;
				headlines = line.clone();
				continue;
			}
			
			//alle anderen Zeilen enthalten je einen Datensatz
			DataItem di = new DataItem();
			//die Entscheidung des Teachers steht im vorletzten Feld (letztes Feld ist leer, weil die Zeile mit einem , aufhört)
			Boolean teacher = line[line.length - 2].trim().equals("ja");
			di.setTeacher(teacher);
			//alle Attribute und deren Werte auslesen und speichern
			List<AttributeValuePair> avps = new ArrayList<>();
			int i;
			for (i = 0; i < line.length - 2; i++) {
				AttributeValuePair avp = new AttributeValuePair();
				avp.setAttribute(headlines[i].trim());
				avp.setValue(line[i].trim());
				avps.add(avp);
			}
			di.setAttributes(avps);
			
			//Datensatz hinzufügen und je nach Entscheidung den entsprechenden Zähler hochzählen
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

	/**
	 * Ermittelt das verwendete Trennzeichen der Eingabe csv Datei
	 * nötig da dieses in den Beispielen nicht immer einheitlich war
	 * @param file
	 * @return Trennzeichen
	 * @throws IOException
	 */
	protected Character guessSeparator(File file) throws IOException {
		//Trennzeichen der Eingabe csv Datei erraten (entweder , oder ;)
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = bufferedReader.readLine();	//erste Zeile lesen
		Character separator = ';';
		if(line != null){
			int countColon = line.length() - line.replace(",", "").length();	//Anzahl , zählen
			int countSemiColon = line.length() - line.replace(";", "").length();//Anzahl ; zählen
			if(countColon > countSemiColon){	//häufigeres Zeichen wird als Trennzeichen interpretiert
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

	/**
	 * Teilt die Datensätze in die gewünschte Anzahl an Listen auf
	 * zur besseren Nachvollziehbarkeit wird hier auf eine zufällige Aufteilung verzichtet!
	 * @param num
	 * @return
	 */
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

	/**
	 * Filtert die Liste der Datensätze und gibt nur diese zurück die beim gewünschten Attribut den gesuchten Wert haben
	 * @param name Name des Attributes
	 * @param attribValue Wert des Attributes
	 * @return
	 */
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
	
	/**
	 * berechnet die Zähler anhand der Datensätze in der Liste neu
	 */
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
