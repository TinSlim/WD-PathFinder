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

public class GraphNativeFullDense extends AbstractGraph {
	
	private int[][] edges;
	private int[][] nodes2;
	
	private int nodesSize;
	
	public GraphNativeFullDense (String filename, String filename2, Boolean isGz, Boolean isGz2, int edgesSize, int nodesSize) throws IOException {
		structName = "graphNativeFullDense";
		this.nodesSize = nodesSize;

		edges = new int[edgesSize][3];
		nodes2 = new int[nodesSize][];
		
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
		int index = 0;
        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		//timeA = System.currentTimeMillis();
    		//sendNotificationTime(10,"Edges: " + edgesLoaded);
    		
    		tempArr = line.split(" ");									// line 	= "18 22 16 32 23"
    		int[] numbers = new int[tempArr.length];					// temArr 	= {"18", "22", "16", "32", "23"}
    		
    		for (int j=0;j<tempArr.length;j++) {
    			numbers[j] = Integer.parseInt(tempArr[j]);				// numbers = {18, 22, 16, 32, 23}
    		}
    		nodes2[index] = numbers;
    		index++;
        }
        fileBuff.close();
		return;
    }

	@Override
	public HashSet<Integer> getAdjacentVertex(int id) {
		int index = searchVertexIndex(id);
		int[] edgesOfV = nodes2[index];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeNum=1;edgeNum<edgesOfV.length;edgeNum++) {
			int edgeID = edgesOfV[edgeNum];
			int adjID = (edges[edgeID][0] == id) ? edges[edgeID][2] : edges[edgeID][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}

	public HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException {
		int index = searchVertexIndex(id);
		int[] edgesOfV = nodes2[index];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeNum=1;edgeNum<edgesOfV.length;edgeNum++) {
			checkTime(seconds, startTime);
			int edgeID = edgesOfV[edgeNum];
			int adjID = (edges[edgeID][0] == id) ? edges[edgeID][2] : edges[edgeID][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException {
		int index = searchVertexIndex(id);
		int[] edgesOfV = nodes2[index];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeNum=1;edgeNum<edgesOfV.length;edgeNum++) {
			checkConn(session);
			int edgeID = edgesOfV[edgeNum];
			int adjID = (edges[edgeID][0] == id) ? edges[edgeID][2] : edges[edgeID][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
	
	public HashSet<Integer> getAdjacentVertexSessionTimeout(int id, WebSocketSession session, long initTime, int limitTime) throws InterruptedException, IOException {
		int index = searchVertexIndex(id);
		int[] edgesOfV = nodes2[index];
		HashSet<Integer> adjVL = new HashSet<Integer>();
		for (int edgeNum=1;edgeNum<edgesOfV.length;edgeNum++) {
			checkConn(session);
			checkTime(limitTime, initTime);
			int edgeID = edgesOfV[edgeNum];
			int adjID = (edges[edgeID][0] == id) ? edges[edgeID][2] : edges[edgeID][0];
			adjVL.add(adjID);
		}
		return adjVL;
	}
		
	
	/**
	 * @param id
	 * @return
	 */
	private int searchVertexIndex(int id) {
		// Búsqueda binaria
		int actIndexLeft = 0;
		int actIndexRight = nodesSize - 1;
		int actIndex = 0;
		while (actIndexLeft <= actIndexRight) {
			actIndex = (actIndexRight + actIndexLeft) / 2;
			if (nodes2[actIndex][0] < id) {
				actIndexLeft = actIndex + 1;
			}
			else if (nodes2[actIndex][0] > id){
				actIndexRight = actIndex - 1;
			}
			else {
				return actIndex;
			}
		}
		return -1;
		//return idNodes.get(id);
	}

	@Override
	public ArrayList<Integer> getEdges(int idVertex, int idVertex2) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		idVertex = searchVertexIndex(idVertex);
		//idVertex2 = searchVertexIndex(idVertex2);
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
	
	public int getGrade (int idVertex) {
		int index = searchVertexIndex(idVertex);
		int[] edgesOfV = nodes2[index];
		return edgesOfV.length - 1;
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

	

}