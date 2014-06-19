package it.unitn.composes.msk;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.SentenceComposer;
import it.unitn.composes.math.VectorUtils;
import it.unitn.composes.tree.CcgTree;


public class CcgRootVectorKernel implements KernelFunction<Tree>{
	protected SentenceComposer sentenceComposer;
	protected VectorProvider semanticSpace;
	public CcgRootVectorKernel(SentenceComposer composer, VectorProvider semanticSpace) {
		this.sentenceComposer = composer;
		this.semanticSpace = semanticSpace;
	}
	
	public double value(Tree arg0, Tree arg1) {
		try {
			CcgTree tree1 = CcgTree.ccgizeTree(arg0);
			CcgTree tree2 = CcgTree.ccgizeTree(arg0);
			double[] vector1 = sentenceComposer.compose(tree1, semanticSpace);
			double[] vector2 = sentenceComposer.compose(tree2, semanticSpace);
			return VectorUtils.cosine(vector1, vector2);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Wrong tree 1:" + arg0.toPennTree());
			System.out.println("Wrong tree 2:" + arg1.toPennTree());
			return 0;
		}
	}
	
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return value(arg0, arg1);
	}
	
}
