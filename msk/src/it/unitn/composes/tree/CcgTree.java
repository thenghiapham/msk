package it.unitn.composes.tree;

import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.unitn.composes.exception.ValueException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

public class CcgTree extends Tree{
	public static final int LEFT_HEAD = 0;
	public static final int RIGHT_HEAD = 1;
	public static final int NO_HEAD = -1;
	String cat = "";
	String modifiedCat = "";
	String pos = "";
	String lemma = "";
	int headPos = NO_HEAD;

	public CcgTree(String rootLabel) {
		// TODO Auto-generated constructor stub
		super(rootLabel);
	}

	public static CcgTree parseTreeFromCcgXml(String xmlString) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes("iso-8859-1")));
			Node root = doc.getFirstChild();
			CcgTree result = parseTreeFromNode(root);	
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
	
	public static int getHeadFromRule(String rule) {
		if (rule.equals("ba")) {
			return RIGHT_HEAD;
		} else if (rule.equals("fa")) {
			return LEFT_HEAD;
		} else if (rule.equals("lex")) {
			return LEFT_HEAD;
		} else if (rule.equals("rp")) {
			return LEFT_HEAD;
		} else {
			//System.out.println("Strange rule: " + rule);
			return LEFT_HEAD;
		}
	}
	
	public static CcgTree createEmptyTree() {
		CcgTree tree = new CcgTree("None");
		tree.cat = "None";
		tree.lemma = "None";
		tree.pos = "None";
		tree.modifiedCat = "None";
		return tree;
	}
	
	public static CcgTree parseTreeFromNode(Node node) {
		if (node.getNodeName().equals("ccg")) {
			//System.out.println("First child:" + node.getFirstChild());
			if (node.getFirstChild() != null) {
				NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node childNode = childNodes.item(i);
					if (!childNode.getNodeName().equals("#text")) {
						return parseTreeFromNode(childNode);
					}
				}
			} 
			return createEmptyTree();
			
		} else if (node.getNodeName().equals("rule")) {
			String rootLabel = node.getAttributes().getNamedItem("cat").getNodeValue();
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
			rootLabel = rootLabel.replaceAll("\\[.*?\\]", "");
			CcgTree result = new CcgTree(rootLabel);
			Vector<Tree> children = new Vector<Tree>();
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (!childNode.getNodeName().equals("#text")) {
					children.add(parseTreeFromNode(childNode));
				}
			}
			result.setChildren(children);
			result.headPos = getHeadFromRule(node.getAttributes().getNamedItem("type").getNodeValue());
			return result;
		} else if (node.getNodeName().equals("lf")) {
			NamedNodeMap attributes = node.getAttributes();
			String superTag = attributes.getNamedItem("cat").getNodeValue();
			String modifiedTag = superTag.replaceAll("\\(", "<");
			modifiedTag = modifiedTag.replaceAll("\\)", ">");
			modifiedTag = modifiedTag.replaceAll(":", ".");
			modifiedTag = modifiedTag.replaceAll("_", "-");
			modifiedTag = modifiedTag.replaceAll("\\[.*?\\]", "");
			String rootLabel = modifiedTag;
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
			CcgTree result = new CcgTree(rootLabel);
			result.headPos = LEFT_HEAD;
			Vector<Tree> children = new Vector<Tree>();
			
			String word = attributes.getNamedItem("word").getNodeValue();
			if ("(".equals(word)) {
				word = "LRB";
			} else if (")".equals(word)) {
				word = "RRB";
			}
			word = word.replaceAll(":", ".");
			word = word.replaceAll("_", "-");
			
			CcgTree child = new CcgTree(word);
			
			String lemma = attributes.getNamedItem("lemma").getNodeValue().toLowerCase();
			if ("(".equals(lemma)) {
				lemma = "LRB";
			} else if (")".equals(lemma)) {
				lemma = "RRB";
			}
			lemma = lemma.replaceAll(":", ".");
			lemma = lemma.replaceAll("_", "-");
			child.lemma = lemma;
			child.cat = superTag;
			
			
			child.modifiedCat = modifiedTag;
			
			String pos = attributes.getNamedItem("pos").getNodeValue();
			pos = pos.replaceAll(":", ".");
			child.pos = pos;
//			System.out.println(child.lemma);
			children.add(child);
			result.setChildren(children);
//			System.out.println(result.toPennTree());
			return result;
		} else {
			System.out.println(node.getNodeName());
			System.out.println(node.getParentNode().getNodeName());
			return null;
		}
		
	}
	
	public String toPennTree() {
		if (this.getChildren().size() == 1) {
			String treeString = "("+ this.getRootLabel()+"__"+headPos;
			treeString += " ";
			if (getChildren().get(0).getChildren().size() != 0) {
				treeString += getChildren().get(0).toPennTree();
			} else {
				CcgTree terminalChild = (CcgTree) getChildren().get(0); 
				treeString += terminalChild.getRootLabel() + "__" + terminalChild.lemma + "__" + terminalChild.pos + "__" + terminalChild.modifiedCat;			
			}
			treeString += ")";
			return treeString;
		} else if (this.getChildren().size() > 1) {
			String treeString = "("+ getRootLabel() + "__" + headPos;
			treeString += " ";
			for (Tree child : getChildren())
				treeString += child.toPennTree();
			treeString += ")";
			return treeString;
		} else {
			if (this.isTerminal()) {
				String treeString = "("+ this.getRootLabel()+"__0";
				treeString += " ";
				treeString += getRootLabel() + "__" + lemma + "__" + pos + "__" + modifiedCat;
				treeString += ")";
				return treeString;
			} else 
				return super.toPennTree();
		}
	}
	
	
	public String toSimplePennTree() {
		if (this.getChildren().size() == 1) {
			String treeString = "("+ this.getRootLabel();
			treeString += " ";
			if (getChildren().get(0).getChildren().size() != 0) {
				treeString += ((CcgTree) getChildren().get(0)).toSimplePennTree();
			} else {
				CcgTree terminalChild = (CcgTree) getChildren().get(0); 
				treeString += terminalChild.getRootLabel();			
			}
			treeString += ")";
			return treeString;
		} else if (this.getChildren().size() > 1) {
			String treeString = "("+ getRootLabel();
			treeString += " ";
			for (Tree child : getChildren())
				treeString += ((CcgTree) child).toSimplePennTree();
			treeString += ")";
			return treeString;
		} else {
			if (this.isTerminal()) {
				String treeString = "("+ this.getRootLabel();
				treeString += " ";
				treeString += getRootLabel();
				treeString += ")";
				return treeString;
			} else 
				return null;
		}
	}
	
