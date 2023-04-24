package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	public ArrayList<int[]> getEdges(int idVertex, int idVertex2) {
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
	
	@Override
	public int getPredicateEdge (Object e) {
		int[] edge = (int[]) e;
		if (edge[1] < 0) {
			return -1 * edge[1];
		}
		return edge[1];
	}
}
