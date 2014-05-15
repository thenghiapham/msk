package it.unitn.composes.msk;

import java.io.File;

import edu.berkeley.compbio.jlibsvm.kernel.KernelFunction;
import it.uniroma2.util.tree.LexicalizedTree;
import it.uniroma2.util.tree.Tree;
import it.uniroma2.util.vector.VectorProvider;
import it.unitn.composes.composition.Additive;
import it.unitn.composes.composition.PsgSentenceComposer;
import it.unitn.composes.composition.SentenceComposer;
import it.unitn.composes.math.VectorUtils;
import it.unitn.composes.space.SemanticSpace;


public class RootVectorKernel implements KernelFunction<Tree>{
	protected SentenceComposer sentenceComposer;
	protected VectorProvider semanticSpace;
	public RootVectorKernel(SentenceComposer composer, VectorProvider semanticSpace) {
		this.sentenceComposer = composer;
		this.semanticSpace = semanticSpace;
	}
	
	public double value(Tree arg0, Tree arg1) {
		LexicalizedTree tree1 = LexicalizedTree.transform(arg0);
		LexicalizedTree tree2 = LexicalizedTree.transform(arg1);
		double[] vector1 = sentenceComposer.compose(tree1, semanticSpace);
		double[] vector2 = sentenceComposer.compose(tree2, semanticSpace);
		return VectorUtils.cosine(vector1, vector2);
	}
	
	@Override
	public double evaluate(Tree arg0, Tree arg1) {
		// TODO Auto-generated method stub
		return value(arg0, arg1);
	}
	
	public static void main(String[] args) throws Exception {
		String treeString1 = "(S#one$c (NP#Mossad$n (NNP#Mossad$n Mossad))(VP#one$c (VP#one$c (AUX#is$a is)(NP#one$c (PP#intelligence$n (IN#of$i of)(NP#intelligence$n (JJS#most$j most)(JJ#well$j well)(JJ#known$j known)(NN#intelligence$n intelligence)(NNS#agency$n agencies)))))))";
		String treeString2 = "(S#rescind$v (NP#directive$n (DT#The$d The)(NN#directive$n directive)(SBAR#stop$v (IN#for$i for)(S#stop$v (VP#stop$v (TO#to$t to)(VP#stop$v (VB#stop$v stop)(S#arrest$v (VP#arrest$v (VBG#arrest$v arresting)(NP#alien$n (JJ#illegal$j illegal)(NNS#alien$n aliens))))))))))";
		LexicalizedTree tree1 = LexicalizedTree.transform(Tree.fromPennTree(treeString1));
		LexicalizedTree tree2 = LexicalizedTree.transform(Tree.fromPennTree(treeString2));
		
		String matrixFile = "/home/pham/real-single-target.dm";
//		String matrixFile = "/home/pham/test.dm";
		String faFile = "/home/pham/fa.txt";
		SemanticSpace semanticSpace = new SemanticSpace(300, new File(matrixFile));
		CollapsedTreeKernel treeKernel1 = new CollapsedTreeKernel(0.4, semanticSpace, new Additive());
		CollapsedTreeKernel2 treeKernel2 = new CollapsedTreeKernel2(0.4, semanticSpace, new PsgSentenceComposer(faFile));
		
		System.out.println("BA:" + treeKernel1.value(tree1, tree1));
		System.out.println("BA:" + treeKernel1.value(tree2, tree2));
		System.out.println("BA:" + treeKernel1.value(tree1, tree2));
		System.out.println("FA:" + treeKernel2.value(tree1, tree1));
		System.out.println("FA:" + treeKernel2.value(tree2, tree2));
		System.out.println("FA:" + treeKernel2.value(tree1, tree2));
		
	}
}
