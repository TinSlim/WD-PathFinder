package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.json.JSONArray;
import org.json.JSONObject;

import com.rdfpath.graph.main.ParseExample;
import com.rdfpath.graph.utils.GraphCounter;
import com.rdfpath.graph.utils.GraphCounterNative;
import com.rdfpath.graph.utils.StatementCounter;
import com.rdfpath.graph.utils.Utils;

public class Graph extends AbstractGraph {

	private HashMap<Integer, Vertex> nodes;

	public Graph (String filename, Boolean isGz) throws IOException {
		
		printMemory();
		
		BufferedReader fileBuff = readFile(filename, isGz);
		RDFParser parser = Rio.createParser(RDFFormat.NTRIPLES);
		StatementCounter myCounter = new StatementCounter();
		parser.setRDFHandler(myCounter);
			
		try {
			parser.parse(fileBuff, "");
		}
		catch (Exception e) {
			System.out.println("::ERRORSTACKTRACE::");
			e.printStackTrace();
			System.out.println("::     ERROR     ::");
			System.out.println(e);
		}
		
		this.nodes = myCounter.getNodes();
		myCounter.printCounters();
		
		return;
		 
    }
	
	public Graph() {
    	this.nodes = new HashMap<Integer, Vertex>();
    }
	
    public Graph(HashMap<Integer, Vertex> nodes) {
    	this.nodes = new HashMap<Integer, Vertex>(nodes);
    }

	public void addNode(int key, Vertex node) {
        nodes.put(key, node);
    }
	
	public HashMap<Integer, Vertex> getNodes() {
        return nodes;
    }
    
	public void setNodes(HashMap<Integer, Vertex> nodes) {
        this.nodes = nodes;
    }
	
	public void printGrafo () {
		System.out.println("Grafo");
	}
	
	@Override
	public CharSequence edgeToJson (Object e, ArrayList<Integer> vertexList) {
    	Edge edgeF = (Edge) e;
		String message;
    	JSONObject json = new JSONObject();
    	
    	String edgeLabel = Utils.getEntityName("P"+edgeF.id+"&type=property");
    	String edgeLabelSmall = edgeLabel;
    	if (edgeLabel.length() > 7) {edgeLabelSmall = edgeLabel.substring(0,Math.min(edgeLabel.length(), 7)) + "...";}
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", edgeF.origin);
    	edge.put("to", edgeF.destination);
    	//edge.put("label", "K"+id);
    	edge.put("label", edgeLabelSmall);//Utils.getEntityName("P" + id));
    	edge.put("title", edgeLabel);
    	edge.put("font", new JSONObject().put("align", "middle"));
    	edge.put("color", new JSONObject().put("color", "#848484"));
    	edge.put("arrows", new JSONObject().put("to", new JSONObject().put("enabled", true).put("type", "arrow")));
    	edge.put("length", 500);
    	// Vertex
    	JSONArray vertexArray = new JSONArray();
    	for (Integer idV : vertexList) {
    		Vertex v = nodes.get(idV);
    		String color = (v.father == v) ? "#cc76FC" : "#97C2FC";
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
		List nList = nodes.get(id).getAdjacentVertex();
		int i = 0;
        while (i < nList.size()) {
            nList.set(i, ((Vertex) nList.get(i)).id);
            i++;
        }
		return nList;
	}

	@Override
	public ArrayList getEdges(int idVertex, int idVertex2) {
		return nodes.get(idVertex).getEdges(nodes.get(idVertex2));
	}

	@Override
	public int getOriginEdge(Object e) {
		Edge edgeF = (Edge) e;
		return edgeF.origin.id;
	}

	@Override
	public int getDestinationEdge(Object e) {
		Edge edgeF = (Edge) e;
		return edgeF.destination.id;
	}
}