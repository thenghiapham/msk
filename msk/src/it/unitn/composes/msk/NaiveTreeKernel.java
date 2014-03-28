package it.unitn.composes.msk;

import java.util.HashMap;
import java.util.Vector;

import it.uniroma2.util.math.ArrayMath;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.BasicComposition;
import it.unitn.composes.tree.SemanticTree;
import it.unitn.composes.utils.StructureUtils;
import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;

public class NaiveTreeKernel implements KernelFunction<Tree> {
	private double lambda = 0.4;
	protected VectorProvider semanticSpace;
	protected BasicComposition composition; 
	
	public NaiveTreeKernel(double lambda, VectorProvider semanticSpace, 
			BasicComposition composition) {
		this.composition = composition;
		this.lambda = lambda;
		this.semanticSpace = semanticSpace;
	}
	
	public NaiveTreeKernel() {
		semanticSpace = null;
		composition = null;
	}
	
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
		SemanticTree tree1 = new SemanticTree(arg0, semanticSpace, composition);
		SemanticTree tree2 = new SemanticTree(arg1, semanticSpace, composition);
		tree1.setParent(null);
		tree2.setParent(null);
		return value(tree1, tree2);
	}
	
	public double value(SemanticTree tree1, SemanticTree tree2) {
		Vector<SemanticTree> nodes1 = tree1.getAllNodes();
//		System.out.println(nodes1.size());
		
		Vector<SemanticTree> nodes2 = tree2.getAllNodes();
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
	
	protected double[][] computeDeltaMatrix(Vector<SemanticTree> nodes1, Vector<SemanticTree> nodes2) {
		HashMap<SemanticTree,Integer> nodeIndices1 = StructureUtils.vector2HashMap(nodes1);
		HashMap<SemanticTree,Integer> nodeIndices2 = StructureUtils.vector2HashMap(nodes2);
		double[][] deltaMatrix = new double[nodes1.size()][nodes2.size()];
		for (SemanticTree node1: nodes1) {
			for (SemanticTree node2: nodes2) {
				deltaMatrix[nodeIndices1.get(node1)][nodeIndices2.get(node2)] = delta(node1, node2);
						
			}
		}
		return deltaMatrix;
	}
	
	protected double delta(SemanticTree node1, SemanticTree node2) {
		double delta = 0;
		if (!node1.isTerminal() && !node2.isTerminal() && node1.getRootLabel().equals(node2.getRootLabel())) {
			try {
			delta = Math.pow(lambda, ((node1.getHeight() + node2.getHeight() - 2) )) 
                * ArrayMath.cosine(node1.getVector(), node2.getVector());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return delta;
	}
}
