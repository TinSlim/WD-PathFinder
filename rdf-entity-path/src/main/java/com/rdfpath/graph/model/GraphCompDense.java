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

public class GraphCompDense extends AbstractGraph {
	// NODES = 98347590
	public int[][][] nodes;
	//public HashMap<Integer,Integer> idNodes; /// HASHMAP TODO
	public int edgesSize;
	
	public GraphCompDense (String filename, Boolean isGz, int edgesSize) throws IOException {
		structName = "graphCompDense";
		//String filename2 = "C:/Users/Cristóbal/Documents/RDF-Path-server/python/prearchivo/compressed_struct.gz";
		//98347590
		// TODO set 36 - 98347590 FILE
		
		
		nodes = new int[edgesSize][][];
		this.edgesSize = edgesSize;
		
		//idNodes = new HashMap<Integer, Integer>();
		BufferedReader fileBuff = readFile(filename, isGz);
		
		String line = "";
        String[] tempArr;
        int node_id = 0;

        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		//timeA = System.currentTimeMillis();

    		//sendNotificationTime(10,"Nodos: " + node_id);
    		
    		tempArr = line.split(" ");
    		int id[] = {Integer.parseInt(tempArr[0])};					// line 	= "18 -22.16.32 23.17"
    		//idNodes.put(id[0], node_id);								// temArr 	= {"18", "-22.16.32", "23.17"}
    		int[][] numbers = new int[tempArr.length][];				// id 		= {18}
    		numbers[0] = id;											// numbers	= {{18}  }
           
    		// Para cada grupo [pred, obj] ó [pred, obj1, obj2, ...]	// Usando el "-22.16.32":
    		for(int i = 1;i < tempArr.length;i++)
    		{
        	   String[] temp_arr3 = tempArr[i].split("\\.");			// temp_arr3	= {"-22", "16", "32} 
        	   int [] conn = new int[temp_arr3.length];
        	   for (int j = 0;j < temp_arr3.length;j++) {
        		   conn[j] = Integer.parseInt(temp_arr3[j]);			// conn	= {-22, 16, 32} 
        	   }
        	   numbers[i] = conn;										// numbers = {{18}, {-22, 16, 32}, {23, 17}} 
           }
           
           nodes[node_id] = numbers;
           node_id += 1;
        }
        fileBuff.close();
		return;
	}
	
	
	public int searchVertexIndex (int id) {
		// Búsqueda binaria
		int actIndexLeft = 0;
		int actIndexRight = edgesSize - 1;
		int actIndex = 0;
		while (actIndexLeft <= actIndexRight) {
			actIndex = (actIndexRight + actIndexLeft) / 2;
			if (nodes[actIndex][0][0] < id) {
				actIndexLeft = actIndex + 1;
			}
			else if (nodes[actIndex][0][0] > id){
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
	public HashSet<Integer> getAdjacentVertex(int id) {
		HashSet<Integer> answer = new HashSet<Integer>();
		int index = searchVertexIndex(id);
		int i = 1;
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
		ArrayList<int[]> edges = new ArrayList<int[]>();
		int index = searchVertexIndex(idVertex);
		
		int i = 1;
		int j;
		while (i < nodes[index].length) {
			j = 1;
			while (j < nodes[index][i].length) {
				if (idVertex2 == nodes[index][i][j])  {
					int[] edge = {nodes[index][0][0],nodes[index][i][0], nodes[index][i][j]};
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
	public int getPredicateEdge(Object e) {
		int[] edge = (int []) e;
		if (edge[1] < 0) {
			return -1 * edge[1];
		}
		return edge[1];
	}
}
