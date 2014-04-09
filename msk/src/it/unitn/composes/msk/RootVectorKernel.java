package it.unitn.composes.msk;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.SentenceComposer;
import it.unitn.composes.math.VectorUtils;


public class RootVectorKernel implements KernelFunction<Tree>{
	protected SentenceComposer sentenceComposer;
	protected VectorProvider semanticSpace;
	public RootVectorKernel(SentenceComposer composer, VectorProvider semanticSpace) {
		this.sentenceComposer = composer;
		this.semanticSpace = semanticSpace;
	}
	
	public double value(Tree arg0, Tree arg1) {
		LexicalizedTree tree1 = LexicalizedTree.transform(arg0);
		LexicalizedTree tree2 = LexicalizedTree.transform(arg1);
		double[] vector1 = sentenceComposer.compose(tree1, semanticSpace);
		double[] vector2 = sentenceComposer.compose(tree2, semanticSpace);
		return VectorUtils.cosine(vector1, vector2);
	}
	
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return value(arg0, arg1);
	}
	
	
}
