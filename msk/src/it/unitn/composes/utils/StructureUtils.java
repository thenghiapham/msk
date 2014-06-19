package it.unitn.composes.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.ejml.simple.SimpleMatrix;

public class StructureUtils {
	public static<E> HashMap<E,Integer> vector2HashMap(Vector<E> nodes) {
		HashMap<E, Integer> result = new HashMap<E,Integer>();
		for (int i=0; i < nodes.size(); i++) {
			result.put(nodes.elementAt(i), i);
		}
		return result;
	}
	
	public static HashMap<String, SimpleMatrix> readFaMatrixFile(String faMatrixFile) throws IOException{
		HashMap<String, SimpleMatrix> result = new HashMap<String, SimpleMatrix> ();
		BufferedReader reader = new BufferedReader(new FileReader(faMatrixFile));
		String line = reader.readLine();
		String[] dims = line.split("\t");
		int row = Integer.parseInt(dims[0]);
		int col = Integer.parseInt(dims[1]);
		int size = row * col;
		while (line != null) {
			
			line = reader.readLine();
			if (line == null) break;
			String[] elements = line.split("\t");
			if (elements.length != size + 1) {
				reader.close();
				throw new IOException("Wrong number of elements: " + (size + 1) 
						+ " vs. " + elements.length);
			}
			double[] data = new double[row * col];
			for (int i = 0; i < size; i++) {
				data[i] = Double.parseDouble(elements[i + 1]);
			}
			SimpleMatrix matrix = new SimpleMatrix(row, col, true, data);
			result.put(elements[0], matrix);
		}
		reader.close();
		return result;
	}
	
	public static void printFlattenMatrix(SimpleMatrix matrix, BufferedWriter writer) throws IOException{
		double[] data = matrix.getMatrix().data;
		for (int i = 0; i < data.length - 1; i++) {
			writer.write(data[i] + "\t");
		}
		writer.write("" + data[data.length - 1]);
	}
}
