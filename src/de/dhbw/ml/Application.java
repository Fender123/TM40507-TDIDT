package de.dhbw.ml;

import java.io.File;
import java.io.IOException;

import de.dhbw.ml.data.DataSet;

public class Application {

	public static void main(String[] args) {
		File sourceFile = new File("mietkartei.CSV");
		
		DataSet ds;
		
		try {
			 ds = new DataSet(sourceFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
