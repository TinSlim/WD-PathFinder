/**
 * 
 */
package com.rdfpath.graph.main;

import java.io.IOException;

import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.wrapper.GraphWrapperServer;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class BorrarAlgoritmoPrueba {
	
	public static void main (String[] args) throws IOException {
		String path = "subsetsBorrar/";
		IGraph graph = new GraphComp(path + "ciclo2", false, 4);
		GraphWrapperServer gw2 = new GraphWrapperServer(graph);
		int[] a = {2,4};
		//int[] b = a + 2;
		gw2.search(a,5);
	}
}
