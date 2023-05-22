package com.rdfpath.graph.wrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import com.rdfpath.graph.model.IGraph;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapperDFS {
	
	private HashMap<Integer, VertexWrapperDFS> nodes;		// Almacena id y Nodo
	private LinkedList<VertexWrapperDFS> toSearch;
	
	private IGraph graph;									// Referencia al grafo
	
	public GraphWrapperDFS (IGraph graph2) {
		this.graph = (IGraph) graph2;
	}


	public LinkedList<LinkedList<Integer>> search (int [] nodesNumbers, int maxSize) throws IOException {
		HashSet<Integer> nodesHash = new HashSet<Integer>();
		for (int i : nodesNumbers) {
			nodesHash.add(i);
		}

		LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
		LinkedList<VertexWrapperDFS> stack = new LinkedList<VertexWrapperDFS>();
		
		for (int x : nodesNumbers) {
			VertexWrapperDFS vw = new VertexWrapperDFS (x);
			stack.add(vw);
		}
				
		while (stack.size() > 0) {
			VertexWrapperDFS actVW = stack.pop();
			if (actVW.actSize > maxSize) {
				continue;
			}
			
			HashSet<Integer> a = graph.getAdjacentVertex(actVW.vertexID);
			VertexWrapperDFS copyActVW = actVW;
			do { 
				a.remove(copyActVW.vertexID);
				copyActVW = copyActVW.father;			}
			while (copyActVW != null);
			
			
			
			for (int neighbor : a) {
				if (nodesHash.contains(neighbor) && neighbor != actVW.initId) {
					LinkedList<Integer> ans = new LinkedList<Integer>();
					ans.add(neighbor);
					copyActVW = actVW;
					do { 
						ans.add(copyActVW.vertexID);
						copyActVW = copyActVW.father;
					}
					while (copyActVW != null);
			}
				else {
					VertexWrapperDFS dd = new VertexWrapperDFS(actVW, neighbor);
					stack.add(dd);
				}
			}
		}
		return paths;
		
	}
	/**
	def all_paths_new(graph, start, max_size):
	    
	        
	        for neighbor in graph[vertex] - set(path):
	            if neighbor in start and neighbor != ini:
	                paths.append(path + [neighbor])
	            else:
	                stack.append((neighbor, path + [neighbor],ini, act_size + 1))
	    return paths
	 */

	public void allPathsNew (int[] start, int maxSize) {
		
		HashSet<Integer> nodesHash = new HashSet<Integer>();
		for (int i : start) {
			nodesHash.add(i);
		}
		
		for (int x : nodesHash) {
			
			int id = x;
			
			LinkedList<Integer> bits = new LinkedList();
			int counter = 0;
			while (counter < maxSize) {
				bits.push(0);
				counter++;
			}
			
			LinkedList<Integer> path = new LinkedList();
			path.push(x);
			
			int init = x;
			
			while (path.size() < bits.size()) {
				HashSet<Integer> a = graph.getAdjacentVertex(id);
				for (int i : path) {
					a.remove(i);
				}
				Integer[] posibleNeighbors = a.toArray(new Integer[a.size()]);
				int indexNewNeighbor = bits.get(path.size());
				
				if (posibleNeighbors.length <= indexNewNeighbor) { 
					bits.set(path.size(), 0);
					
					if (path.size() > 1) {
						bits.set(path.size() - 1, bits.get(path.size() - 1) + 1);
						path.pop();
						id = path.get(0);
					}
					
					else {
						break;
					}
				}
				
				else if (nodesHash.contains(posibleNeighbors[indexNewNeighbor]) && posibleNeighbors[indexNewNeighbor] != init) {
					path.push(posibleNeighbors[indexNewNeighbor]);
					System.out.println(path); // print		
					System.out.println("or");
					path.pop();
					bits.set(path.size(),bits.get(path.size()) + 1);
					id = path.get(0);
				}
				
				else if (path.size() == bits.size() - 1) {
					path.push(posibleNeighbors[indexNewNeighbor]);
					id = posibleNeighbors[indexNewNeighbor];
					path.pop();
					id = path.get(0);
					bits.set(path.size(), bits.get(path.size()) + 1);
				}
				
				else {
					path.push(posibleNeighbors[indexNewNeighbor]);
					id = posibleNeighbors[indexNewNeighbor];
				}
				
			}
			
		}
		return;
	}

}