package it.unitn.composes.usage;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class Parse2Svm {
	public static void main(String[] args) {
		String gsFile = args[0];
		String parseFile = args[1];
		String negationFile = args[2];
		String trainFile = args[3];
		String testFile = args[4];
		mergeFile(gsFile, parseFile, negationFile, trainFile, testFile);		
	}

	private static void mergeFile(String gsFile, String parseFile,
			String negationFile, String trainFile, String testFile) {
		try {
			BufferedReader gsReader = new BufferedReader(new FileReader(gsFile));
			BufferedReader parseReader = new BufferedReader(new FileReader(parseFile));
			BufferedReader negationReader = new BufferedReader(new FileReader(negationFile));
			BufferedWriter trainWriter = new BufferedWriter(new FileWriter(trainFile));
			BufferedWriter testWriter = new BufferedWriter(new FileWriter(testFile));
			
			
			Vector<String> outputStrings = new Vector<String>();
			String gsLine = gsReader.readLine();
			while (gsLine != null && !"".equals(gsLine)) {
				String parseString = parseReader.readLine();
				String negationString = negationReader.readLine().split(" ")[2];
				parseString = parseString.replace("\t", " |BT| ");
				parseString = gsLine + "\t |BT| " + parseString + " |ET|  |BV|  1:" + negationString + " |EV|\n";
				outputStrings.add(parseString);
				gsLine = gsReader.readLine();
			}
			Collections.shuffle(outputStrings);
			for (int i = 0; i < outputStrings.size() / 2; i++) {
				trainWriter.write(outputStrings.get(i));
			}
			
			for (int i = outputStrings.size() / 2; i < outputStrings.size(); i++) {
				testWriter.write(outputStrings.get(i));
			}
			gsReader.close();
			parseReader.close();
			negationReader.close();
			trainWriter.flush();
			trainWriter.close();
			testWriter.flush();
			testWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
