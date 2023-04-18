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
import org.json.JSONArray;
import org.json.JSONObject;

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
	private int edgesSize;
	
	public GraphNative (String filename, Boolean isGz, int edgesSize) throws IOException {
		structName = "graphNative";
		this.edgesSize = edgesSize; 
		
		nodes = new HashMap<Integer, LinkedList<Integer>>();
		edges = new int[edgesSize][3];
		
		BufferedReader fileBuff = readFile(filename, isGz);

		String line = "";
        String[] tempArr;
        
        int countedStatements = 0;
        int nodesLoaded = 0;
        int edgesLoaded = 0;

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
	public HashSet<Integer> getAdjacentVertex(int id) {
		LinkedList<Integer> edgesOfV = nodes.get(id);
		HashSet<Integer> adjVL = new HashSet<Integer>();
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

	@Override
	public int getPredicateEdge(Object e) {
		Integer edgeNum = (Integer) e;
		return edges[edgeNum][1];
	}

}