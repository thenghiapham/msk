package it.unitn.composes.msk;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.SemanticVectorProviderNEW;
import it.uniroma2.util.vector.SemanticVectorProvider;

public class CollapsedTreeKernel implements KernelFunction<Tree>{

	private double lambda = 0.4;
	private SemanticVectorProviderNEW semanticSpace;
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public CollapsedTreeKernel(double lambda, SemanticVectorProviderNEW semanticSpace) {
		
	}
	

}
