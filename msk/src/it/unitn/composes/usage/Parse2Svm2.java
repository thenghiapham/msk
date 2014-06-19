package it.unitn.composes.usage;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Parse2Svm2 {
	public static void main(String[] args) {
		String gsDir = args[0];
		String parseDir = args[1];
		String featureDir = args[2];
		String svmDir = args[3];
		mergeDir(gsDir, parseDir, featureDir, svmDir);		
	}
	
	private static void mergeDir(String gsDir, String parseDir, String featureDir, String svmDir) {
		File inputDir = new File(parseDir);
		if (inputDir.isDirectory()) {
			 File[] inputFiles = inputDir.listFiles();
			 for (File parseFile: inputFiles) {
				 String fileName = parseFile.getName();
				 String gsFile = gsDir + "/" + fileName.replaceAll("input", "gs");
				 String featureFile = featureDir + "/" + fileName;
				 String svmFile = svmDir + "/" + fileName;
				 mergeFile(gsFile, parseFile.getAbsolutePath(), featureFile, svmFile);
			 }
		}
	}

	private static void mergeFile(String gsFile, String parseFile,
			String featureFile, String svmFile) {
		try {
			BufferedReader gsReader = new BufferedReader(new FileReader(gsFile));
			BufferedReader parseReader = new BufferedReader(new FileReader(parseFile));
			BufferedReader featureReader = new BufferedReader(new FileReader(featureFile));
			BufferedWriter svmWriter = new BufferedWriter(new FileWriter(svmFile));
			
			
			Vector<String> outputStrings = new Vector<String>();
			String gsLine = gsReader.readLine();
			while (gsLine != null && !"".equals(gsLine)) {
				String parseString = parseReader.readLine();
				String negationString = featureReader.readLine().split(" ")[2];
				parseString = parseString.replace("\t", " |BT| ");
				parseString = gsLine + "\t |BT| " + parseString + " |ET|  |BV|  1:" + negationString + " |EV|\n";
				outputStrings.add(parseString);
				gsLine = gsReader.readLine();
			}
			for (int i = 0; i < outputStrings.size(); i++) {
				svmWriter.write(outputStrings.get(i));
			}
			
			gsReader.close();
			parseReader.close();
			featureReader.close();
			svmWriter.flush();
			svmWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
