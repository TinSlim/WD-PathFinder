package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphWrapper;
import com.rdfpath.graph.model.GraphWrapperTest;
import com.rdfpath.graph.model.IGraph;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class PathExample {
	public static void main (String[] args) throws IOException {
		
		String path = "subsets/";
		String filename = "little";
		String end = ".nt";
		
		IGraph graph = new Graph(path + filename + end, false);
		int[] nodesNumbers = {3,1}; 
		GraphWrapperTest graphWrapper = new GraphWrapperTest(graph);
		System.out.println(graphWrapper.search(nodesNumbers, 6));
	}
	
}
