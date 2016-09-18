package de.dhbw.ml;

import java.io.File;
import java.io.IOException;

import de.dhbw.ml.data.AttributeSet;
import de.dhbw.ml.data.DataItem;
import de.dhbw.ml.data.DataSet;
import de.dhbw.tree.ID3;
import de.dhbw.tree.Node;

public class Application {
	
	protected static String sourceFilename = "mietkartei.CSV";
	protected static String attribFilename = "Attribute_Mietkartei.txt";

	public static void main(String[] args) {
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
		
		ID3 tree = new ID3(traningSet, attribSet);
		Node root = tree.buildTree();
		
		root.print();
		System.exit(1);
		
		System.out.println("Tree completed");
		
		System.out.println("Starting test");
		int numCorrect = 0;
		int numWrong = 0;
		for (DataItem di : testSet.getItems()) {
			System.out.println(di.toString());
			boolean classificationRes = tree.classify(root, di);
			if(classificationRes == di.getTeacher()){
				numCorrect++;
				System.out.println("correct");
			}else{
				numWrong++;
				System.out.println("wrong");
			}
		}
	}

	protected static void readArgs(String[] args) {
		if(args.length > 0){
			sourceFilename = args[0];
		}
		if(args.length > 1){
			attribFilename = args[1];
		}
		
		System.out.format("Starting with sourceFile: %s and attribFile: %s", sourceFilename, attribFilename);
		System.out.println();
	}
}
