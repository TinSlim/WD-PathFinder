/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.json.JSONArray;
import org.json.JSONObject;

import com.rdfpath.graph.main.ParseExample;
import com.rdfpath.graph.utils.GraphCounterNative;
import com.rdfpath.graph.utils.StatementCounter;
import com.rdfpath.graph.utils.Utils;

/**
*
* @author Cristóbal Torres G.
* @github Tinslim
*
*/
public class GraphNative extends AbstractGraph {

    private HashMap<Integer, LinkedList<Integer>> nodes;
	private int[][] edges;


	public GraphNative (String filename, Boolean isGz) throws IOException {
		printMemory();

		
		if (isGz) {
			FileInputStream stream = new FileInputStream(filename);
			GZIPInputStream gzip = new GZIPInputStream(stream);
			BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
			
			RDFParser parser = Rio.createParser(RDFFormat.NTRIPLES);
			GraphCounterNative myCounter = new GraphCounterNative(462570);
			parser.setRDFHandler(myCounter);
			
			try {
				parser.parse(br, "");
			}
			catch (Exception e) {
				//throw new IOException(e);
				System.out.println("ERROR:STACKTRACE::"); // TODO
				e.printStackTrace();
				System.out.println("ERROR:_______::"); // TODO
				System.out.println(e);
			}
			this.nodes = myCounter.getNodes();
			this.edges = myCounter.getEdges();
			myCounter.printCounters();
			return;
		}
		else {
			RDFParser parser = Rio.createParser(Rio.getParserFormatForFileName(filename).orElse(RDFFormat.NTRIPLES));
			GraphCounterNative myCounter = new GraphCounterNative(462570);
			parser.setRDFHandler(myCounter);
			
			//RDFWriter writer = Rio.createWriter(RDFFormat.RDFJSON, System.out);
			//parser.setRDFHandler(writer);
			try {
				parser.parse(ParseExample.class.getResourceAsStream(filename), "");
			}
			catch (Exception e) {
				throw new IOException(e);
			}
			this.nodes = myCounter.getNodes();
			this.edges = myCounter.getEdges();
			myCounter.printCounters(); 
			return;
		}
		 
    }

	@Override
	public CharSequence edgeToJson(Object e, ArrayList<Integer> vList) {
    	String message;
    	int idEdge = (int) e;
    	JSONObject json = new JSONObject();
    	
    	String edgeLabel = Utils.getEntityName("P"+edges[idEdge][1] +"&type=property");
    	String edgeLabelSmall = edgeLabel;
    	if (edgeLabel.length() > 7) {edgeLabelSmall = edgeLabel.substring(0,Math.min(edgeLabel.length(), 7)) + "...";}
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", edges[idEdge][0]);
    	edge.put("to", edges[idEdge][2]);
    	//edge.put("label", "K"+id);
    	edge.put("label", edgeLabelSmall);//Utils.getEntityName("P" + id));
    	edge.put("title", edgeLabel);
    	edge.put("font", new JSONObject().put("align", "middle"));
    	edge.put("color", new JSONObject().put("color", "#848484"));
    	edge.put("arrows", new JSONObject().put("to", new JSONObject().put("enabled", true).put("type", "arrow")));
    	edge.put("length", 500);
    	// Vertex
    	JSONArray vertexArray = new JSONArray();
    	for (Integer v : vList) {
    		String color = "#97C2FC";
    		//String color = (v.father == v) ? "#cc76FC" : "#97C2FC";
    		String vertexLabel = Utils.getEntityName("Q" + v);
        	String vertexLabelSmall = vertexLabel;
        	if (vertexLabel.length() > 7) {vertexLabelSmall = vertexLabel.substring(0,Math.min(vertexLabel.length(), 7)) + "...";}
    		vertexArray.put(
    				new JSONObject()
    				.put("id", v)
    				//.put("label",Utils.getEntityName("Q" + v) + "_" + v)
    				.put("label", vertexLabelSmall)
    				.put("title", vertexLabel)
    				.put("color",color));
    	}
    	json.put("edge", edge);
    	json.put("vertex", vertexArray);
    	message = json.toString();
    	
    	return message;
    }

	@Override
	public List<Integer> getAdjacentVertex(int id) {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		ArrayList<Integer> adjVL = new ArrayList<Integer>();
		for (Integer ig : edgesOfV) {
			int adjID = (edges[ig][0] == id) ? edges[ig][2] : edges[ig][0]; 
			adjVL.add(adjID);
		}
		
		return adjVL;
	}

	@Override
	public ArrayList getEdges(int idVertex, int idVertex2) {
		ArrayList<Integer> result = new ArrayList();
		Boolean lessEdges = nodes.get(idVertex).size() > nodes.get(idVertex2).size();
		List<Integer> vFrom = lessEdges ? nodes.get(idVertex2) : nodes.get(idVertex);
		int vTo = lessEdges ? idVertex : idVertex2;
		for (int edgeAdj : vFrom) {
			if (edges[edgeAdj][0] == vTo || edges[edgeAdj][2] == vTo) {
				result.add(edgeAdj);
			}
		}
		return result;
	}

	@Override
	public int getOriginEdge(Object e) {
		Integer edgeNum = (Integer) e;
		return edges[edgeNum][0];
	}

	@Override
	public int getDestinationEdge(Object e) {
		Integer edgeNum = (Integer) e;
		return edges[edgeNum][2];
	}

}