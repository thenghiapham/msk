package it.unitn.composes.utils;


import java.util.HashMap;
import java.util.Vector;

public class StructureUtils {
	public static<E> HashMap<E,Integer> vector2HashMap(Vector<E> nodes) {
		HashMap<E, Integer> result = new HashMap<E,Integer>();
		for (int i=0; i < nodes.size(); i++) {
			result.put(nodes.elementAt(i), i);
		}
		return result;
	}
	
	
}
