package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.Arrays;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphWrapper;

public class AlgorithmTest {
	public static void main(String[] args) throws IOException {
		Integer[] nodesNumbers = {8,10,2};
		System.out.println("1===================");
		System.out.println(nodesNumbers);
		String filename = "/nt/star.nt";
		Graph graph = new Graph(filename);
		System.out.println("2=====================");
		GraphWrapper wrap1 = new GraphWrapper(graph);
		System.out.println("3=====================");
		wrap1.search(nodesNumbers, 3);
		System.out.println("4=====================");
	}
}
