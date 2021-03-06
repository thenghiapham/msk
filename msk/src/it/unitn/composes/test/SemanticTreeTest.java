package it.unitn.composes.test;

import org.xml.sax.SAXException;

import it.uniroma2.util.tree.Tree;
import it.unitn.composes.tree.CcgTree;

public class SemanticTreeTest {
	public static void testParseTree() throws SAXException{
		String xmlString = "<ccg><rule type=\"fa\" cat=\"S[dcl]\\NP\">"
            +"<lf start=\"1\" span=\"1\" word=\"plays\" lemma=\"play\" pos=\"VBZ\" chunk=\"I-VP\" entity=\"O\" cat=\"(S[dcl]\\NP)/NP\" />"
            +"<rule type=\"lex\" cat=\"NP\">"
            +"   <lf start=\"2\" span=\"1\" word=\"guitar\" lemma=\"guitar\" pos=\"NN\" chunk=\"I-NP\" entity=\"O\" cat=\"N\" />"
            +"</rule>"
          +"</rule></ccg>";
		Tree tree = CcgTree.parseTreeFromCcgXml(xmlString);
		System.out.println(tree.toPennTree());
		String pennString = "(S[dcl]\\NP (<S[dcl]\\NP>/NP plays) (NP (N guitar)))";
		try {
			Tree tree1 = Tree.fromPennTree(pennString);
			System.out.println(tree1.toPennTree());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SAXException{
		testParseTree();
	}
}
