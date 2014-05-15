package it.unitn.composes.msk;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.unitn.composes.exception.ValueException;
import it.unitn.composes.tree.CcgTree;
import it.unitn.composes.tree.LexicalizedSemanticTree;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;

public class CollapsedTreeKernel3 extends CollapsedTreeKernel2 implements  KernelFunction<Tree> {
	public double value(Tree arg0, Tree arg1) {
		try {
			LexicalizedSemanticTree tree1 = composer.buildSemanticTree(CcgTree.ccgizeTree(arg0), semanticSpace);
			LexicalizedSemanticTree tree2 = composer.buildSemanticTree(CcgTree.ccgizeTree(arg1), semanticSpace);
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
