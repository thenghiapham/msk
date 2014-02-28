package it.unitn.composes.test;

import java.io.File;

import it.unitn.composes.space.SemanticSpace;

public class SpaceTest {
	public static void main(String[] args) {
		String inputFile = "/home/pham/work/project/msk/spaces/core_ppmi_svd300.dm";
		testLoadSpace(inputFile);
		
		
		
	}
	
	public static void testLoadSpace(String inputFile) {
		System.out.println("Loading space");
		SemanticSpace space = new SemanticSpace(300, new File(inputFile));
		try {
			double[] cat = space.getVector("cat-n");
			System.out.print("cat-n: ");
			for (double value:cat) {
				System.out.print(value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("horrible design");
		}
	}
}
