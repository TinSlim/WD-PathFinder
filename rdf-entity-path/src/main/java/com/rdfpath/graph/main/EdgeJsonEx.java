package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.rdfpath.graph.algorithms.BFSMix;
import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

public class EdgeJsonEx {
	public static void main(String[] args) throws IOException{
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		Graph graph = new Graph(filename,false);
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		BFSMix bfsAlg = new BFSMix(graph);
		
		Integer[] nodesNumbers = {12,25};
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		String newName = "";
		for (Integer i : nodesNumbers) {
			listNodes.add(nodes.get(i));
			newName = String.join("_",newName,Integer.toString(i));
		}
		
		bfsAlg.setSearchNodes(listNodes);
		ArrayList<Edge> edges = bfsAlg.getRoads(3);
		//for (Edge e : edges) {
		//	System.out.println(e.toJson());
		//}
	}
}
