package it.unitn.composes.composition;

import it.unitn.composes.math.VectorUtils;

public class Multiplicative {
	public Multiplicative() {
		
	}
	
	public double[] compose(double[] v1, double[] v2) {
		double[] product;
		try {
			product = VectorUtils.pointwiseProduct(v1, v2);
		} catch (Exception e) {
			e.printStackTrace();
			product = null;
		}
		return product;
	}

}
