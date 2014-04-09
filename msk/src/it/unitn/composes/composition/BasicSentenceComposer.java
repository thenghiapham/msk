package it.unitn.composes.composition;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.tree.SemanticTree;

public class BasicSentenceComposer implements SentenceComposer{
	protected BasicComposition compositionModel;
	public BasicSentenceComposer(BasicComposition compositionModel) {
		this.compositionModel = compositionModel;
	}
	public double[] compose(LexicalizedTree tree, VectorProvider semanticSpace) {
		SemanticTree semanticTree = new SemanticTree(tree, semanticSpace, compositionModel);
		return semanticTree.getVector();
	}
}
