package it.unitn.composes.msk;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.BasicComposition;
import it.unitn.composes.exception.ValueException;
import it.unitn.composes.tree.CcgTree;
import it.unitn.composes.tree.LexicalizedSemanticTree;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;

public class CollapsedTreeKernel1 extends CollapsedTreeKernel implements KernelFunction<Tree> {
	public CollapsedTreeKernel1(double lambda, VectorProvider semanticSpace, 
			BasicComposition composition) {
		super(lambda, semanticSpace, composition);
	}
	
	public double value(Tree arg0, Tree arg1) {
		try {
			LexicalizedSemanticTree tree1 = new LexicalizedSemanticTree(CcgTree.ccgizeTree(arg0), semanticSpace, composition);
			LexicalizedSemanticTree tree2 = new LexicalizedSemanticTree(CcgTree.ccgizeTree(arg1), semanticSpace, composition);
	//		System.out.println("*****************************");
	//		System.out.println("arg0: " + arg0.toPennTree());
	//		System.out.println("arg1: " + arg1.toPennTree());
	//		System.out.println("tree1: " + tree1.toPennTree());
	//		System.out.println("tree2: " + tree2.toPennTree());
			tree1.setParent(null);
			tree2.setParent(null);
			double value = value(tree1, tree2);
	//		System.out.println("value: " + value);
			
			return value;
		} catch (ValueException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
