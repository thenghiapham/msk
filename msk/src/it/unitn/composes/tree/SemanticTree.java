package it.unitn.composes.tree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.BasicComposition;

public class SemanticTree extends Tree {
	protected double[] vector;
	public SemanticTree(Tree syntacticTree, VectorProvider semanticSpace, BasicComposition com) {
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
			this.setChildren(children);
			
		}
	}
	
	public double[] getVector() {
		return this.vector;
	}
	
	public Vector<SemanticTree> getAllNodes() {
		Vector<SemanticTree> all = new Vector<SemanticTree>();
		for (Tree child : this.getChildren())
			all.addAll(((SemanticTree) child).getAllNodes());
		all.add(this);
		return all;
	}
	
	public int getHeight() {
		
		if (this.isTerminal()) {
			return 0;
		} else if (this.isPreTerminal()) {
			return 1;
		} else {
			int maxChildHeight = 0;
			for (Tree child: this.getChildren()) {
				int childHeight = ((SemanticTree) child).getHeight();
				if (childHeight > maxChildHeight) {
					maxChildHeight = childHeight;
				}
			}
			return maxChildHeight + 1;
		}
	}	
}
