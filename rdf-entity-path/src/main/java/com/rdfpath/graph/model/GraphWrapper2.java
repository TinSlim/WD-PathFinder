package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapper2 {
	private String[] palette = {"#ADF7B6","#A0CED9","#FCF5C7"};
	private HashMap<Integer, VertexWrapper2> nodes;
	private IGraph graph;
	private HashSet<Integer> addedNodes;
	private WebSocketSession session;
	private int totalEdges;
	
	public GraphWrapper2 (IGraph graph2) {
		this.graph = (IGraph) graph2;
		this.nodes = new HashMap<Integer, VertexWrapper2>();
		this.addedNodes = new HashSet<Integer>();
		this.session = null;
		this.totalEdges = 0;
	}
	
	public void setSession (WebSocketSession session) {
		this.session = session;
	}

	public void search (int [] nodesNumbers, int size) throws IOException {
		
		LinkedList<VertexWrapper2> toSearch = new LinkedList<VertexWrapper2>();
		HashSet<Integer> nodesNumbersSet = new HashSet<Integer>();
		
		
		int index = 0;
		for (int idSearch : nodesNumbers) {
			VertexWrapper2 actVW = new VertexWrapper2(idSearch);
			nodes.put(idSearch, actVW);
			nodesNumbersSet.add(idSearch);
			toSearch.push(actVW);
			
			addedNodes.add(idSearch);
			session.sendMessage(new TextMessage(
					graph.initNodeToJson(
							idSearch,
							palette[index],
							(360/nodesNumbers.length)*index)));
			index++;
			float a = (360/nodesNumbers.length)*index;
			System.out.println(a);
			System.out.println(Math.cos(a));
			System.out.println(Math.sin(a));
			System.out.println("-");
			//session.sendMessage(new TextMessage(graph.nodeToJson(idSearch)));
		}
		

		while (toSearch.size() > 0) {
			VertexWrapper2 actualVW = toSearch.pop();
			
			if (actualVW.sameColorDistance > (size/2) + size%2) {
				continue;
			}

			// Revisa VÉRTICES adyacentes
			for (Integer adjVertex : graph.getAdjacentVertex(actualVW.idVertex)) {

				// Así no cicla en el mismo nodo
				if (actualVW.idVertex == adjVertex) {
					continue;
				}

				// NO ha sido visitado:
				if (nodes.get(adjVertex) == null) {
					VertexWrapper2 newVW = new VertexWrapper2 (actualVW, adjVertex);
					nodes.put(adjVertex, newVW);
					toSearch.add(newVW);
				}
				
				// SI ha sido visitado
				else {
					VertexWrapper2 adjVW = nodes.get(adjVertex);
					
					// Si es el que lo agregó a la lista
					if (actualVW.fromFather(adjVW)) {
						actualVW.removeFather();
						continue;
					}
					
					// Mismo color
					if (actualVW.colorNode == adjVW.colorNode) {

						// Revisar si se agregó, ESTÁ EN PATH
						if (adjVW.otherColorDistance > -1 && adjVW.otherColorDistance + actualVW.sameColorDistance <= size) {
							LinkedList<Integer> unionNodes = new LinkedList<Integer>();
							unionNodes.push(adjVW.idVertex);
							unionNodes.push(actualVW.idVertex);
							makeEdges(unionNodes);
							backTracking(actualVW, size, nodesNumbersSet);
						}
						
						else if (adjVW.addFrom(actualVW)) {
							toSearch.add(adjVW);
						}
					}
					
					else {
						if (adjVW.sameColorDistance + actualVW.sameColorDistance + 1 <= size) { //
							adjVW.otherColorDistance = actualVW.sameColorDistance + 1;
							actualVW.otherColorDistance = adjVW.sameColorDistance + 1;
							backTracking(adjVW, size, nodesNumbersSet);
							LinkedList<Integer> unionNodes = new LinkedList<Integer>();
							unionNodes.push(adjVW.idVertex);
							unionNodes.push(actualVW.idVertex);
							makeEdges(unionNodes);
							backTracking(actualVW, size, nodesNumbersSet);
						}
					
					}
					
				}
			}
		}
	}
	
	public void backTracking(VertexWrapper2 vw, int maxSize, HashSet<Integer> nodesNumbers) {
		LinkedList<VertexBackTracking> stack = new LinkedList<VertexBackTracking>();
		stack.push(new VertexBackTracking(vw));
		while (stack.size() > 0) {
			VertexBackTracking actualBT = stack.pop();
			if (actualBT.colorDistance + actualBT.grade > maxSize) {
				continue;
			}
			
			if (nodesNumbers.contains(actualBT.actVW.idVertex)) {
				makeEdges(actualBT.road);
			}
			
			for (VertexWrapper2 vwFrom: actualBT.actVW.from) {
				if (! actualBT.nodes.contains(vwFrom.idVertex)) {
					VertexBackTracking vbtNew = new VertexBackTracking(actualBT, vwFrom);
					stack.push(vbtNew);
				}				
			}
		}
	}

	public void makeEdges (LinkedList<Integer> nodesList) {
		if (nodesList.size() < 2) {
			return;
		}
		int i = 0;
		while (i < nodesList.size() - 1) {
			sendEdges(nodesList.get(i), nodesList.get(i + 1));
			i++;
		}
		return;
	}
	
	public void sendEdges (int v1, int v2) {
		if (nodes.get(v1).hasEdgeWith(v2) && nodes.get(v2).hasEdgeWith(v1)) {
		return;
		}
		
		nodes.get(v1).addEdgeWith(v2);
		nodes.get(v2).addEdgeWith(v1);
		
		ArrayList edges = graph.getEdges(v1, v2);
		ArrayList<Integer> vList = new ArrayList<Integer>();
		// AddedNodes se puede borrar usando size de edgesNodes
		if (!addedNodes.contains(v1)) {
			addedNodes.add(v1);
			try {
				session.sendMessage(new TextMessage(graph.nodeToJson(v1)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!addedNodes.contains(v2)) {
			addedNodes.add(v2);
			try {
				session.sendMessage(new TextMessage(graph.nodeToJson(v2)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Object edge : edges) {
			totalEdges+=1;
			try { 
				session.sendMessage(new TextMessage(graph.edgeToJson(edge)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}