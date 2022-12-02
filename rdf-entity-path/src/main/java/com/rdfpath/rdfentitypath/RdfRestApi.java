package com.rdfpath.rdfentitypath;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdfpath.graph.algorithms.BFSMix;
import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

@RestController
public class RdfRestApi {
	Graph graph = null;

	@RequestMapping("/querylol")
	public String home() throws IOException {//throws IOException {
		Vertex a = new Vertex (2);
		System.out.println(a);
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		graph = new Graph(filename); // TODO esta linea pide IOExcept
		System.out.println(graph.getNodes().get(1).myFather);
		System.out.println("graph2");
		
		BFSMix bfsAlg = new BFSMix(graph);
		
		Integer[] nodesNumbers = {18,20,19};
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		String newName = "";
		for (Integer i : nodesNumbers) {
			listNodes.add(graph.getNodes().get(i));
			newName = String.join("_",newName,Integer.toString(i));
		}
		
		bfsAlg.setSearchNodes(listNodes);
		ArrayList<Edge> edges = bfsAlg.getRoadsOnline(3);
		System.out.println(edges);
		System.out.println("-------------");
	    return "---beser--";
	}
}
