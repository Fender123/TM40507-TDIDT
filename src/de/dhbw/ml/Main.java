package de.dhbw.ml;

import java.io.File;
import java.io.IOException;

import de.dhbw.ml.data.AttributeSet;
import de.dhbw.ml.data.DataItem;
import de.dhbw.ml.data.DataSet;
import de.dhbw.tree.ID3;
import de.dhbw.tree.Node;

public class Main {
	
	protected static String sourceFilename = "mietkartei.CSV";
	protected static String attribFilename = "Attribute_Mietkartei.txt";
	
	public static boolean DEBUG = false;

	public static void main(String[] args) {	
		// prüfen ob Debug Ausgaben aktiviert werden sollen
		String debugEnv = System.getenv("TDIDT_DEBUG");
		if(debugEnv != null && debugEnv.toLowerCase().equals("true")){
			DEBUG = true;
		}
		
		System.out.println("Usage:");
		System.out.println("java tdidt.jar dataSource.csv attributeSource.txt");
		System.out.println("if the one or both parameters are omitted it will fall back to hardcoded default filenames.");
		System.out.println();
		if(DEBUG){
			System.out.println("Debug output enabled!");
			System.out.println();
		}
		
		//Programmargumente auslesen
		readArgs(args);
		
		//Datei mit Quelldaten
		File sourceFile = new File(sourceFilename);
		
		//Datenstruktur für Quelldaten
		DataSet dataSet = null;
		
		try {
			//Datenstruktur erstellen (liest automatisch aus Datei)
			 dataSet = new DataSet(sourceFile);
		} catch (IOException e) {
			//bei Fehler abbrechen
			e.printStackTrace();
			System.out.println("Error reading source! Exiting...");
			return;
		}
		
		//Quelldaten in 2 gleich große Mengen aufteilen 
		DataSet[] splitSets = dataSet.split(2);
		DataSet testSet = splitSets[0];		//Testdaten
		DataSet traningSet = splitSets[1];	//Trainingsdaten
		
		//Datei mit Attributdaten
		File attribFile = new File(attribFilename);
		
		//Datenstruktur für Attributdaten
		AttributeSet attribSet = null;
		try{
			//Datenstruktur für Attributdaten erstellen, liest Werte aus Datei
			attribSet = new AttributeSet(attribFile);
		}catch(Exception e){
			//Im Fehlerfall abbrechen
			e.printStackTrace();
			System.out.println("Error reading attrib file! Exiting...");
			return;
		}

		//Zur Zeitmessung, Startzeit
		long start = System.nanoTime();
		//ID3 initialisieren
		ID3 tree = new ID3(traningSet, attribSet);
		//Entscheidungsbaum erstellen
		Node root = tree.buildTree();
		//Endzeit
		long end = System.nanoTime();
		//Berechnung der Dauer
		long duration = end - start;

		//Baum ausgeben
		root.print();
		System.out.println();
		
		//Rechenzeit für Baum ausgeben
		System.out.format("Tree completed in %.2fms\n", ((double) duration / 1000000.0));
		System.out.println();
		
		
		System.out.println("Starting test");
		//Variablen für Auswertung der Klassifikation der Kontrolldaten initialisieren
		int numCorrect = 0;
		int numWrong = 0;
		int numFalsePositive = 0;
		int numFalseNegative = 0;
		int numTruePositive = 0;
		int numTrueNegative = 0;
		//Alle Testdaten klassifizieren
		for (DataItem di : testSet.getItems()) {
			if(DEBUG){
				System.out.println(di.toString());
			}
			//Klassifikation des Datensatzes durch den Entscheidungsbaum
			boolean classificationRes = tree.classify(root, di);
			if(DEBUG){
				System.out.println("Class res: " + (classificationRes ? "1" : "0"));
			}
			//Prüfen des Ergebnisses
			if(classificationRes == di.getTeacher()){
				//Richtiges Ergebnis
				numCorrect++;
				if(DEBUG){
					System.out.println("correct");
				}
				if(di.getTeacher() == true && classificationRes == true){
					//True positive
					numTruePositive++;
				}else{
					//True negative
					numTrueNegative++;
				}
			}else{
				//Falsches Ergebnis
				numWrong++;
				if(DEBUG){
					System.out.println("wrong");
				}
				if(di.getTeacher() == true && classificationRes == false){
					//False positive
					numFalsePositive++;
				}else{
					//False negative
					numFalseNegative++;
				}
			}
		}
		
		System.out.format("\nClassified %d entries:\n\n", testSet.getNumberOfExamples());
		System.out.format("\tcorrect: %d (%.2f%%)\n", numCorrect, ((double) numCorrect / testSet.getNumberOfExamples() * 100.0));
		System.out.format("\twrong: %d (%.2f%%)\n\n", numWrong, ((double) numWrong / testSet.getNumberOfExamples() * 100.0));
		System.out.format("\tfalse positives: %d\n", numFalsePositive);
		System.out.format("\tfalse negatives: %d\n", numFalseNegative);
		System.out.format("\ttrue positives: %d\n", numTruePositive);
		System.out.format("\ttrue negatives: %d\n\n", numTrueNegative);
		
		
	}

	/**
	 * liest sourceFilename und attribFilename aus den Programmargumenten aus
	 * @param args
	 */
	protected static void readArgs(String[] args) {
		if(args.length > 0){
			sourceFilename = args[0];
		}
		if(args.length > 1){
			attribFilename = args[1];
		}
		
		System.out.format("Starting with sourceFile: %s and attribFile: %s\n", sourceFilename, attribFilename);
		System.out.println();
	}
}
