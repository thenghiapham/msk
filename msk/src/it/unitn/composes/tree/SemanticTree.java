package it.unitn.composes.tree;

import java.util.Vector;

import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.SemanticVectorProviderNEW;
import it.unitn.composes.composition.BasicComposition;

public class SemanticTree extends Tree {
	private double[] vector;
	public SemanticTree(Tree syntacticTree, SemanticVectorProviderNEW semanticSpace, BasicComposition com) {
		String label = syntacticTree.getRootLabel();
		setRootLabel(label);
		Vector<Tree> children = new Vector<Tree>();
		if (syntacticTree.isTerminal()) {
			String posLabel = syntacticTree.getUsePosLabel();
			this.setUsePosLabel(posLabel);
			try {
				vector = semanticSpace.getVector(syntacticTree.getRootLabel());
			} catch (Exception e) {
				vector = null;
			}
			
		} else if (syntacticTree.isPreTerminal()) {
			SemanticTree child = new SemanticTree(syntacticTree.getChildren().get(0), semanticSpace, com);
			children.add(child);
			
			this.vector = child.vector;
			this.setChildren(children);
		} else {
			for (Tree syntacticChild: syntacticTree.getChildren()) {
				SemanticTree child = new SemanticTree(syntacticChild, semanticSpace, com);
				children.add(child);
				
				if (child.vector != null) {
					if (this.vector == null) {
						this.vector = child.vector;
					} else {
						this.vector = com.compose(this.vector, child.vector);
					}
				}
			}
			
		}
	}
}
