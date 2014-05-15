package it.unitn.composes.tree;

import it.uniroma2.util.tree.Tree;
import it.unitn.composes.exception.ValueException;

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

public class CcgTree extends Tree{
	String cat = "";
	String modifiedCat = "";
	String pos = "";
	String lemma = "";

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
			CcgTree tree = new CcgTree("None");
			tree.cat = "None";
			tree.lemma = "None";
			tree.pos = "None";
			tree.modifiedCat = "None";
			return tree;
			
		} else if (node.getNodeName().equals("rule")) {
			String rootLabel = node.getAttributes().getNamedItem("cat").getNodeValue();
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
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
			return result;
		} else if (node.getNodeName().equals("lf")) {
			NamedNodeMap attributes = node.getAttributes();
			String superTag = attributes.getNamedItem("cat").getNodeValue();
			String modifiedTag = superTag.replaceAll("\\(", "<");
			modifiedTag = modifiedTag.replaceAll("\\)", ">");
			String rootLabel = modifiedTag;
			rootLabel = rootLabel.replaceAll("\\(", "<");
			rootLabel = rootLabel.replaceAll("\\)", ">");
			CcgTree result = new CcgTree(rootLabel);
			Vector<Tree> children = new Vector<Tree>();
				
			CcgTree child = new CcgTree(attributes.getNamedItem("word").getNodeValue());
			child.lemma = attributes.getNamedItem("lemma").getNodeValue().toLowerCase();
			child.cat = superTag;
			child.modifiedCat = modifiedTag;
			child.pos = attributes.getNamedItem("pos").getNodeValue();
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
			if (getChildren().get(0).getChildren().size() != 0)
				return super.toPennTree();
			else {
				String treeString = "("+ this.getRootLabel();
				treeString += " ";
				CcgTree terminalChild = (CcgTree) getChildren().get(0); 
				treeString += terminalChild.getRootLabel() + "::" + terminalChild.lemma + "::" + terminalChild.pos + "::" + terminalChild.modifiedCat;
				treeString += ")";
				return treeString;
			}
		} else {
			return super.toPennTree();
		}
	}
	
//	public CcgTree fromPennTree() {
//		//TODO: a lot of stuff here
//		// Tree tree
//	}
	
	public static CcgTree ccgizeTree(Tree aTree) throws ValueException {
		if (aTree.isTerminal()) {
			String rootLabel = aTree.getRootLabel();
			String[] elements = rootLabel.split("::");
			if (elements.length != 4) {
				throw new ValueException("ccgizable terminal node should have 4 information");
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
			CcgTree newTree = new CcgTree(aTree.getRootLabel());
			Vector<Tree> newChildren = new Vector<Tree>();
			List<Tree> children = aTree.getChildren();
			for (Tree child:children) {
				newChildren.add(ccgizeTree(child));
			}
			newTree.setChildren(newChildren);
			return newTree;
		}
	}
}