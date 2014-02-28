package it.unitn.composes.test;

import it.uniroma2.tk2.TreeKernel;
import it.uniroma2.util.tree.Tree;
import it.unitn.composes.composition.Additive;
import it.unitn.composes.msk.CollapsedTreeKernel;
import it.unitn.composes.msk.NaiveTreeKernel;
import it.unitn.composes.space.SemanticSpace;

import java.io.File;

public class MixedSaladKernelTest {
	public static void main(String[] args) {
		SemanticSpace space = new SemanticSpace(300, new File("/home/pham/work/project/msk/spaces/core_ppmi_svd300.dm"));
		CollapsedTreeKernel treeKernel = new CollapsedTreeKernel(0.4, space, new Additive());
		TreeKernel oldKernel = new TreeKernel();
		TreeKernel.lambda = 0.4;
		TreeKernel.lexicalized = true;
		
		NaiveTreeKernel naiveKernel = new NaiveTreeKernel(0.4, space, new Additive());
		
		
		//treeKernel.s
		try {
			Tree tree1 = Tree.fromPennTree("(NP (DT a) (N cat-n))");
			Tree tree2 = Tree.fromPennTree("(NP (DT the) (N dog-n))");
			System.out.println(treeKernel.evaluate(tree1, tree2));
			System.out.println(oldKernel.evaluate(tree1, tree2));
			System.out.println(naiveKernel.evaluate(tree1, tree2));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
