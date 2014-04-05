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
		SemanticSpace space = new SemanticSpace(300, new File("/home/pham/real-single-target.dm"));
		CollapsedTreeKernel treeKernel = new CollapsedTreeKernel(0.4, space, new Additive());
		TreeKernel oldKernel = new TreeKernel();
		TreeKernel.lambda = 0.4;
		TreeKernel.lexicalized = true;
		
		NaiveTreeKernel naiveKernel = new NaiveTreeKernel(0.4, space, new Additive());
		
		
		//treeKernel.s
		try {
			Tree tree1 = Tree.fromPennTree("(S#decline$v:-1:262143 (NP#Schueller$n:-1:63 (NP#Schueller$n:-1:63 (NNP#Regina$n:1:7 Regina)(NNP#Schueller$n:2:56 Schueller)))(VP#decline$v:-1:262080 (VBD#decline$v:-1:0 declined)(S#comment$v:-1:262080 (VP#comment$v:-1:262080 (TO#to$t:-1:0 to)(VP#comment$v:-1:262080 (VB#comment$v:-1:0 comment)(PP#report$n:-1:262080 (IN#on$i:-1:0 on)(NP#report$n:-1:262080 (PP#newspaper$n:-1:262080 (IN#in$i:-1:0 in)(NP#newspaper$n:-1:262080 (NP#Italy$n:-1:448 (NNP#Italy$n:3:448 Italy)(POS#'s$p:-1:0 's))(NNP#La$n:4:3584 La)(NNP#Repubblica$n:5:28672 Repubblica)(NN#newspaper$n:6:229376 newspaper))))))))))");
			Tree tree2 = Tree.fromPennTree("(S#work$v:-1:262143 (NP#Shueller$n:-1:63 (NNP#Regina$n:1:7 Regina)(NNP#Shueller$n:2:56 Shueller))(VP#work$v:-1:262080 (VBZ#work$v:-1:0 works)(PP#newspaper$n:-1:262080 (IN#for$i:-1:0 for)(NP#newspaper$n:-1:262080 (NP#Italy$n:-1:448 (NNP#Italy$n:3:448 Italy)(POS#'s$p:-1:0 's))(NNP#La$n:4:3584 La)(NNP#Repubblica$n:5:28672 Repubblica)(NN#newspaper$n:6:229376 newspaper))))(.#.$.:-1:0 .))");
			System.out.println(treeKernel.evaluate(tree1, tree2));
			System.out.println(oldKernel.evaluate(tree1, tree2));
			System.out.println(naiveKernel.evaluate(tree1, tree2));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
