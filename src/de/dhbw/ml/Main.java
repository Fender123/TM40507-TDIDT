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
		
		readArgs(args);
		
		File sourceFile = new File(sourceFilename);
		
		DataSet dataSet = null;
		
		try {
			 dataSet = new DataSet(sourceFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error reading source! Exiting...");
			return;
		}
		
		DataSet[] splitSets = dataSet.split(2);
		DataSet testSet = splitSets[0];
		DataSet traningSet = splitSets[1];
		
		File attribFile = new File(attribFilename);
		
		AttributeSet attribSet = null;
		try{
			attribSet = new AttributeSet(attribFile);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error reading attrib file! Exiting...");
			return;
		}

		long start = System.nanoTime();
		ID3 tree = new ID3(traningSet, attribSet);
		Node root = tree.buildTree();
		long end = System.nanoTime();
		long duration = end - start;
		
		System.out.format("Tree completed in %.2fms\n", ((double) duration / 1000000.0));
		System.out.println();
		
		if(DEBUG){
			root.print();
		}
		
		System.out.println();
		System.out.println("Starting test");
		int numCorrect = 0;
		int numWrong = 0;
		int numFalsePositive = 0;
		int numFalseNegative = 0;
		int numTruePositive = 0;
		int numTrueNegative = 0;
		for (DataItem di : testSet.getItems()) {
			if(DEBUG){
				System.out.println(di.toString());
			}
			boolean classificationRes = tree.classify(root, di);
			if(DEBUG){
				System.out.println("Class res: " + (classificationRes ? "1" : "0"));
			}
			if(classificationRes == di.getTeacher()){
				numCorrect++;
				if(DEBUG){
					System.out.println("correct");
				}
				if(di.getTeacher() == true && classificationRes == true){
					numTruePositive++;
				}else{
					numTrueNegative++;
				}
			}else{
				numWrong++;
				if(DEBUG){
					System.out.println("wrong");
				}
				if(di.getTeacher() == true && classificationRes == false){
					numFalsePositive++;
				}else{
					numFalseNegative++;
				}
			}
		}
		
		System.out.format("\nClassified %d entries:\n\n\tcorrect: %d\n\twrong: %d\n\n\tfalse positives: %d\n\tfalse negatives: %d\n\ttrue positives: %d\n\ttrue negatives: %d\n\n", testSet.getNumberOfExamples(), numCorrect, numWrong, numFalsePositive, numFalseNegative, numTruePositive, numTrueNegative);
	}

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