//	public CcgTree fromPennTree() {
//		//TODO: a lot of stuff here
//		// Tree tree
//	}
	
	public static CcgTree ccgizeTree(Tree aTree) throws ValueException {
		if (aTree.isTerminal()) {
			String rootLabel = aTree.getRootLabel();
			String[] elements = rootLabel.split("__");
			if (elements.length != 4) {
				throw new ValueException("ccgizable terminal node should have 4 information: " + rootLabel);
			}
			String word = elements[0];
			String lemma = elements[1];
			String pos = elements[2];
			String modifiedCat = elements[3];
			String cat = modifiedCat.replaceAll("<", "(");
			cat = cat.replaceAll(">", ")");
			CcgTree newTerminalNode = new CcgTree(word);
			newTerminalNode.cat = cat;
			newTerminalNode.lemma = lemma;
			newTerminalNode.modifiedCat = modifiedCat;
			newTerminalNode.pos = pos;
			return newTerminalNode;
		} else {
			String label = aTree.getRootLabel();
			String[] elements = label.split("__");
			if (elements.length == 1) {
				System.out.println("Wrong label:" + label);
				throw new ValueException("non-terminal node must have 2 information");
			}
			CcgTree newTree = new CcgTree(elements[0]);
			newTree.headPos = Integer.parseInt(elements[1]);
			Vector<Tree> newChildren = new Vector<Tree>();
			List<Tree> children = aTree.getChildren();
			for (Tree child:children) {
				newChildren.add(ccgizeTree(child));
			}
			newTree.setChildren(newChildren);
			return newTree;
		}
	}
	
	public LexicalizedTree toLexicalizeTree() {
		//TODO: do something here
		LexicalizedTree resultTree = new LexicalizedTree(this);
		if ("None".equals(cat)) {
			resultTree.setLemma(lemma);
		} else if (isTerminal()) {
			resultTree.setLemma(lemma);
		} else {
			Vector<Tree> newChildren = new Vector<Tree>();
			List<Tree> children = this.getChildren();
			for (Tree child:children) {
				newChildren.add(((CcgTree) child).toLexicalizeTree());
			}
			resultTree.setChildren(newChildren);
			resultTree.setLemma(((CcgTree) children.get(headPos)).getLemma());
		}
		return resultTree;
	}
	
	public static LexicalizedTree lexicalize(Tree tree) {
		try {
			return CcgTree.ccgizeTree(tree).toLexicalizeTree();
		} catch (ValueException e) {
			e.printStackTrace();
			System.out.println("Wrong tree:" + tree.toPennTree());
			return createEmptyTree().toLexicalizeTree();
		}
	}
	
	public String getLemma() {
		return lemma;
	}
	
	
	public List<CcgTree> getAllSubTree(int height) {
		List<CcgTree> list = new ArrayList<CcgTree>(10);
		getHeightAndAdd(list, height);
		return list;
	}
	
	private int getHeightAndAdd(List<CcgTree> list, int targetHeight) {
		int height = 0;
		List<Tree> children = getChildren();
		if (children.size() == 0) {
			height = 1;
			if (this.pos.length() == 1) height = 10;
		} else {
			if (children.size() == 1) {
				CcgTree ccgChild = (CcgTree) children.get(0);
				height = ccgChild.getHeightAndAdd(list, targetHeight);
			} else {
				for (Tree child: children) {
					CcgTree ccgChild = (CcgTree) child;
					height = Math.max(height, ccgChild.getHeightAndAdd(list, targetHeight));
				}
				height = height + 1;
			}
		}
		if (height == targetHeight) {
			list.add(this);
		}
		return height;
	}
	
	public String shortPennTree() {
		List<Tree> children = this.getChildren();
		String treeString = "("+ this.getRootLabel();
		treeString += " ";
		if (children.size() == 1) {
			CcgTree child = (CcgTree) getChildren().get(0); 
			if (children.get(0).getChildren().size() != 0)
				treeString += child.shortPennTree();
			else {
				treeString += child.pos;
			}
		} else {
			if (this.isTerminal()) {
				treeString += pos;
			} else {
				for (Tree child : children)
					treeString += ((CcgTree) child).shortPennTree();
			}
		}
		treeString += ")";
		return treeString;
	}
}