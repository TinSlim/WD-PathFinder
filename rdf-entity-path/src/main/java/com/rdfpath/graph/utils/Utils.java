package com.rdfpath.graph.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.rdfpath.graph.model.Edge;

public class Utils {
	
	public Utils () {
	}
	
	public static void saveGraph (ArrayList<Edge> edges, String filename) throws IOException {
		System.out.println(filename);
		FileWriter myWriter = new FileWriter(filename);
		for (Edge e : edges) {
			myWriter.write(
					"<http://www.wikidata.org/entity/Q" + Integer.toString(e.getOrigin().getId()) + "> " +
					"<http://www.wikidata.org/prop/direct/P" + Integer.toString(e.getId()) + "> " +
					"<http://www.wikidata.org/entity/Q" + Integer.toString(e.getDestination().getId()) + "> .\n");
		}
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
	}
}
