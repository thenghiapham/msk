package it.unitn.composes.test;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;

public class LexicalizedTreeTest {
	public static void main(String[] args) {
		try {
			Tree tree = Tree.fromPennTree("(NP (CD ECB)(NP#Italy$n:-1:448 Italy))");
			LexicalizedTree tree1 = LexicalizedTree.transform(tree);
			LexicalizedTree pnode = (LexicalizedTree) tree1.getChildren().get(1);
			LexicalizedTree tnode = (LexicalizedTree) pnode.getChildren().get(0);
			System.out.println(pnode.getLemma());
			System.out.println(tnode.getLemma());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
