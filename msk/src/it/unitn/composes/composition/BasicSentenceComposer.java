package it.unitn.composes.composition;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.tree.CcgTree;
import it.unitn.composes.tree.LexicalizedSemanticTree;

public class BasicSentenceComposer implements SentenceComposer{
	protected BasicComposition compositionModel;
	public BasicSentenceComposer(BasicComposition compositionModel) {
		this.compositionModel = compositionModel;
	}
	public double[] compose(LexicalizedTree tree, VectorProvider semanticSpace) {
		LexicalizedSemanticTree semanticTree = new LexicalizedSemanticTree(tree, semanticSpace, compositionModel);
		return semanticTree.getVector();
	}
	
	public double[] compose(CcgTree tree, VectorProvider semanticSpace) {
		LexicalizedSemanticTree semanticTree = new LexicalizedSemanticTree(tree, semanticSpace, compositionModel);
		return semanticTree.getVector();
	}
	@Override
	public LexicalizedSemanticTree buildSemanticTree(LexicalizedTree tree,
			VectorProvider semanticSpace) {
		return new LexicalizedSemanticTree(tree, semanticSpace, compositionModel);
	}
	
	@Override
	public LexicalizedSemanticTree buildSemanticTree(CcgTree tree,
			VectorProvider semanticSpace) {
		return new LexicalizedSemanticTree(tree, semanticSpace, compositionModel);
	}
}
