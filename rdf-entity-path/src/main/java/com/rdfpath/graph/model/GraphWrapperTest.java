package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.locationtech.jts.awt.PointShapeFactory.X;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapperTest implements Callable<String> {
	
	private HashMap<Integer, VertexWrapperTest> nodes;		// Almacena id y Nodo
	private LinkedList<VertexWrapperTest> toSearch;
	
	private IGraph graph;									// Referencia al grafo
	
	public GraphWrapperTest (IGraph graph2) {
		this.graph = (IGraph) graph2;
	}


	public LinkedList<LinkedList<Integer>> search (int [] nodesNumbers, int maxSize) throws IOException {
		HashSet<Integer> nodesHash = new HashSet<Integer>();
		for (int i : nodesNumbers) {
			nodesHash.add(i);
		}
		
		
		LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
		LinkedList<VertexWrapperTest> stack = new LinkedList<VertexWrapperTest>();
		
		for (int x : nodesNumbers) {
			stack.add(new VertexWrapperTest (x));
		}
				
		while (stack.size() > 0) {
			VertexWrapperTest actVW = stack.pop();
			if (actVW.actSize > maxSize) {
				continue;
			}
			
			HashSet<Integer> a = graph.getAdjacentVertex(actVW.vertexID);
			
			for (int i : actVW.path) {
				a.remove(i);
			}
			
			
			
			for (int neighbor : a) {
				
				if (nodesHash.contains(neighbor) && neighbor != actVW.initId) {
					LinkedList<Integer> secList = (LinkedList<Integer>) actVW.path.clone();
					secList.add(neighbor);
					//paths.add(secList);
					System.out.println(secList);
					System.out.println(stack.size());
			
			}
				else {
					VertexWrapperTest dd = new VertexWrapperTest(actVW);
					dd.vertexID = neighbor;
					dd.path.add(neighbor);
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

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}