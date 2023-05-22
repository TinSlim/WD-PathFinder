package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.web.socket.WebSocketSession;

public class Graph extends AbstractGraph {
	private HashMap<Integer, Vertex> nodes;

	public Graph (String filename, Boolean isGz) throws IOException {
		structName = "graphGt";

		nodes = new HashMap<Integer, Vertex>();
		BufferedReader fileBuff = readFile(filename, isGz);

		String line = "";
        String[] tempArr;
        
        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		
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

    		Edge edge = new Edge(predicateID, subjectNode, objectNode);
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

	@Override
	public HashSet<Integer> getAdjacentVertex(int id) {
		HashSet<Integer> nList = nodes.get(id).getAdjacentVertex();
		return nList;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException {
		HashSet<Integer> nList = nodes.get(id).getAdjacentVertexSession(session);
		return nList;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException {
		HashSet<Integer> nList = nodes.get(id).getAdjacentVertexTimeout(seconds, startTime);
		return nList;
	}

	@Override
	public ArrayList<Edge> getEdges(int idVertex, int idVertex2) {
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

	@Override
	public int getPredicateEdge(Object e) {
		Edge edgeF = (Edge) e;
		return edgeF.id;
	}
}