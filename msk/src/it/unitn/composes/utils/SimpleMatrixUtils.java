package it.unitn.composes.utils;

import org.ejml.simple.SimpleMatrix;

public class SimpleMatrixUtils {
	public static SimpleMatrix buildSimpleMatrix(double[] data, int row, int column) {
		double[][] tmpdata = new double[1][row * column];
		tmpdata[0] = data;
		SimpleMatrix result = new SimpleMatrix(tmpdata);
		result.reshape(row, column);
		return result;
	}
}
