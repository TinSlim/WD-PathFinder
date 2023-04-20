package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphWrapper;
import com.rdfpath.graph.model.GraphWrapper2;
import com.rdfpath.graph.model.GraphWrapper3;
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
		
		String path = "subsetsBorrar/";//"subsets/"
		String filename = "triple"; // "subset1000000";  
		String end = ".nt";// ".nt.gz";
		
		System.out.println("Loading Graph");
		IGraph graph = new Graph(path + filename + end, false);
		System.out.println("GraphLoaded");
		int[] nodesNumbers = {2,6,9}; //{261, 298};
		//GraphWrapperTest graphWrapper = new GraphWrapperTest(graph);
		//graphWrapper.allPathsNew(nodesNumbers, 6);
		
		GraphWrapper2 graphWrapper = new GraphWrapper2(graph);
		graphWrapper.search(nodesNumbers, 6);
	}
}
