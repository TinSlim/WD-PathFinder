/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.rdfpath.graph.wrapper.VertexWrapper;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapper {

	private HashMap<Integer, VertexWrapper> nodes;
	
	private IGraph graph;
	
	private int nodesSearch;
	private ArrayList <VertexWrapper> toSearch;
	private ArrayList<Integer> road;
	
	private WebSocketSession session;
	private ArrayList edges;
	private ArrayList<Integer> sentVertex;
	
	private long startTime;
	private long firstTime;
	private long lastTime;
	private Boolean started;
	
	public GraphWrapper (IGraph graph2) {
		this.graph = (IGraph) graph2;
		this.nodesSearch = 0;
		this.toSearch = new ArrayList<VertexWrapper>();
		this.road = new ArrayList<Integer>();
		this.session = null;
		this.edges = new ArrayList<Edge>();
		this.sentVertex = new ArrayList<Integer>();
		this.nodes = new HashMap<Integer, VertexWrapper>();
	}
	
	public void setSession (WebSocketSession session) {
		this.session = session;
	}
	
	public long[] getTimes () {
		long[] ans = {startTime, firstTime, lastTime};
		return ans;
	}
	
	public void search (int [] nodesNumbers, int size) throws IOException {
		// Obtiene nodos para caminos
		started = false;
		startTime = System.currentTimeMillis();
		firstTime = startTime;
		lastTime = startTime;
		
		
		// Guarda id nodos para buscar
		ArrayList<Integer> listNodes = new ArrayList<Integer> ();
		for (Integer i : nodesNumbers) {
			listNodes.add(i);
		}
		
		// Los añade a lista para buscar
		nodesSearch = 0;
		for (Integer idSearch : listNodes) {
			
			VertexWrapper actVW = new VertexWrapper(idSearch);
			//actVW.colorNode = nodesSearch;
			//nodes.put(v, actVW); revisar
			toSearch.add(actVW);
			nodesSearch += 1;
		}
		// -----
		
		int roadSize = 0;
		
		int actColor = 0; // USAR NÚMEROS
		while (toSearch.size() > 0 && roadSize < size * nodesSearch) {
			VertexWrapper actualVW = toSearch.remove(0);
			
			// Revisa color para avanzar en 1
			if (actualVW.colorNode != actColor) {
				actColor = actualVW.colorNode;
				roadSize += 1;
			}
			
			// Revisa VÉRTICES adyacentes
			for (Integer adjVertex : graph.getAdjacentVertex(actualVW.idVertex)) {
				
				// NO ha sido visitado:
				if (nodes.get(adjVertex) == null) {
					VertexWrapper newVW = new VertexWrapper (actualVW, adjVertex);
					nodes.put(adjVertex, newVW);
					toSearch.add(newVW);
				}
				
				
				// SI ha sido visitado Y NO es de cuál vine
				else if (actualVW.fatherNode != adjVertex) {
					VertexWrapper adjVW = nodes.get(adjVertex);
					
					// MISMO COLOR
					if (adjVW.colorNode == actualVW.colorNode) {
						
						// NUEVA RAMA -> BackTracking
						if (road.contains(adjVertex)) {
							// añadir Edge actualVW-adjVW
							ArrayList edgesVW = graph.getEdges(actualVW.idVertex, adjVW.idVertex);
							for (Object e : edgesVW) {
								sendEdge(e);
							}
							
							// BACKTRACKING actualVW
							backTracking(actualVW);
							adjVW.from.add(actualVW);
						}

						// GUARDAR HIJOS
						else {
							adjVW.from.add(actualVW);
						}

					}
					
					// DISTINTO COLOR
					else if (!actualVW.from.contains(adjVW) && !adjVW.from.contains(actualVW)) {
						
						// añadir Edge actualVW-adjVW
						ArrayList edgesVW = graph.getEdges(actualVW.idVertex, adjVW.idVertex);
						for (Object e : edgesVW) {
							sendEdge(e);
						}
						
						// BACKTRACKING actualVW
						backTracking(actualVW);
						
						// BACKTRACKING adjVW
						backTracking(adjVW);
						adjVW.from.add(actualVW);
					}
				}
			}
		}
		lastTime = System.currentTimeMillis();
	}

	public void backTracking (VertexWrapper vw) throws IOException {
		ArrayList<VertexWrapper> toCheck = new ArrayList<VertexWrapper>();
		toCheck.add(vw);
		while (toCheck.size() > 0) {
			VertexWrapper actualVW = toCheck.remove(0);
			if (! road.contains(actualVW.idVertex)) {
				road.add(actualVW.idVertex);
				for (VertexWrapper adjVW : actualVW.from) {
					// Imprime aristas
					ArrayList edgesVW = graph.getEdges(actualVW.idVertex, adjVW.idVertex);
					for (Object e : edgesVW) {
						sendEdge(e);
					}
					
					// Añade nodos
					toCheck.add(adjVW);
				}
			}
		}
		
	}
	
	public void sendEdge(Object e) throws IOException {
		if (edges.contains(e)) {return;};
		
		//
		if (!started) {
			firstTime = System.currentTimeMillis();
			started = true;
		}
		
		//
		if (session != null) {
			ArrayList<Integer> vList = new ArrayList<Integer>();
			if (!sentVertex.contains(graph.getOriginEdge(e))) {
				sentVertex.add(graph.getOriginEdge(e));
				vList.add(graph.getOriginEdge(e));
			}
			if (!sentVertex.contains(graph.getDestinationEdge(e))) {
				vList.add(graph.getDestinationEdge(e));
				sentVertex.add(graph.getDestinationEdge(e));
			}
			//session.sendMessage(new TextMessage(graph.edgeToJson(e,vList)));
		}
		else {
			System.out.println(graph.getOriginEdge(e) + " -> "+ graph.getDestinationEdge(e));
		}
		edges.add(e);
	}
}