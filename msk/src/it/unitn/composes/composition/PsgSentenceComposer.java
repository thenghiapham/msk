package it.unitn.composes.composition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.ejml.simple.SimpleMatrix;

import it.uniroma2.util.math.ArrayMath;
import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.tree.CcgTree;
import it.unitn.composes.tree.LexicalizedSemanticTree;
import it.unitn.composes.utils.StructureUtils;

public class PsgSentenceComposer implements SentenceComposer{
	
	protected HashMap<String, BasicComposition> compositions;
	public PsgSentenceComposer(HashMap<String, BasicComposition> compositions) {
		this.compositions = compositions;
		
	}
	
	public PsgSentenceComposer(String faMatrixFile) {
		this.compositions = new HashMap<String, BasicComposition>();
		try {
			HashMap<String, SimpleMatrix> matrices = StructureUtils.readFaMatrixFile(faMatrixFile);
			Iterator<String> keyIterator = matrices.keySet().iterator();
			while (keyIterator.hasNext()) {
				String structure = keyIterator.next().toString();
				SimpleMatrix matrix = matrices.get(structure);
				BasicComposition composition = new FullAdditive(matrix);
				this.compositions.put(structure, composition);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	protected boolean hasComposition(LexicalizedTree tree, LexicalizedTree headChild) {
		if (headChild == null) return false;
		String headLemma = headChild.getLemma();
		char headType = headLemma.charAt(headLemma.length() - 1);
		if (headType != 'v' && headType != 'n') {
			return false;
		}
		if (headType == 'n') {
			if ("NN".equals(headChild.getRootLabel()) || "NNS".equals(headChild.getRootLabel())) {
				for (Tree child: tree.getChildren()) {
					if ("JJ".equals(child.getRootLabel())) return true;
				}
			}
		}
		if (headType == 'v') {
			if (headChild.getRootLabel().startsWith("VB")) {
				for (Tree child: tree.getChildren()) {
					if ("NP".equals(child.getRootLabel())) return true;
				}
			} 
//			else if (headChild.getRootLabel().startsWith("VP")) {
//				for (Tree child: tree.getChildren()) {
//					if ("NP".equals(child.getRootLabel())) return true;
//				}
//			}
		}
		return false;
	}
	
	protected boolean hasComposition(CcgTree tree, LexicalizedTree headChild) {
		//TODO: fix here
		if (headChild == null) return false;
		String headLemma = headChild.getLemma();
		char headType = headLemma.charAt(headLemma.length() - 1);
		if (headType != 'v' && headType != 'n') {
			return false;
		}
		if (headType == 'n') {
			if ("NN".equals(headChild.getRootLabel()) || "NNS".equals(headChild.getRootLabel())) {
				for (Tree child: tree.getChildren()) {
					if ("JJ".equals(child.getRootLabel())) return true;
				}
			}
		}
		if (headType == 'v') {
			if (headChild.getRootLabel().startsWith("VB")) {
				for (Tree child: tree.getChildren()) {
					if ("NP".equals(child.getRootLabel())) return true;
				}
			} 
//			else if (headChild.getRootLabel().startsWith("VP")) {
//				for (Tree child: tree.getChildren()) {
//					if ("NP".equals(child.getRootLabel())) return true;
//				}
//			}
		}
		return false;
	}
	
	protected double[] composeSingleStructure(LexicalizedSemanticTree head, LexicalizedSemanticTree component) {
		double[] headVector = head.getVector();
		String componentType = getCompositionType(head, component);
//		System.out.println(componentType);
		if (compositions.containsKey(componentType)) {
			return compositions.get(componentType).compose(head.getVector(), component.getVector());
		} else {
			return new double[headVector.length];
		}
	}
	
	protected String getCompositionType(LexicalizedSemanticTree head, LexicalizedSemanticTree component) {
		String headLemma = head.getLemma();
		char headType = headLemma.charAt(headLemma.length() - 1);
		if (headType == 'n') {
			if ("NN".equals(head.getRootLabel()) || "NNS".equals(head.getRootLabel())) {
				if ("JJ".equals(component.getRootLabel())) return "AN";
			}
		}
		if (headType == 'v') {
			if (head.getRootLabel().startsWith("VB")) {
				if ("NP".equals(component.getRootLabel())) return "VO";
			} 
		}
		return "None";
	}

	@Override
	public double[] compose(LexicalizedTree tree, VectorProvider semanticSpace) {
		// TODO Auto-generated method stub
		return buildSemanticTree(tree, semanticSpace).getVector();
	}
	
	@Override
	public double[] compose(CcgTree tree, VectorProvider semanticSpace) {
		// TODO Auto-generated method stub
		return buildSemanticTree(tree, semanticSpace).getVector();
	}
	
	public LexicalizedSemanticTree buildSemanticTree(LexicalizedTree tree, VectorProvider semanticSpace) {
		if (tree.isPreTerminal()) {
			return new LexicalizedSemanticTree(tree, semanticSpace, null);
		} else {
			if (tree.isTerminal()) {
				System.out.println("Weird");
				return new LexicalizedSemanticTree(tree, semanticSpace, null);
			}
			
			LexicalizedTree headChild = getHeadChild(tree);
			Vector<Tree> newChildren = new Vector<Tree>();
			boolean hasComp = hasComposition(tree, headChild);
			double[] vector = new double[semanticSpace.getVectorSize()];
			if (hasComp) {
//				System.out.println("Fa in action");
				LexicalizedSemanticTree headSemanticChild = buildSemanticTree(headChild, semanticSpace);
				for (Tree child: tree.getChildren()) {
					if (headChild != child) {
						LexicalizedSemanticTree semanticChild = buildSemanticTree((LexicalizedTree)child, semanticSpace);
						double[] iVector = composeSingleStructure(headSemanticChild, semanticChild);
						try {
							vector = ArrayMath.sum(vector, iVector);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(1);
						}
						newChildren.add(semanticChild);
					} else {
						newChildren.add(headSemanticChild);
					}
				}
			} else {
				for (Tree child: tree.getChildren()) {
					LexicalizedSemanticTree semanticChild = buildSemanticTree((LexicalizedTree)child, semanticSpace);
					try {
						vector = ArrayMath.sum(vector, semanticChild.getVector());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						System.exit(1);
					}
					
					newChildren.add(semanticChild);
				}
			}
			
			LexicalizedSemanticTree semTree = new LexicalizedSemanticTree(tree); 
			semTree.setVector(vector);
			semTree.setChildren(newChildren);
			return semTree;
		}
		//TODO: put more things here
		
	}
	
	public LexicalizedSemanticTree buildSemanticTree(CcgTree tree, VectorProvider semanticSpace) {
		if (tree.isPreTerminal()) {
			return new LexicalizedSemanticTree(tree, semanticSpace, null);
		} else {
			if (tree.isTerminal()) {
				System.out.println("Weird");
				return new LexicalizedSemanticTree(tree, semanticSpace, null);
			}
			
			LexicalizedTree headChild = getHeadChild(tree);
			Vector<Tree> newChildren = new Vector<Tree>();
			boolean hasComp = hasComposition(tree, headChild);
			double[] vector = new double[semanticSpace.getVectorSize()];
			if (hasComp) {
//				System.out.println("Fa in action");
				LexicalizedSemanticTree headSemanticChild = buildSemanticTree(headChild, semanticSpace);
				for (Tree child: tree.getChildren()) {
					if (headChild != child) {
						LexicalizedSemanticTree semanticChild = buildSemanticTree((LexicalizedTree)child, semanticSpace);
						double[] iVector = composeSingleStructure(headSemanticChild, semanticChild);
						try {
							vector = ArrayMath.sum(vector, iVector);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(1);
						}
						newChildren.add(semanticChild);
					} else {
						newChildren.add(headSemanticChild);
					}
				}
			} else {
				for (Tree child: tree.getChildren()) {
					LexicalizedSemanticTree semanticChild = buildSemanticTree((LexicalizedTree)child, semanticSpace);
					try {
						vector = ArrayMath.sum(vector, semanticChild.getVector());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						System.exit(1);
					}
					
					newChildren.add(semanticChild);
				}
			}
			
			LexicalizedSemanticTree semTree = new LexicalizedSemanticTree(tree); 
			semTree.setVector(vector);
			semTree.setChildren(newChildren);
			return semTree;
		}
		//TODO: put more things here
		
	}
	
	protected LexicalizedTree getHeadChild(LexicalizedTree tree) {
		if (tree.isTerminal()) {
			return null;
		} else if (tree.isPreTerminal()) { 
			return (LexicalizedTree) tree.getChildren().get(0);
		} else {
			String lemma = tree.getLemma();
			if (lemma == null) return null;
			for (Tree child: tree.getChildren()) {
				if ( tree.getLemma().equals(((LexicalizedTree) child).getLemma())) {
					return (LexicalizedTree) child;
				}
			}
			return null;
		}
	}
	
	protected LexicalizedTree getHeadChild(CcgTree tree) {
		// TODO: fix here with left, right rule
		if (tree.isTerminal()) {
			return null;
		} else if (tree.isPreTerminal()) { 
			return (LexicalizedTree) tree.getChildren().get(0);
		} else {
			String lemma = tree.getLemma();
			if (lemma == null) return null;
			for (Tree child: tree.getChildren()) {
				if ( tree.getLemma().equals(((LexicalizedTree) child).getLemma())) {
					return (LexicalizedTree) child;
				}
			}
			return null;
		}
	}

//	public double[] cds(Constituent c) throws Exception {
//		double [] out = null;
//		String type = getHeadType(c); 
//		if (!type.startsWith("N") && !type.startsWith("V") && !type.startsWith("J"))
//			return null;
//		if (c instanceof SimpleConst)
//			out = fetchSemanticVector(c);
//		else {
//			Constituent head = ((ComplxConst) c).getImmediateGov();
//			VectorComposer.CompositionType compositionType = null;
//			if (type.toLowerCase().startsWith("v")) 
//				compositionType = VectorComposer.CompositionType.VN;
//			else if (type.toLowerCase().startsWith("j"))
//				compositionType = VectorComposer.CompositionType.JN;
//			else
//				compositionType = VectorComposer.CompositionType.NN;
//			for (Object o : ((ComplxConst) c).getSubConstituents()) {
//				Constituent child = (Constituent) o;
//				if (child != head)
//					out = vectorComposer.sum(out, cds(child));
//			}
//			out = vectorComposer.abComposition(cds(head), out, compositionType);
//		}
//		return out;
//	}
}
