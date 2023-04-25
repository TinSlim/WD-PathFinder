package com.rdfpath.graph.main;

import java.io.IOException;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.wrapper.GraphWrapperServer;

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
		
		GraphWrapperServer graphWrapper = new GraphWrapperServer(graph);
		graphWrapper.search(nodesNumbers, 6);
	}
}
