package it.unitn.composes.composition;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.vector.VectorProvider;

public interface SentenceComposer {
	public double[] compose(LexicalizedTree tree, VectorProvider semanticSpace);
}
