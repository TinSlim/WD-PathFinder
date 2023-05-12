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

	private HashMap<Integer, VertexWrapperTimeTest> nodes;
	private IGraph graph;
	private HashSet<Integer> addedNodes;
	private WebSocketSession session;
	public int totalEdges;
	public long startTime;
	public int seconds;
	//public int[] nodesNumbers;
	//public int size;
	
	public GraphWrapperTimeTestOptMem (IGraph graph2, int seconds){
		this.graph = (IGraph) graph2;
		this.nodes = new HashMap<Integer, VertexWrapperTimeTest>();
		this.addedNodes = new HashSet<Integer>();
		this.session = null;
		this.totalEdges = 0;
		this.seconds = seconds;
		//this.nodesNumbers = nums;
		//this.size = maxSize;
	}

	
	public void setSession (WebSocketSession session) {
		this.session = session;
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
	
	//@Override
    //public void run() {
	public void search (int [] nodesNumbers, int size) {
		startTime = System.currentTimeMillis();
		try {
			LinkedList<VertexWrapperTimeTest> toSearch = new LinkedList<VertexWrapperTimeTest>();
			HashSet<Integer> nodesNumbersSet = new HashSet<Integer>();
			
			// Los añade a lista para buscar
			for (int idSearch : nodesNumbers) {
				VertexWrapperTimeTest actVW = new VertexWrapperTimeTest(idSearch);
				nodes.put(idSearch, actVW);
				nodesNumbersSet.add(idSearch);
				toSearch.push(actVW);
			}
	
			while ( toSearch.size() > 0) {
				//checkTime("Before pop");													// Revisa tiempo
				
				VertexWrapperTimeTest actualVW = toSearch.pop();
				if (actualVW.sameColorDistance > (size/2) + size%2) {
					continue;
				}
				
				// Revisa VÉRTICES adyacentes
				for (Integer adjVertex : graph.getAdjacentVertexTimeout(actualVW.idVertex, seconds, startTime)) {
					//checkTime("Checking adj vertexes");									// Revisa tiempo
					
					// Así no cicla en el mismo nodo
					if (actualVW.idVertex == adjVertex) {
						continue;
					}
					
					// NO ha sido visitado:
					if (nodes.get(adjVertex) == null) {
						VertexWrapperTimeTest newVW = new VertexWrapperTimeTest (actualVW, adjVertex);
						nodes.put(adjVertex, newVW);
						toSearch.add(newVW);
					}
					
					// SI ha sido visitado
					else {
						VertexWrapperTimeTest adjVW = nodes.get(adjVertex);
						
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
							if (adjVW.sameColorDistance + actualVW.sameColorDistance + 1 <= size) {
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		finally {
			System.out.print(totalEdges+";"+(System.currentTimeMillis() - startTime)+";");
		}
	}
	
	public void backTracking(VertexWrapperTimeTest vw, int maxSize, HashSet<Integer> nodesNumbers) throws InterruptedException {
		LinkedList<VertexBackTrackingTimeTest> stack = new LinkedList<VertexBackTrackingTimeTest>();
		stack.push(new VertexBackTrackingTimeTest(vw));
		while (stack.size() > 0) {
			//checkTime("Inside backTracking");												// Revisa tiempo
			VertexBackTrackingTimeTest actualBT = stack.pop();
			if (actualBT.colorDistance + actualBT.grade > maxSize) {
				continue;
			}
			
			if (nodesNumbers.contains(actualBT.actVW.idVertex)) {
				makeEdges(actualBT.road);
			}
			
			for (VertexWrapperTimeTest vwFrom: actualBT.actVW.from) {
				checkTime("For vertex in BT");
				if (! actualBT.nodes.contains(vwFrom.idVertex)) {
					VertexBackTrackingTimeTest vbtNew = new VertexBackTrackingTimeTest(actualBT, vwFrom);
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
			//checkTime("Inside makeEdges");													// Revisa tiempo
			sendEdges(nodesList.get(i), nodesList.get(i + 1));
			i++;
		}
		return;
	}
	
	public void sendEdges (int v1, int v2) throws InterruptedException {
		if (nodes.get(v1).hasEdgeWith(v2) || nodes.get(v2).hasEdgeWith(v1)) {
			return;
		}
		
		if (nodes.get(v1).edgesWith != null) {
			nodes.get(v1).addEdgeWith(v2);
		}
		else {
			nodes.get(v2).addEdgeWith(v1);
		}
		//nodes.get(v1).addEdgeWith(v2);
		//nodes.get(v2).addEdgeWith(v1);
		
		ArrayList edges = graph.getEdges(v1, v2);
		ArrayList<Integer> vList = new ArrayList<Integer>();
		if (!addedNodes.contains(v1)) {
			vList.add(v1);
			addedNodes.add(v1);
		}
		
		if (!addedNodes.contains(v2)) {
			vList.add(v2);
			addedNodes.add(v2);
		}
		
		for (Object edge : edges) {
			//checkTime("For edge in sendEdges");													// Revisa tiempo
			totalEdges+=1;
		}
		
	}

}