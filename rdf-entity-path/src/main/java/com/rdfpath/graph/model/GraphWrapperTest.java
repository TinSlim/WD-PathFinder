package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

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
	
	private IGraph graph;									// Referencia al grafo
	
	private int nodesSearch;
	private ArrayList <VertexWrapperTest> toSearch;			// Lista de nodos por buscar
	private ArrayList<Integer> road;						//
	
	private WebSocketSession session;						// Sesión
	private ArrayList edges;								// Aristas
	private ArrayList<Integer> sentVertex;					// ID nodos enviados
	
	private long startTime;									// Tiempo inicio
	private long firstTime;									// Tiempo primera arista
	private long lastTime;									// Tiempo última arista
	private Boolean started;								// Encontró al menos una arista
	
	public GraphWrapperTest (IGraph graph2) {
		this.graph = (IGraph) graph2;
		this.nodesSearch = 0;
		this.toSearch = new ArrayList<VertexWrapperTest>();
		this.road = new ArrayList<Integer>();
		this.session = null;
		this.edges = new ArrayList<Edge>();
		this.sentVertex = new ArrayList<Integer>();
		this.nodes = new HashMap<Integer, VertexWrapperTest>();
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
		
		// Añade nodos a lista para buscar
		nodesSearch = 0;
		for (int idSearch : nodesNumbers) {
			VertexWrapperTest actVW = new VertexWrapperTest(idSearch);
			//nodes.put(v, actVW); revisar
			toSearch.add(actVW);
			nodesSearch += 1;
		}

		int actColor = -1; // USAR NÚMEROS
		while (toSearch.size() > 0 && roadSize < size * nodesSearch) { // TODO CAMBIAR ESTO
			
			VertexWrapperTest actualVW = toSearch.remove(0);
			
			// Revisa color para avanzar en 1
			if (actualVW.colorNode != actColor) {
				actColor = actualVW.colorNode;
			}
			
			if (actualVW.getSize() >= size) {
				continue;
			}
			
			// Revisa VÉRTICES adyacentes
			for (Integer adjVertex : graph.getAdjacentVertex(actualVW.idVertex)) {
				
				// NO ha sido visitado:
				if (nodes.get(adjVertex) == null) {
					VertexWrapperTest newVW = new VertexWrapperTest (actualVW, adjVertex);
					nodes.put(adjVertex, newVW);
					toSearch.add(newVW);
				}
				
				// SI ha sido visitado Y NO es de cuál vine
				else if (actualVW.fatherNode != adjVertex) {
					
					VertexWrapperTest adjVW = nodes.get(adjVertex);
					
					// MISMO COLOR
					if (adjVW.colorNode == actualVW.colorNode) {
						
						// NUEVA RAMA
						if (road.contains(adjVertex)) {
								
							// Revisar si es un ciclo sobre un mismo nodo:
							
							// Si no, agregarlo
							
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

	public void backTracking (VertexWrapperTest actualVW2) throws IOException {
		ArrayList<VertexWrapperTest> toCheck = new ArrayList<VertexWrapperTest>();
		toCheck.add(actualVW2);
		while (toCheck.size() > 0) {
			VertexWrapperTest actualVW = toCheck.remove(0);
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
			session.sendMessage(new TextMessage(graph.edgeToJson(e,vList)));
		}
		edges.add(e);
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}