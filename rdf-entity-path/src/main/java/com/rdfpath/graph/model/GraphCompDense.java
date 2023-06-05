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

public class GraphCompDense extends AbstractGraph {
	public int[][][] nodes;
	public int edgesSize;
	
	public GraphCompDense (String filename, Boolean isGz, int edgesSize) throws IOException {
		structName = "graphCompDense";

		nodes = new int[edgesSize][][];
		this.edgesSize = edgesSize;

		BufferedReader fileBuff = readFile(filename, isGz);
		
		String line = "";
        String[] tempArr;
        int node_id = 0;

        while((line = fileBuff.readLine()) != null) {					// Ejemplo:
    		tempArr = line.split(" ");									// line 	= "18 -22.16.32 23.17"
    		int id[] = {Integer.parseInt(tempArr[0])};					// temArr 	= {"18", "-22.16.32", "23.17"}				
    		int[][] numbers = new int[tempArr.length][];				// id 		= {18}
    		numbers[0] = id;											// numbers	= {18, ...}						
           
    		// Para cada grupo [pred, obj] ó [pred, obj1, obj2, ...]	
    		for(int i = 1;i < tempArr.length;i++)
    		{
        	   String[] temp_arr3 = tempArr[i].split("\\.");			// Usando el "-22.16.32":
        	   int [] conn = new int[temp_arr3.length];					// temp_arr3	= {"-22", "16", "32}
        	   for (int j = 0;j < temp_arr3.length;j++) {				// conn	= {-22, 16, 32} 
        		   conn[j] = Integer.parseInt(temp_arr3[j]);
        	   }
        	   numbers[i] = conn;										// numbers = {{18}, {-22, 16, 32}, {23, 17}} 			
           }
           
           nodes[node_id] = numbers;
           node_id += 1;
        }
        fileBuff.close();
		return;
	}
	
	/**
	 * Búsqueda binaria para encontrar el índice que corresponde con el ID de un nodo. Si no lo encuentra
	 * retorna -1
	 * @param id			ID del nodo que se va a buscar
	 * @return				Índice del nodo buscado en el arreglo
	 */
	public int searchVertexIndex (int id) {
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
	public HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException {
		HashSet<Integer> answer = new HashSet<Integer>();
		int index = searchVertexIndex(id);
		int i = 1;
		int j = -1;

		while (i < nodes[index].length) {
			j = 1;
			while (j < nodes[index][i].length) {
				checkTime(seconds,startTime);
				answer.add(nodes[index][i][j]);
				j+=1;
			}
			i+=1;
		}
		return answer;
	}
	
	
	@Override
	public HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException {
		HashSet<Integer> answer = new HashSet<Integer>();
		int index = searchVertexIndex(id);
		if (index == -1) return answer;
		
		int i = 1;
		int j = -1;

		while (i < nodes[index].length) {
			j = 1; 
			while (j < nodes[index][i].length) {
				checkConn(session);
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

	public int getGrade (int idVertex) {
		int index = searchVertexIndex(idVertex);
		int gradeTotal = 0;
		
		int i = 1;

		while (i < nodes[index].length) {
			gradeTotal += nodes[index][i].length - 1;
			i+=1;
		}
		return gradeTotal;
	}
}
