package it.unitn.composes.tree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.uniroma2.util.math.ArrayMath;
import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.BasicComposition;

public class LexicalizedSemanticTree extends Tree{
	
	private double[] vector;
	String lemma="";
	public LexicalizedSemanticTree(LexicalizedTree syntacticTree, VectorProvider semanticSpace, BasicComposition com) {
		String label = syntacticTree.getRootLabel();
		setRootLabel(label);
		Vector<Tree> children = new Vector<Tree>();
		lemma = syntacticTree.getLemma();
		if (lemma == null) lemma = "";
		if (syntacticTree.isTerminal()) {
			
			
		} else if (syntacticTree.isPreTerminal()) {
			LexicalizedTree syntacticChild = (LexicalizedTree) syntacticTree.getChildren().get(0);
			LexicalizedSemanticTree child = new LexicalizedSemanticTree(syntacticChild, semanticSpace, com);
			children.add(child);
			try {
				
				vector = semanticSpace.getVector(lemma);
			} catch (Exception e) {
				e.printStackTrace();
				vector = ArrayMath.zeros(semanticSpace.getVectorSize());
			}
			
			child.vector = this.vector;
			this.setChildren(children);
		} else {
			for (Tree syntacticChild: syntacticTree.getChildren()) {
				LexicalizedSemanticTree child = new LexicalizedSemanticTree((LexicalizedTree) syntacticChild, semanticSpace, com);
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
	
	public Vector<LexicalizedSemanticTree> getAllNodes() {
		Vector<LexicalizedSemanticTree> all = new Vector<LexicalizedSemanticTree>();
		for (Tree child : this.getChildren())
			all.addAll(((LexicalizedSemanticTree) child).getAllNodes());
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
				int childHeight = ((LexicalizedSemanticTree) child).getHeight();
				if (childHeight > maxChildHeight) {
					maxChildHeight = childHeight;
				}
			}
			return maxChildHeight + 1;
		}
	}

	public static Tree parseTreeFromCcgXml(String xmlString, boolean useLemma) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes("iso-8859-1")));
			Node root = doc.getFirstChild();
			Tree result = parseTreeFromNode(root, useLemma);
			result.setParent(null);
			return result;
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException saxe) {
			saxe.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Tree parseTreeFromNode(Node node, boolean useLemma) {
		if (node.getNodeName().equals("ccg")) {
			return parseTreeFromNode(node.getFirstChild(), useLemma);
		} else if (node.getNodeName().equals("rule")) {
			String rootLabel = node.getAttributes().getNamedItem("cat").getNodeValue();
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
			Tree result = new Tree(rootLabel);
			Vector<Tree> children = new Vector<Tree>();
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (!childNode.getNodeName().equals("#text")) {
				children.add(parseTreeFromNode(childNode, useLemma));
				}
			}
			result.setChildren(children);
			return result;
		} else if (node.getNodeName().equals("lf")) {
			NamedNodeMap attributes = node.getAttributes(); 
			String rootLabel = attributes.getNamedItem("cat").getNodeValue();
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
			Tree result = new Tree(rootLabel);
			Vector<Tree> children = new Vector<Tree>();
			if (useLemma) {
				children.add(new Tree(attributes.getNamedItem("lemma").getNodeValue()));
			} else {
				children.add(new Tree(attributes.getNamedItem("word").getNodeValue()));
			}
			result.setChildren(children);
			return result;
		} else {
			System.out.println(node.getNodeName());
			System.out.println(node.getParentNode().getNodeName());
			return null;
		}
		
	}
	
	@Override
	public String toPennTree() {
		String treeString = "";
		if (vector == null) {
			treeString = "("+getRootLabel() + ":" + lemma + ":##null";
		} else {
			treeString = "("+getRootLabel() + ":" + lemma + ":##" + vector[0] +"#" + vector[1];
		}
		List<Tree>  children = getChildren(); 
		if (children.size() == 1) {
			treeString += " ";
			if (children.get(0).getChildren().size() == 0)
				treeString += children.get(0).getRootLabel();
			else
				treeString += children.get(0).toPennTree();
		}
		else if (children.size() > 1) {
			treeString += " ";
			for (Tree child : children)
				treeString += child.toPennTree();
		}
		treeString += ")";
		return treeString;
	}
}
