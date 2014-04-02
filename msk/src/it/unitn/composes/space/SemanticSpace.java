package it.unitn.composes.space;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import it.uniroma2.util.math.ArrayMath;
import it.uniroma2.util.vector.RandomVectorGenerator;
import it.uniroma2.util.vector.VectorProvider;

public class SemanticSpace implements VectorProvider{
    public enum SemanticVectorFileType {dense,sparse};
    private Hashtable<String,double[]> distributionalSemanticDictionary;
    private int vectorSize;
    private RandomVectorGenerator rtg;
    
    boolean plusOne = false;
    
    public SemanticSpace(int vectorSize, File semanticVectors){
        this.vectorSize = vectorSize;
        this.distributionalSemanticDictionary = new Hashtable<String,double[]>();
        this.loadVectors(null, semanticVectors, SemanticVectorFileType.dense);
        this.rtg = new RandomVectorGenerator(vectorSize);
    }

    public SemanticSpace(int vectorSize, File semanticVectors, boolean plusOne){
        this.vectorSize = vectorSize;
        this.distributionalSemanticDictionary = new Hashtable<String,double[]>();
        this.loadVectors(null, semanticVectors, SemanticVectorFileType.dense);
        this.rtg = new RandomVectorGenerator(vectorSize);
        this.plusOne = plusOne;
    }
    
    
    public double[] getVector(String word) throws Exception{
        
        //if set to true the returned vector is (1, v) where v is the original (normalized) distributional vector
        double[] v = new double[vectorSize];
        if (word == null){
            // if the node doesn't have a word attached
        	v = ArrayMath.versor(ArrayMath.zeros(vectorSize));
//            v = ArrayMath.versor(ArrayMath.ones(vectorSize));        
        } else if (distributionalSemanticDictionary.get(word.toLowerCase()) == null){
                // if it can't find the word (personal names, particles and so on)
                //v = ArrayMath.versor(rtg.getVector(word));
        	v = ArrayMath.versor(ArrayMath.zeros(vectorSize));
        } //Looking for the term vector in cache
        else if (distributionalSemanticDictionary != null && distributionalSemanticDictionary.get(word.toLowerCase()) != null){        
                v = ArrayMath.versor(distributionalSemanticDictionary.get(word.toLowerCase()));
        } else {
            //System.out.println(distributionalSemanticDictionary);
//            v = ArrayMath.versor(ArrayMath.ones(vectorSize));
        	v = ArrayMath.versor(ArrayMath.ones(vectorSize));
        }
    
        if (plusOne){
            double[] x = new double[vectorSize + 1];
            x[0] = 1;
            for (int i = 1; i<vectorSize; i++){
                x[i] = v[i];
            }
            return x;
        } else{
            return v;
        }
    }
    
    public int getVectorSize() {
        return vectorSize;
    }
    
    /**
     * Scans the vectors file and load all the terms found in the term list;
     * upon finding and loading a term, the term is deleted from the list
     * @param termsToLoad the list of terms to load
     * @param vectorsFile the semantic vectors file to scan
     * @throws Exception
     */
    private void loadVectors(ArrayList<String> termsToLoad, File vectorsFile, SemanticVectorFileType type) {
        //Scanning the vector file
    	try {
	        BufferedReader input = new BufferedReader(new FileReader(vectorsFile));
	        if (type == SemanticVectorFileType.dense)
	            //Skip the first line (features)
	            input.readLine();
	        String line = input.readLine();
	        while (line != null)
	        {
	            if (type == SemanticVectorFileType.dense)
	            {
	                //The first string is the term, the following ones are the vector values
	                String[] values = line.split("( |\\t)");
	                //Checking that the term has no blanks!
	                int vectorStart;
	                for (vectorStart=1; vectorStart<values.length-1; vectorStart++) {
	                    try {
	                    	Double.parseDouble(values[vectorStart]);
	                    } catch(Exception e) {
	                    	continue;
	                    }
	                    break;
	                }
	                String term = values[0].toLowerCase();
	                //System.out.print("." + term);
	                for (int i=1; i<vectorStart; i++)
	                    term += " "+values[i];
	                if (termsToLoad == null || termsToLoad.contains(term))
	                {
	                    int size = values.length - vectorStart;
	                    if (vectorSize != 0 && vectorSize <= size)
	                        size = vectorSize;
	                    else if (vectorSize != 0)
	                        System.out.println("Warning! "+vectorSize+" features required, but only "+size+" available!");
	                    double[] semanticVector = new double[size];
	                    for (int i=vectorStart; i<size+vectorStart; i++)
	                        semanticVector[i-vectorStart] = Double.parseDouble(values[i]);
	                    distributionalSemanticDictionary.put(term, semanticVector);
	                    if (termsToLoad != null) termsToLoad.remove(term);
	                }
	            }
	            else
	            {
	                String[] values = line.split("\t");
	                if (termsToLoad == null || termsToLoad.contains(values[0].substring(0, values[0].length()-1)))
	                {
	                    double[] semanticVector = new double[vectorSize];
	                    for (int i=1; i<values.length; i++)
	                    {
	                        String[] indexValue = values[i].split(":");
	                        if (indexValue.length != 2)
	                            System.out.println("Warning! Wrong format for feature:value in term "+values[0]);
	                        if (Integer.valueOf(indexValue[0]) > vectorSize)
	                            break;
	                        semanticVector[Integer.valueOf(indexValue[0])-1] = Double.parseDouble(indexValue[1]);
	                    }
	                    distributionalSemanticDictionary.put(values[0].substring(0, values[0].length()-1), semanticVector);
	                    if (termsToLoad != null) termsToLoad.remove(values[0].substring(0, values[0].length()-1));
	                }
	            }
	            line = input.readLine();
	        }
	        input.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    } 
}
