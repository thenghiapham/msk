package it.unitn.composes.msk;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.SemanticVectorProviderNEW;
import it.unitn.composes.composition.BasicComposition;
import it.unitn.composes.tree.SemanticTree;

public class CollapsedTreeKernel implements KernelFunction<Tree>{

	private double lambda = 0.4;
	protected SemanticVectorProviderNEW semanticSpace;
	protected BasicComposition composition; 
	
	public CollapsedTreeKernel(double lambda, SemanticVectorProviderNEW semanticSpace, 
			BasicComposition composition) {
		this.composition = composition;
		this.lambda = lambda;
		this.semanticSpace = semanticSpace;
	}
	
	public CollapsedTreeKernel() {
		semanticSpace = null;
		composition = null;
	}
	
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return value(arg0, arg1);
	}
	
	public double value(Tree arg0, Tree arg1) {
		SemanticTree tree1 = new SemanticTree(arg0, semanticSpace, composition);
		SemanticTree tree2 = new SemanticTree(arg1, semanticSpace, composition);
		return value(tree1, tree2);
	}
	
	public double value(SemanticTree tree1, SemanticTree tree2) {
		return 0;
	}

}
