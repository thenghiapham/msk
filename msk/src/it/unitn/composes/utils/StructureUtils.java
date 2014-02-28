package it.unitn.composes.utils;

import it.unitn.composes.tree.SemanticTree;

import java.util.HashMap;
import java.util.Vector;

public class StructureUtils {
	public static HashMap<SemanticTree,Integer> vector2HashMap(Vector<SemanticTree> nodes) {
		HashMap<SemanticTree, Integer> result = new HashMap<SemanticTree,Integer>();
		for (int i=0; i < nodes.size(); i++) {
			result.put(nodes.elementAt(i), i);
		}
		return result;
	}
}
