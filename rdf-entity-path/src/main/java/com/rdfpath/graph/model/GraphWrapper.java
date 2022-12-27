package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class GraphWrapper {
	private HashMap<Vertex, VertexWrapper> nodes;
	private Graph graph;
	
	private int nodesSearch;
	private ArrayList <VertexWrapper> toSearch;

	private ArrayList<Vertex> road;
	
	private WebSocketSession session;
	
	public GraphWrapper (Graph graph) {
		this.nodes = new HashMap<Vertex, VertexWrapper>();
		this.graph = graph;
		this.nodesSearch = 0;
		this.toSearch = new ArrayList<VertexWrapper>();
		this.road = new ArrayList<Vertex>();
		this.session = null;
	}
	
	public void setSession (WebSocketSession session) {
		this.session = session;
	}
	
	public void search (int [] nodesNumbers, int size) throws IOException {
		// Obtiene nodos para caminos
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		for (Integer i : nodesNumbers) {
			listNodes.add(graph.getNodes().get(i));
		}
		// -----
		
		// Los añade a lista para buscar
		nodesSearch = 0;
		for (Vertex v : listNodes) {
			nodesSearch += 1;
			VertexWrapper actVW = new VertexWrapper(v);
			nodes.put(v, actVW);
			toSearch.add(actVW);
		}
		// -----
		
		int roadSize = 0;
		Vertex actColor = null;
		while (toSearch.size() > 0 && roadSize < size * nodesSearch) {
			VertexWrapper actualVW = toSearch.remove(0);
			
			// Revisa color para avanzar en 1
			if (actualVW.colorNode != actColor) {
				actColor = actualVW.colorNode;
				roadSize += 1;
			}
			
			// Revisa VÉRTICES adyacentes
			for (Vertex adjVertex : actualVW.node.getAdjacentVertex()) {
				
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
							ArrayList<Edge> edges = actualVW.node.getEdges(adjVW.node);
							for (Edge e : edges) {
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
						ArrayList<Edge> edges = actualVW.node.getEdges(adjVW.node);
						for (Edge e : edges) {
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
	}

	public void backTracking (VertexWrapper vw) throws IOException {
		ArrayList<VertexWrapper> toCheck = new ArrayList<VertexWrapper>();
		toCheck.add(vw);
		while (toCheck.size() > 0) {
			VertexWrapper actualVW = toCheck.remove(0);
			if (! road.contains(actualVW.node)) {
				road.add(actualVW.node);
				for (VertexWrapper adjVW : actualVW.from) {
					// Imprime aristas
					ArrayList<Edge> edges = actualVW.node.getEdges(adjVW.node);
					for (Edge e : edges) {
						sendEdge(e);
					}
					// Añade nodos
					toCheck.add(adjVW);
				}
				
			}
		}
		
	}
	
	public void sendEdge(Edge e) throws IOException {
		if (session == null) {
			System.out.println(e);
		}
		else {
			ArrayList<Vertex> vList = new ArrayList<Vertex>();
			if (!road.contains(e.getOrigin())) {
				vList.add(e.getOrigin());
			}
			if (!road.contains(e.getDestination())) {
				vList.add(e.getDestination());
			}
			session.sendMessage(new TextMessage(e.toJson(vList)));
		}
		
		
	}
}
