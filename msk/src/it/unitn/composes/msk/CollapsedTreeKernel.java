package it.unitn.composes.msk;

import java.util.HashMap;
import java.util.Vector;
import java.lang.Math;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.uniroma2.util.math.ArrayMath;
import it.unitn.composes.composition.BasicComposition;
import it.unitn.composes.tree.LexicalizedSemanticTree;
import it.unitn.composes.utils.StructureUtils;


public class CollapsedTreeKernel implements KernelFunction<Tree>{

	private double lambda = 0.4;
//	private boolean lexicalized = true;
	protected VectorProvider semanticSpace;
	protected BasicComposition composition; 
	
	public CollapsedTreeKernel(double lambda, VectorProvider semanticSpace, 
			BasicComposition composition) {
		this.composition = composition;
		this.lambda = lambda;
		this.semanticSpace = semanticSpace;
	}
	
	public CollapsedTreeKernel() {
		semanticSpace = null;
		composition = null;
	}
	
//	public void setLexicalized(boolean lexicalized) {
//		this.lexicalized = lexicalized;
//	}
	
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public void setSemanticSpace(VectorProvider semanticSpace) {
		this.semanticSpace = semanticSpace;
	}
	
	public void setComposition(BasicComposition composition) {
		this.composition = composition;
	}
	
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return value(arg0, arg1);
	}
	
	public double value(Tree arg0, Tree arg1) {
		LexicalizedSemanticTree tree1 = new LexicalizedSemanticTree(LexicalizedTree.transform(arg0), semanticSpace, composition);
		LexicalizedSemanticTree tree2 = new LexicalizedSemanticTree(LexicalizedTree.transform(arg1), semanticSpace, composition);
		tree1.setParent(null);
		tree2.setParent(null);
		double value = value(tree1, tree2);
		System.out.println("*****************************");
		System.out.println("Tree1: " + tree1.toPennTree());
		System.out.println("Tree2: " + tree1.toPennTree());
		System.out.println("value: " + value);
		
		return value;
	}
	
	public double value(LexicalizedSemanticTree tree1, LexicalizedSemanticTree tree2) {
		Vector<LexicalizedSemanticTree> nodes1 = tree1.getAllNodes();
//		System.out.println(nodes1.size());
		
		Vector<LexicalizedSemanticTree> nodes2 = tree2.getAllNodes();
//		System.out.println(nodes2.size());
		
		double[][] deltaMatrix = computeDeltaMatrix(nodes1,nodes2);
		double sum = 0;
		for (double[] row: deltaMatrix) {
			for (double cell: row) {
				sum += cell;
			}
		}
		return sum;
	}
	
	
	
	protected double[][] computeDeltaMatrix(Vector<LexicalizedSemanticTree> nodes1, Vector<LexicalizedSemanticTree> nodes2) {
		HashMap<LexicalizedSemanticTree,Integer> nodeIndices1 = StructureUtils.vector2HashMap(nodes1);
		HashMap<LexicalizedSemanticTree,Integer> nodeIndices2 = StructureUtils.vector2HashMap(nodes2);
		double[][] deltaMatrix = new double[nodes1.size()][nodes2.size()];
		for (LexicalizedSemanticTree node1: nodes1) {
			for (LexicalizedSemanticTree node2: nodes2) {
				deltaMatrix[nodeIndices1.get(node1)][nodeIndices2.get(node2)] =
						computeDelta(deltaMatrix,node1,node2,nodeIndices1,nodeIndices2);
			}
		}
		return deltaMatrix;
	}
	
	protected double computeDelta(double[][] deltaMatrix, LexicalizedSemanticTree node1, LexicalizedSemanticTree node2,
			HashMap<LexicalizedSemanticTree,Integer> nodeIndices1,HashMap<LexicalizedSemanticTree,Integer> nodeIndices2) {
		try {
			double delta = 0;
			if (node1.isTerminal() || node2.isTerminal()) {
				delta = 0;
			} else if (node1.isPreTerminal() && node2.isPreTerminal() && node1.getRootLabel().equals(node2.getRootLabel()) 
					&& (node1.getChildren().get(0).getRootLabel().equals(node2.getChildren().get(0).getRootLabel()))){
					delta = 1;
				
			} else if (!productionCompare(node1, node2)) {
	            if (!node1.getRootLabel().equals(node2.getRootLabel())) {
	                delta = 0;
	            } else {
	                delta = (Math.pow(this.lambda, (Math.max(node1.getHeight(),node2.getHeight()) - 1))) * 
	                                                                 ArrayMath.cosine(node1.getVector(), node2.getVector());
//	                for (double value: node1.getVector()) {
//	                	System.out.print(" " + value);
//	                }
//	                System.out.println();
//	                for (double value: node2.getVector()) {
//	                	System.out.print(" " + value);
//	                }
//	                System.out.println();
//	                System.out.println(ArrayMath.cosine(node1.getVector(), node2.getVector()));
	            }
			} else {
	            double product_children_delta = this.lambda; 
	            for (int i = 0; i < node1.getChildren().size(); i++) {
	            	LexicalizedSemanticTree child1 = (LexicalizedSemanticTree) node1.getChildren().get(i);
	            	LexicalizedSemanticTree child2 = (LexicalizedSemanticTree) node2.getChildren().get(i);
	                double child_delta = deltaMatrix[nodeIndices1.get(child1)][nodeIndices2.get(child2)];
	                if (child_delta == -1) {
	                	System.out.println("Error");
	                } else {
	                    product_children_delta *= (1 + child_delta);
	                }
	            }
	            double sim_children_product = 1;
	            for (int i = 0; i < node1.getChildren().size(); i++) {
	            	LexicalizedSemanticTree child1 = (LexicalizedSemanticTree) node1.getChildren().get(i);
	            	LexicalizedSemanticTree child2 = (LexicalizedSemanticTree) node2.getChildren().get(i);
	                sim_children_product *= ArrayMath.cosine(child1.getVector(), child2.getVector());
	            }
	            delta = (product_children_delta + 
	                           (Math.pow(this.lambda, (Math.max(node1.getHeight(),node2.getHeight()) - 1))) * 
	                            (ArrayMath.cosine(node1.getVector(), node2.getVector()) - 
	                             sim_children_product));
			}
	        return delta;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private static boolean productionCompare(Tree a, Tree b) {
		if (!a.getRootLabel().equals(b.getRootLabel()))
			return false;
		if (a.getChildren().size() != b.getChildren().size() || a.getChildren().size() == 0)
			return false;
		for (int i=0; i<a.getChildren().size(); i++)
			if (!a.getChildren().get(i).getRootLabel().equals(b.getChildren().get(i).getRootLabel()))
				return false;
		return true;
	} 

}
