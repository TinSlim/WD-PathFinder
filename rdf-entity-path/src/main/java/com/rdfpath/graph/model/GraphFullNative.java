package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.web.socket.WebSocketSession;

/**
*
* @author Cristóbal Torres G.
* @github Tinslim
*
*/

public class GraphFullNative extends AbstractGraph {
	
	private int[][] edges;
	private int[][] nodes2;
	
	public GraphFullNative (String filename, String filename2, Boolean isGz, Boolean isGz2, int edgesSize, int nodesSize) throws IOException {
		structName = "graphNativeFull";
		edges = new int[edgesSize][3];
		nodes2 = new int[nodesSize + 1][];
		
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
    		
    		// Añade arista
    		edges[countedStatements][0] = subjectID;
    		edges[countedStatements][1] = predicateID;
    		edges[countedStatements][2] = objectID;
    		edgesLoaded += 1; //Cuenta arista
    		countedStatements++;
 
        }
        fileBuff.close();
        
        
        fileBuff = readFile(filename2, isGz2);

		line = "";
        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		//timeA = System.currentTimeMillis();
    		//sendNotificationTime(10,"Edges: " + edgesLoaded);
    		
    		tempArr = line.split(" ");									// line 	= "18 22 16 32 23"
    		int[] numbers = new int[tempArr.length - 1];				// temArr 	= {"18", "22", "16", "32", "23"}
    		int id = Integer.parseInt(tempArr[0]);						// id		= 18
    		
    		for (int j=1;j<tempArr.length;j++) {
    			numbers[j - 1] = Integer.parseInt(tempArr[j]);			// numbers = {22, 16, 32, 23}
    		}
    		nodes2[id] = numbers;
        }
        fileBuff.close();
		return;
    }

	@Override
	public HashSet<Integer> getAdjacentVertex(int id) {
		int[] edgesOfV = nodes2[id];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeId : edgesOfV) {
			int adjID = (edges[edgeId][0] == id) ? edges[edgeId][2] : edges[edgeId][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	public HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException {
		int[] edgesOfV = nodes2[id];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeId : edgesOfV) {
			checkTime(seconds,startTime);
			int adjID = (edges[edgeId][0] == id) ? edges[edgeId][2] : edges[edgeId][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException {
		int[] edgesOfV = nodes2[id];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeId : edgesOfV) {
			checkConn(session);
			int adjID = (edges[edgeId][0] == id) ? edges[edgeId][2] : edges[edgeId][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	@Override
	public ArrayList<Integer> getEdges(int idVertex, int idVertex2) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		//Boolean lessEdges = nodes2[idVertex].length > nodes2[idVertex2].length;
		//int [] vFrom = lessEdges ? nodes2[idVertex2] : nodes2[idVertex];
		//int vTo = lessEdges ? idVertex : idVertex2;
		for (int edgeAdj : nodes2[idVertex]) {
			if (edges[edgeAdj][0] == idVertex2 || edges[edgeAdj][2] == idVertex2) {
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
		int idEdge = (int) e;
    	return edges[idEdge][1];
	}

	
	public int getGrade (int idVertex) {
		int[] edgesOfV = nodes2[idVertex];
		return edgesOfV.length;
	}
	

}