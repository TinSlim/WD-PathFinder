/**
 * 
 */
package com.rdfpath.graph.main;

import java.io.IOException;

import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphWrapper2;
import com.rdfpath.graph.model.IGraph;

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
		GraphWrapper2 gw2 = new GraphWrapper2(graph);
		int[] a = {1,4};
		gw2.search(a,5);
	}
}
