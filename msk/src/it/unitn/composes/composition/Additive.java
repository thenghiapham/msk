package it.unitn.composes.composition;
import it.uniroma2.util.math.ArrayMath;

public class Additive implements BasicComposition {
	private double alpha;
	private double beta;
	public Additive() {
		alpha  = 1.0;
		beta = 1.0;
	}
	public Additive(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public double[] compose(double[] v1, double[] v2) {
		v1 = ArrayMath.scalardot(alpha, v1);
		v2 = ArrayMath.scalardot(beta, v2);
		double[] sum;
		try {
			sum = ArrayMath.sum(v1, v2);
		} catch (Exception e) {
			e.printStackTrace();
			sum = null;
		}
		return sum;
	}
}
