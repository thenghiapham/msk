package it.unitn.composes.composition;

import org.ejml.simple.SimpleMatrix;

import it.unitn.composes.space.SemanticSpace;
import it.unitn.composes.utils.SimpleMatrixUtils;

public class LexicalFunction implements BasicComposition{
	SemanticSpace functionSpace;

	@Override
	public double[] compose(double[] v1, double[] v2) {
		// TODO Auto-generated method stub
		int vectorLength = v2.length;
		int matrixSize = v1.length;
		SimpleMatrix target = SimpleMatrixUtils.buildSimpleMatrix(v2, vectorLength, 1);
		SimpleMatrix functor = SimpleMatrixUtils.buildSimpleMatrix(v1, matrixSize / vectorLength, vectorLength);
		return functor.mult(target).getMatrix().getData();
	}
}
