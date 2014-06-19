package it.unitn.composes.composition;

import org.ejml.simple.SimpleMatrix;

public class FullAdditive implements BasicComposition{
	protected SimpleMatrix transposedStackedMatrix;
	public FullAdditive(SimpleMatrix tranposedStackedMatrix) {
		this.transposedStackedMatrix = tranposedStackedMatrix;
	}

	@Override
	public double[] compose(double[] v1, double[] v2) {
		SimpleMatrix inputMatrix;
		if (transposedStackedMatrix.numRows() == (v1.length + v2.length + 1)) {
			double[][] stackedVector = new double[1][v1.length + v2.length + 1];
			System.arraycopy(v1, 0, stackedVector[0], 0, v1.length);
			System.arraycopy(v2, 0, stackedVector[0], v1.length, v2.length);
			stackedVector[0][v1.length + v2.length] = 1;
			inputMatrix = new SimpleMatrix(stackedVector);
		} else {
			double[][] stackedVector = new double[1][v1.length + v2.length];
			System.arraycopy(v1, 0, stackedVector[0], 0, v1.length);
			System.arraycopy(v2, 0, stackedVector[0], v1.length, v2.length);
			inputMatrix = new SimpleMatrix(stackedVector);
		}
		return inputMatrix.mult(transposedStackedMatrix).getMatrix().getData();
	}
}
