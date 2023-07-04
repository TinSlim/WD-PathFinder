/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

/**
*
* @author Cristóbal Torres G.
* @github Tinslim
*
*/
public class GraphNative extends AbstractGraph {

    private HashMap<Integer, LinkedList<Integer>> nodes;
	private int[][] edges;

	public GraphNative (String filename, Boolean isGz, int edgesSize) throws IOException {
		structName = "graphNative";
		nodes = new HashMap<Integer, LinkedList<Integer>>();
		edges = new int[edgesSize][3];
		
		BufferedReader fileBuff = readFile(filename, isGz);

		String line = "";
        String[] tempArr;
        
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
    		

    		// Añade Arista a los Nodos, puede crear nodos si no existen
    		if (nodes.containsKey(subjectID)) {
    			nodes.get(subjectID).add(countedStatements);
    		}
    		else {
    			LinkedList<Integer> adjList = new LinkedList<Integer>();
    			adjList.add(countedStatements);
    			nodes.put(subjectID,adjList);
    			nodesLoaded += 1;
    		}
    		if (nodes.containsKey(objectID)) {
    			nodes.get(objectID).add(countedStatements);
    		}
    		else {
    			LinkedList<Integer> adjList = new LinkedList<Integer>();
    			adjList.add(countedStatements);
    			nodes.put(objectID,adjList);
    			nodesLoaded += 1;
    		}
    		
    		// Añade arista
    		edges[countedStatements][0] = subjectID;
    		edges[countedStatements][1] = predicateID;
    		edges[countedStatements][2] = objectID;
    		edgesLoaded += 1; //Cuenta arista
    		countedStatements++;
 
        }
        fileBuff.close();
		return;
    }


	@Override
	public HashSet<Integer> getAdjacentVertex(int id) {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (Integer ig : edgesOfV) {
			int adjID = (edges[ig][0] == id) ? edges[ig][2] : edges[ig][0]; 
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	public HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (Integer ig : edgesOfV) {
			checkTime(seconds,startTime);
			int adjID = (edges[ig][0] == id) ? edges[ig][2] : edges[ig][0]; 
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (Integer ig : edgesOfV) {
			checkConn(session);
			int adjID = (edges[ig][0] == id) ? edges[ig][2] : edges[ig][0]; 
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	public HashSet<Integer> getAdjacentVertexSessionTimeout(int id, WebSocketSession session, long initTime, int limitTime) throws InterruptedException, IOException {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (Integer ig : edgesOfV) {
			checkConn(session);
			checkTime(limitTime, initTime);
			int adjID = (edges[ig][0] == id) ? edges[ig][2] : edges[ig][0]; 
			adjVL.add(adjID);
		}
		return adjVL;
	}

	@Override
	public ArrayList<Integer> getEdges(int idVertex, int idVertex2) {
		ArrayList<Integer> result = new ArrayList<Integer>();
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

	@Override
	public int getPredicateEdge(Object e) {
		Integer edgeNum = (Integer) e;
		return edges[edgeNum][1];
	}


	@Override
	public int getGrade(int idVertex) {
		return nodes.get(idVertex).size();
	}


	

}