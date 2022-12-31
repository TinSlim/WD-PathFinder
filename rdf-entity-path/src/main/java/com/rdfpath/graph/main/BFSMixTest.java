package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.rdfpath.graph.algorithms.BFSMix;
import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;
import com.rdfpath.graph.utils.Utils;

public class BFSMixTest {

	private static final long MEGABYTE = 1024L * 1024L;
	
	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static void main(String[] args) throws IOException{
		long startTimeCreatingGraph = System.currentTimeMillis();
		
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		String savePath = "src/main/resources";
		
		Graph graph = new Graph(filename,false);
		
		long endTimeCreatingGraph = System.currentTimeMillis();
		long durationCreatingGraph = (endTimeCreatingGraph - startTimeCreatingGraph);
		
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", durationCreatingGraph, endTimeCreatingGraph, startTimeCreatingGraph );
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));

		BFSMix bfsAlg = new BFSMix(graph);
		
		Integer[] nodesNumbers = {18,20,19};
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		String newName = "";
		for (Integer i : nodesNumbers) {
			listNodes.add(nodes.get(i));
			newName = String.join("_",newName,Integer.toString(i));
		}
		
		bfsAlg.setSearchNodes(listNodes);
		//bfsAlg.getRoads(66);
		ArrayList<Edge> edges = bfsAlg.getRoadsOnline(3);
		System.out.println("-------------");
		// roads
		// [5->6->10, 10->13->11, 9->8->10, 17->21->10, 11->14->12, 16->20->17, 18->23->17, 3->4->5, 5->7->9, 16->19->12, 15->18->16, 16->22->18, 12->17->15]
		// [5->6->10, 10->13->11, 3->4->5, 11->14->12, 5->7->9, 16->20->17, 17->21->10, 16->19->12, 15->18->16, 9->8->10, 12->17->15, 16->22->18]
		Utils.saveGraph(edges,savePath + filename.replace(".nt", newName + ".nt"));
		
		
	    long endTime = System.currentTimeMillis();
		long duration = (endTime - endTimeCreatingGraph);
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", duration, endTimeCreatingGraph, endTime );
		long durationTotal = (endTime - startTimeCreatingGraph);  
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", durationTotal, startTimeCreatingGraph, endTime );
		//System.out.println("Human-Readable format : "+TimeParser.millisToShortDHMS( durationTotal ) );
		
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory2 = runtime.totalMemory() - runtime.freeMemory() - memory;
	    // System.out.println("Used memory is bytes: " + memory2);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory2));
	}
}