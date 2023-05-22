/**
 * 
 */
package com.rdfpath.graph.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphNativeFullDense;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class CheckNeighbors {

	public static void main (String[] args) throws IOException {
		//if (System.getProperty("subset") == null || System.getProperty("graph") == null) {
		//	System.out.println("-Dsubset -Dgraph");
		//	return;
		//}		
		
		IGraph graph;
		int[] ids;


		String path = "subsets/";
		//String path = "subsets_old/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619};

		int index = 0;
		String actSubset = System.getProperty("subset");
		String graphName = System.getProperty("graph");
		
		index = 2;
		
		System.out.println("Indice: "+index);
		System.out.println("Subset: "+actSubset);
		System.out.println("Graph : "+graphName);
		
	
		// Obtiene números aleatorios
		System.out.println(path+files[index]+"_random_values.csv");
		BufferedReader fileBuff = Utils.readFile(path+files[index]+"_random_values.csv",false);
		String line = "";
        String[] tempArr;
        line = fileBuff.readLine();
        tempArr = line.split(",");
        ids = new int[tempArr.length];
        for (int j = 0; j < tempArr.length; j++) {
        	ids[j] = Integer.parseInt(tempArr[j]);
        }	
		
        graph = null;
        System.out.println("Empieza grafo1");
        graph = new Graph(path + files[index] + end, true);
        ArrayList graphAns = graph.checkAdj(ids);
        graph = null;
        
        System.out.println("Empieza grafo2");
		graph = new GraphNative(path + files[index] + end, true, edgesSize[index]);
		ArrayList graphNativeAns = graph.checkAdj(ids);
		
		System.out.println("Empieza grafo3");
		graph = new GraphCompDense(path + files[index] + endComp, true, nodesSize[index]);
		ArrayList graphCompDenseAns = graph.checkAdj(ids);
		
		System.out.println("Empieza grafo4");
		graph = new GraphComp(path + files[index] + endComp, true, maxNodeId[index]);
		ArrayList graphCompAns = graph.checkAdj(ids);
		
		System.out.println("Empieza grafo5");
		graph = new GraphFullNative(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], maxNodeId[index]);
		ArrayList graphFullNatAns = graph.checkAdj(ids);
		
		System.out.println("Empieza grafo6");
		graph = new GraphNativeFullDense(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], nodesSize[index]);
		ArrayList graphFullNatDenseAns = graph.checkAdj(ids);
		
		System.out.println("Compara valores");
		for (int j = 0; j<1000;j++) {
			System.out.println(graphAns.get(j).equals(graphNativeAns.get(j)) &&
					graphCompDenseAns.get(j).equals(graphCompAns.get(j)) &&
					graphFullNatAns.get(j).equals(graphFullNatDenseAns.get(j)));
			
			if (!(
					graphAns.get(j).equals(graphNativeAns.get(j)) &&
					graphCompDenseAns.get(j).equals(graphCompAns.get(j)) &&
					graphFullNatAns.get(j).equals(graphFullNatDenseAns.get(j))
					)) {
				System.out.println(j);
				System.out.println(graphAns.get(j).equals(graphNativeAns.get(j)));
				System.out.println(graphCompDenseAns.get(j).equals(graphCompAns.get(j)));
				System.out.println(graphFullNatAns.get(j).equals(graphFullNatDenseAns.get(j)));
				System.out.println("");
			}
			
		}
		
			
			
			
		
	}
}
