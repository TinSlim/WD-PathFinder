package com.rdfpath.graph.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.web.socket.WebSocketSession;

import com.rdfpath.graph.model.IGraph;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapperTimeTestOptMem {

	private HashMap<Integer, VertexWrapperTimeTestOptMem> nodes;
	private IGraph graph;
	private HashSet<Integer> addedNodes;
	public int totalEdges;
	public long startTime;
	public int seconds;
	
	public GraphWrapperTimeTestOptMem (IGraph graph2, int seconds){
		this.graph = (IGraph) graph2;
		this.nodes = new HashMap<Integer, VertexWrapperTimeTestOptMem>();
		this.addedNodes = new HashSet<Integer>();
		this.totalEdges = 0;
		this.seconds = seconds;
	}

	public void checkTime (String message) throws InterruptedException {
		if ( (System.currentTimeMillis() - startTime) > seconds * 1000 ) {
			if (System.getProperty("debug") != null) {
				System.out.println(message);
			}
			throw new InterruptedException("Done Time");
		}
		/*
		 if (Thread.interrupted()) {
		    throw new InterruptedException();
		}
		 */
	}

	public void search (int [] nodesNumbers, int size, int maxEdgeSize) {
		startTime = System.currentTimeMillis();
		try {
			LinkedList<VertexWrapperTimeTestOptMem> toSearch = new LinkedList<VertexWrapperTimeTestOptMem>();
			HashSet<Integer> nodesNumbersSet = new HashSet<Integer>();
			
			// Los añade a lista para buscar
			for (int idSearch : nodesNumbers) {
				VertexWrapperTimeTestOptMem actVW = new VertexWrapperTimeTestOptMem(idSearch);
				nodes.put(idSearch, actVW);
				nodesNumbersSet.add(idSearch);
				toSearch.push(actVW);
			}
	
			while ( toSearch.size() > 0 ) {
				checkTime("Before pop");													// Revisa tiempo
				
				VertexWrapperTimeTestOptMem actualVW = toSearch.pop();
				if (actualVW.sameColorDistance > (size/2) + size%2) {
					continue;
				}
				
				// Revisa VÉRTICES adyacentes
				for (Integer adjVertex : graph.getAdjacentVertexTimeoutLimited(actualVW.idVertex, seconds, startTime, maxEdgeSize, actualVW.initial)) {
					checkTime("Checking adj vertexes");									// Revisa tiempo
					
					// Así no cicla en el mismo nodo
					if (actualVW.idVertex == adjVertex) {
						continue;
					}
					
					// NO ha sido visitado:
					if (nodes.get(adjVertex) == null) {
						VertexWrapperTimeTestOptMem newVW = new VertexWrapperTimeTestOptMem (actualVW, adjVertex);
						nodes.put(adjVertex, newVW);
						toSearch.add(newVW);
					}
					
					// SI ha sido visitado
					else {
						VertexWrapperTimeTestOptMem adjVW = nodes.get(adjVertex);
						
						// Si es el que lo agregó a la lista
						if (actualVW.onlyFather(adjVW)) {
							continue;
						}
						
						// Mismo color
						if (actualVW.colorNode == adjVW.colorNode) {
	
							// Revisar si se agregó, ESTÁ EN PATH
							if (adjVW.otherColorDistance > -1 && adjVW.otherColorDistance + actualVW.sameColorDistance + 1 <= size) {
								
								if (actualVW.otherColorDistance == -1) {
									actualVW.otherColorDistance = adjVW.otherColorDistance + 1;
									backTracking(actualVW, size, nodesNumbersSet);
								}
								
								
								LinkedList<Integer> unionNodes = new LinkedList<Integer>();
								unionNodes.push(adjVW.idVertex);
								unionNodes.push(actualVW.idVertex);
								makeEdges(unionNodes);
							}
							
							if (adjVW.queueTimes < 2) {
								toSearch.add(adjVW);
								adjVW.queueTimes = adjVW.queueTimes += 1; 
							}
							
							adjVW.from.add(actualVW);
						}
						
						else {
							if (adjVW.sameColorDistance + actualVW.sameColorDistance + 1 <= size) {
								
								LinkedList<Integer> unionNodes = new LinkedList<Integer>();
								unionNodes.push(adjVW.idVertex);
								unionNodes.push(actualVW.idVertex);
								makeEdges(unionNodes);
								
								if (adjVW.otherColorDistance != -1) {
									adjVW.otherColorDistance = Math.min(adjVW.otherColorDistance,actualVW.sameColorDistance + 1);
								} else {
									adjVW.otherColorDistance = actualVW.sameColorDistance + 1;
									backTracking(adjVW, size, nodesNumbersSet);
								}
								if (actualVW.otherColorDistance != -1) {
									actualVW.otherColorDistance = Math.min(actualVW.otherColorDistance,adjVW.sameColorDistance + 1);
								} else {
									actualVW.otherColorDistance = adjVW.sameColorDistance + 1;
									backTracking(actualVW, size, nodesNumbersSet);
								}
							}
						}
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		finally {
			System.out.print(totalEdges+";"+(System.currentTimeMillis() - startTime)+";");
		}
	}
	
	public void backTracking(VertexWrapperTimeTestOptMem vw, int maxSize, HashSet<Integer> nodesNumbers) throws InterruptedException {
		LinkedList<VertexBackTrackingTimeTestOptMem> stack = new LinkedList<VertexBackTrackingTimeTestOptMem>();
		stack.push(new VertexBackTrackingTimeTestOptMem(vw));
		while (stack.size() > 0) {
			checkTime("Inside backTracking");												// Revisa tiempo
			VertexBackTrackingTimeTestOptMem actualBT = stack.pop();
			if (actualBT.colorDistance + actualBT.grade > maxSize) {
				continue;
			}
			
			if (nodesNumbers.contains(actualBT.actVW.idVertex)) {
				makeEdges(actualBT.road);
			}
			
			for (VertexWrapperTimeTestOptMem vwFrom: actualBT.actVW.from) {
				checkTime("For vertex in BT");
				if (vwFrom.otherColorDistance == -1) {
					vwFrom.otherColorDistance = actualBT.actVW.otherColorDistance + 1;
				}
				if (! actualBT.nodes.contains(vwFrom.idVertex)) {
					VertexBackTrackingTimeTestOptMem vbtNew = new VertexBackTrackingTimeTestOptMem(actualBT, vwFrom);
					stack.push(vbtNew);
				}				
			}
		}
	}

	public void makeEdges (LinkedList<Integer> nodesList) throws InterruptedException {
		if (nodesList.size() < 2) {
			return;
		}
		int i = 0;
		while ( i < nodesList.size() - 1) {
			checkTime("Inside makeEdges");													// Revisa tiempo
			sendEdges(nodesList.get(i), nodesList.get(i + 1));
			i++;
		}
		return;
	}
	
	public void sendEdges (int v1, int v2) throws InterruptedException {
		checkTime("Send Edges");											// Revisa tiempo
		VertexWrapperTimeTestOptMem vw1 = nodes.get(v1);
		VertexWrapperTimeTestOptMem vw2 = nodes.get(v2);
		if (vw1.hasEdgeWith(v2) || vw2.hasEdgeWith(v1)) {
			return;
		}
		
		if (vw1.edgesWith != null) {
			vw1.addEdgeWith(v2);
		}
		else {
			vw2.addEdgeWith(v1);
		}

		ArrayList edges = graph.getEdges(v1, v2);
				
		if (!addedNodes.contains(v1)) {
			addedNodes.add(v1);
		}
		
		if (!addedNodes.contains(v2)) {
			addedNodes.add(v2);
		}
		
		for (Object edge : edges) {
			checkTime("For edge in sendEdges");													// Revisa tiempo
			totalEdges+=1;
		}
		
	}

}