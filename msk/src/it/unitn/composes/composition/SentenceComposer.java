package it.unitn.composes.composition;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.tree.CcgTree;
import it.unitn.composes.tree.LexicalizedSemanticTree;

public interface SentenceComposer {
	public double[] compose(LexicalizedTree tree, VectorProvider semanticSpace);
	public double[] compose(CcgTree tree, VectorProvider semanticSpace);
	public LexicalizedSemanticTree buildSemanticTree(LexicalizedTree tree, VectorProvider semanticSpace);
	public LexicalizedSemanticTree buildSemanticTree(CcgTree tree, VectorProvider semanticSpace);
}
