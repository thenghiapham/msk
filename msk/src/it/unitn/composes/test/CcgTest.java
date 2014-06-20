package it.unitn.composes.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import it.unitn.composes.tree.CcgTree;

public class CcgTest {
	public static void main(String[] args) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			String xmlString = "";
			String line = reader.readLine();
			while (!("".equals(line) || line == null)) {
				xmlString += line + "\n";
				line = reader.readLine();
			}
			CcgTree tree = CcgTree.parseTreeFromCcgXml(xmlString);
			List<CcgTree> trees = tree.getAllSubTree(2);
			for (CcgTree subtree: trees) {
				System.out.println(subtree.shortPennTree());
			}
			
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}