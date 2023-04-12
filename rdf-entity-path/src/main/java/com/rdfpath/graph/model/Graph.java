package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.rdfpath.graph.utils.Utils;

public class Graph extends AbstractGraph {
	private HashMap<Integer, Vertex> nodes;

	public Graph (String filename, Boolean isGz) throws IOException {
		structName = "graphGt";

		nodes = new HashMap<Integer, Vertex>();
		BufferedReader fileBuff = readFile(filename, isGz);

		String line = "";
        String[] tempArr;
        int edgesLoaded = 0;
        int countedStatements = 0;
        int nodesLoaded = 0;
        
        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		//timeA = System.currentTimeMillis();

    		//sendNotificationTime(10,"Nodos: " + nodesLoaded);
    		
    		tempArr = line.split(" ");									// line 	= "<...> <...> <...> ."
    		
    		String subj = tempArr[0];
    		String pred = tempArr[1];
    		String obj = tempArr[2];

    		int objectID = Integer.parseInt(obj.substring(33, obj.length()-1));
    		int predicateID = Integer.parseInt(pred.substring(38, pred.length()-1));
    		int subjectID = Integer.parseInt(subj.substring(33, subj.length()-1));
    		
    		countedStatements++;
    		Vertex subjectNode;
    		Vertex objectNode;	

    		// Obtiene nodos si existen, sino los crea
    		if(nodes.containsKey(subjectID)){
    			subjectNode = nodes.get(subjectID);
    		} else {
    			subjectNode = new Vertex(subjectID);
    			nodes.put(subjectID, subjectNode);
    			nodesLoaded += 1; //Cuenta nodos
    		}

    		if(nodes.containsKey(objectID)){
    			objectNode = nodes.get(objectID);
    		} else {
    			objectNode = new Vertex(objectID);
    			nodes.put(objectID, objectNode);
    			nodesLoaded += 1; //Cuenta nodos
    		}

    		Edge edge = new Edge(predicateID, subjectNode, objectNode, 0);
    		subjectNode.addEdge(edge);
    		objectNode.addEdge(edge);
    		edgesLoaded += 1;
 
        }
        fileBuff.close();
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
	public HashSet<Integer> getAdjacentVertex(int id) {
		HashSet<Integer> nList = nodes.get(id).getAdjacentVertex();
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