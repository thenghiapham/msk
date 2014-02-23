package it.unitn.composes.math;

public class VectorUtils {
	public static double[] pointwiseProduct(double[] v1, double[] v2) {
		if (v1 == null || v2 == null) {
			System.out.println("Null vector");
			return null;
		} else if (v1.length != v2.length) {
			System.out.println("Multiply vectors of different length");
			return null;
		} else {
			double[] result = new double[v1.length];
			for (int i = 0; i < v1.length; i++) {
				result[i] = v1[i] + v2[i];
			}
			return result;
		}
	}
}
