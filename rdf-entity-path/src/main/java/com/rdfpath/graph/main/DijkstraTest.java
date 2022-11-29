package com.rdfpath.graph.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.rdfpath.graph.algorithms.Dijkstra;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

public class DijkstraTest {
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static void main(String[] args) throws IOException{
		long startTimeCreatingGraph = System.currentTimeMillis();
		
		String filename = "/test.nt";//"./resources/subset100000.nt";
		Graph graph = new Graph(filename);
		
		long endTimeCreatingGraph = System.currentTimeMillis();
		long durationCreatingGraph = (endTimeCreatingGraph - startTimeCreatingGraph);
		
		System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", durationCreatingGraph, endTimeCreatingGraph, startTimeCreatingGraph );
		//System.out.println("Human-Readable format : "+TimeParser.millisToShortDHMS( durationCreatingGraph ) );
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		// Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory));

	    
		Dijkstra dijkstra = new Dijkstra(graph);
		
		// subset10000
		// Vertex start = nodes.get(1001);
		// Vertex end = nodes.get(352);
		Vertex start = nodes.get(1);
		Vertex end = nodes.get(8);
		System.out.println(start);
		System.out.println(end);
		
		LinkedList<Vertex> path = dijkstra.executeBaseline(start, end).getPath();
        System.out.println("Path length: "+path.size());

        Vertex before = null;
        for (Vertex vertex : path) {
        	if(before != null)
        		System.out.println("edge "+before.getEdge(vertex, false).getId());
            System.out.println("node "+vertex.getId());
            before = vertex;
        }
		
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
//	    System.out.println("Used memory is bytes: " + memory2);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory2));
	}
}