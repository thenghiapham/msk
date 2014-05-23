package it.unitn.composes.usage;

import it.unitn.composes.tree.CcgTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class CcgXml2Txt {
	
	public static void convertDirectory(String ccgDir, String txtDir) {
		File inputDir = new File(ccgDir);
		File outputDir = new File(txtDir);
		if (inputDir.isDirectory() && outputDir.isDirectory()) {
			 File[] inputFiles = inputDir.listFiles();
			 for (File inputFile: inputFiles) {
				 String fileName = inputFile.getName();
				 String inputFilePath = inputFile.getAbsolutePath();
				 String outputFilePath = outputDir + "/" + fileName.replaceAll("xml", "txt");
				 System.out.println(fileName);
				 convertFile(inputFilePath, outputFilePath);
			 }
		} else {
			System.out.println(ccgDir + "  or " + txtDir + " is not a directory");
		}
	}
	
	public static void convertFile(String ccgFile, String txtFile) {
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(ccgFile),"iso-8859-1"));
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(txtFile));
			String line = inputReader.readLine();
			boolean oddSentence = true;
			boolean inCcg = false;
			String firstSentence = "";
			String secondSentence = "";
			String xmlString = "";
			while (line != null || "".equals(line)) {
				line = line.trim();
				if (line.equals("<ccg>")) {
					inCcg = true;
					xmlString = line;
				} else if (inCcg) {
					xmlString = xmlString + line;
				}
				if (line.equals("</ccg>")) {
					inCcg = false;
					String sentence = CcgTree.parseTreeFromCcgXml(xmlString).toPennTree();
					if (oddSentence) {
						firstSentence = sentence;
						oddSentence = false;
					} else {
						secondSentence = sentence;
						oddSentence = true;
						outputWriter.write(firstSentence + "\t" + secondSentence + "\n");
					}
				}
				line = inputReader.readLine();
			}
			outputWriter.flush();
			outputWriter.close();
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Needs 2 arguments: Ccg_directory Output_txt_directory");
			System.exit(1);
		}
		convertDirectory(args[0], args[1]);
	}
}