package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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

public class GraphComp extends AbstractGraph {
	// NODES = 98347590
	public int[][][] nodes;
	//public HashMap<Integer,Integer> idNodes; /// HASHMAP TODO
	public int edgesSize;
	
	public GraphComp (String filename, Boolean isGz, int edgesSize) throws IOException {
		structName = "graphComp";
		
		nodes = new int[edgesSize + 1][][];
		this.edgesSize = edgesSize + 1;
		
		BufferedReader fileBuff = readFile(filename, isGz);
		
		String line = "";
        String[] tempArr;
        int node_id = 0;

        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		//timeA = System.currentTimeMillis();

    		//sendNotificationTime(10,"Nodos: " + node_id);
    		
    		tempArr = line.split(" ");
    		int id = Integer.parseInt(tempArr[0]);						// line 	= "18 -22.16.32 23.17"
    		//idNodes.put(id[0], node_id);								// temArr 	= {"18", "-22.16.32", "23.17"}
    		int[][] numbers = new int[tempArr.length - 1][];			// id 		= 18
           
    		// Para cada grupo [pred, obj] ó [pred, obj1, obj2, ...]	// Usando el "-22.16.32":
    		for(int i = 1;i < tempArr.length;i++)
    		{
        	   String[] temp_arr3 = tempArr[i].split("\\.");			// temp_arr3	= {"-22", "16", "32} 
        	   int [] conn = new int[temp_arr3.length];
        	   for (int j = 0;j < temp_arr3.length;j++) {
        		   conn[j] = Integer.parseInt(temp_arr3[j]);			// conn	= {-22, 16, 32} 
        	   }
        	   numbers[i - 1] = conn;									// numbers = {{-22, 16, 32}, {23, 17}} 
           }
           
           nodes[id] = numbers;
           node_id += 1;
        }
        fileBuff.close();
		return;
	}
	
	// TODO Cuidado excepcion
	public int searchVertexIndex (int id) {
		if (id < edgesSize) {
			return id;
		}
		return -1;
	}
	
	@Override
	public HashSet<Integer> getAdjacentVertex(int id) {
		HashSet<Integer> answer = new HashSet<Integer>();
		int index = searchVertexIndex(id);
		int i = 0;
		int j = -1;
		while (i < nodes[index].length) {
			j = 1;
			while (j < nodes[index][i].length) {
				answer.add(nodes[index][i][j]);
				j+=1;
			}
			i+=1;
		}
		return answer;
	}

	@Override
	public ArrayList getEdges(int idVertex, int idVertex2) {
		// TODO Auto-generated method stub
		ArrayList<int[]> edges = new ArrayList<int[]>();
		int index = searchVertexIndex(idVertex);
		
		int i = 0;
		int j;
		while (i < nodes[index].length) {
			j = 1;
			while (j < nodes[index][i].length) {
				if (idVertex2 == nodes[index][i][j])  {
					int[] edge = {index,nodes[index][i][0], nodes[index][i][j]};
					edges.add(edge);
					break;
				}
				j+=1;
			}
			i+=1;
		}
		return edges;
	}

	@Override
	public int getOriginEdge(Object e) {
		int[] edge = (int[]) e;
		if (edge[1] > 0) {
			return edge[0];
		}
		return edge[2];
	}

	@Override
	public int getDestinationEdge(Object e) {
		int[] edge = (int[]) e;
		if (edge[1] < 0) {
			return edge[0];
		}
		return edge[2];
	}
	
	public int getPredicateEdge(int[] edge) {
		if (edge[1] < 0) {
			return -1 * edge[1];
		}
		return edge[1];
	}

	@Override
	public CharSequence edgeToJson(Object e, ArrayList<Integer> vList) {
		int[] edgeF = (int[]) e;
		String message;
    	JSONObject json = new JSONObject();
    	
    	String edgeLabel = Utils.getEntityName("P"+getPredicateEdge(edgeF)+"&type=property");
    	String edgeLabelSmall = edgeLabel;
    	if (edgeLabel.length() > 7) {edgeLabelSmall = edgeLabel.substring(0,Math.min(edgeLabel.length(), 7)) + "...";}
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", getOriginEdge(e));
    	edge.put("to", getDestinationEdge(e));
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
    		//String vertexLabel = "" + v;
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

}
