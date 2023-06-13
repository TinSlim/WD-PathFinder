package com.rdfpath.graph.wrapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphWrapperServer {
	private String[] palette = {"#ADF7B6","#A0CED9","#FCF5C7","#FFEE93", "#FFC09F"};
	private int[][] paletteRGB = {
		{173,247,182}, {160,206,217}, {252,245,199}, {255,238,147}, {255,192,159}
		};
	public long startTime;
	
	private HashMap<Integer, VertexWrapperServer> nodes;
	private IGraph graph;
	private HashSet<Integer> addedNodes;
	private WebSocketSession session;
	private int totalEdges;
	private int[] nodesNumbers;
	private int actualDistance;
	private String lang;
	
	@SuppressWarnings("unchecked")
	public CharSequence vertexWrapperUpdateToJson (VertexWrapperServer vw) {
		JSONObject newVertex = new JSONObject();
		newVertex.put("id", vw.idVertex);
		newVertex.put("nodeGrade", vw.backTNodeGrade);
	    
		newVertex.put("roadSize", vw.sameColorDistance + vw.otherColorDistance);
		
		JSONObject json = new JSONObject();
		json.put("type","edit");
	    json.put("data",newVertex);
	    
	    return json.toString();
	}
	
	@SuppressWarnings("unchecked")
	public CharSequence vertexWrapperToJson (VertexWrapperServer vw, float angle) {
		// Node
		String vertexLabelSmall = null;
		String vertexDescription = null;
		
		String[] vertexLabel;
		try {
			vertexLabel = Utils.getEntityName(vw.idVertex, lang, true);
			vertexLabelSmall = vertexLabel[0];
			vertexDescription = vertexLabel[1];
		} catch (IOException e) {
		}
		
	    
		String title = "Q" + vw.idVertex;
		if (vertexLabelSmall != null) {title += " - " + vertexLabelSmall;};
		if (vertexDescription != null) {title += "\n" + vertexDescription;};
	    
	    JSONObject newVertex = new JSONObject();
	    newVertex.put("label", vertexLabelSmall != null ? vertexLabelSmall : "" + vw.idVertex );
	    newVertex.put("color",vw.getHexColor());
	    newVertex.put("title", title);
	    newVertex.put("id", vw.idVertex);
	    newVertex.put("edgeSize", 0);
	    newVertex.put("size", 18);
	    
	    newVertex.put("roadSize", vw.sameColorDistance + vw.otherColorDistance);
	    newVertex.put("nodeGrade", vw.backTNodeGrade);
	    
	    JSONObject font = new JSONObject();
    	font.put("align", "middle");
    	//font.put("strokeColor", "#f5f5f5"); // TODO color
    	font.put("size", 18);
    	newVertex.put("font", font);
	    
	    try {
			String imageUrl = Utils.getImage("Q" + vw.idVertex);
			if (imageUrl != "") {
				newVertex.put("shape", "image");
				newVertex.put("image", imageUrl);
			}
		} catch (Exception e) {
			
		}
		
	    
	    	
	    // Json
	    JSONObject json = new JSONObject();
	    json.put("type","vertex");
	    json.put("data",newVertex);
	    
	    double angleR = Math.toRadians(angle);
	    if (angle != -1) {
	    	newVertex.put("x", Math.cos(angleR) * 500);
		    newVertex.put("y", Math.sin(angleR) * 500);
		    newVertex.put("fixed",true);
	    }
	    
	    return json.toString();
	}
	

	private CharSequence ping() {
		JSONObject json = new JSONObject();
	    json.put("type","ping");
		return json.toString();
	}
	
	@SuppressWarnings("unchecked")
	public CharSequence vertexWrapperEditPositionJson (VertexWrapperServer vw, float angle) {
		// Node	    
	    JSONObject newVertex = new JSONObject();
	    
	    // Json
	    JSONObject json = new JSONObject();
	    json.put("type","edit");
	    json.put("data",newVertex);
	    
	    double angleR = Math.toRadians(angle);
	    if (angle != -1) {
	    	newVertex.put("x", Math.cos(angleR) * actualDistance);
		    newVertex.put("y", Math.sin(angleR) * actualDistance);
		    newVertex.put("fixed",true);
	    }
	    newVertex.put("id", vw.idVertex);
	    return json.toString();
	}

	public GraphWrapperServer (IGraph graph2) {
		this.graph = (IGraph) graph2;
		this.nodes = new HashMap<Integer, VertexWrapperServer>();
		this.addedNodes = new HashSet<Integer>();
		this.session = null;
		this.totalEdges = 0;
	}
	
	public void setSession (WebSocketSession session) {
		this.session = session;
	}

	public void search (int [] nodesNumbers, int size, int maxEdgeSize) throws IOException {
		startTime = System.currentTimeMillis();
		this.nodesNumbers = nodesNumbers;
		
		LinkedList<VertexWrapperServer> toSearch = new LinkedList<VertexWrapperServer>();
		HashSet<Integer> nodesNumbersSet = new HashSet<Integer>();
		
		actualDistance = 500;
		int index = 0;
		for (int idSearch : nodesNumbers) {
			VertexWrapperServer actVW = new VertexWrapperServer(idSearch);
			actVW.color = paletteRGB[index % paletteRGB.length];
			nodes.put(idSearch, actVW);
			nodesNumbersSet.add(idSearch);
			toSearch.push(actVW);
			addedNodes.add(idSearch);
			session.sendMessage(new TextMessage(
					vertexWrapperToJson (actVW, (360/nodesNumbers.length)*index)));
			index++;
		}
		

		while (toSearch.size() > 0) {
			checkConn();

			VertexWrapperServer actualVW = toSearch.pop();
			if (actualVW.sameColorDistance > (size/2) + size%2) {
				continue;
			}

			// Revisa VÉRTICES adyacentes
			for (Integer adjVertex : graph.getAdjacentVertexSessionLimited(actualVW.idVertex, session, maxEdgeSize, actualVW.added == null)) {
				checkConn();

				// Así no cicla en el mismo nodo
				if (actualVW.idVertex == adjVertex) {
					continue;
				}

				// NO ha sido visitado:
				if (nodes.get(adjVertex) == null) {
					VertexWrapperServer newVW = new VertexWrapperServer (actualVW, adjVertex,graph.getGrade(adjVertex)); //  Math.log10( -- )
					nodes.put(adjVertex, newVW);
					toSearch.add(newVW);
				}
				
				// SI ha sido visitado
				else {
					VertexWrapperServer adjVW = nodes.get(adjVertex);
					
					// Si es el que lo agregó a la lista
					if (actualVW.fromFather(adjVW)) {
						actualVW.removeFather();
						continue;
					}
					
					// Mismo color
					if (actualVW.colorNode == adjVW.colorNode) {

						// Revisar si se agregó, ESTÁ EN PATH
						if (adjVW.otherColorDistance > -1 && adjVW.otherColorDistance + actualVW.sameColorDistance + 1 <= size) {
							LinkedList<Integer> unionNodes = new LinkedList<Integer>();
							actualVW.otherColorDistance = adjVW.otherColorDistance + 1;
							unionNodes.push(adjVW.idVertex);
							unionNodes.push(actualVW.idVertex);
							
							
							
							
							int bTGrade = Math.max(adjVW.backTNodeGrade, actualVW.maxNodeGrade);
							if (actualVW.backTNodeGrade < 0) actualVW.backTNodeGrade =  bTGrade;
							
						    // TODO
							// WB encuentra USA y usa su grado
							
							makeEdges(unionNodes);
							backTracking(actualVW, size, nodesNumbersSet);
						}
						
						else if (adjVW.addFrom(actualVW)) {
							toSearch.add(adjVW);
						}
					}
					
					else {
						if (adjVW.sameColorDistance + actualVW.sameColorDistance + 1 <= size) { //
							// Configuración de color TODO quitar o mantener
							int[] newColor = {
									(adjVW.color[0] + actualVW.color[0]) / 2,
									(adjVW.color[1] + actualVW.color[1]) / 2,
									(adjVW.color[2] + actualVW.color[2]) / 2,
							};
							
							if (adjVW.sameColorDistance == actualVW.sameColorDistance) {
								adjVW.color = newColor;
								actualVW.color = newColor;
							}
							else {
								adjVW.color = newColor;
							}
							
							if (adjVW.otherColorDistance != -1) {
								adjVW.otherColorDistance = Math.min(adjVW.otherColorDistance,actualVW.sameColorDistance + 1);
							} else {
								adjVW.otherColorDistance = actualVW.sameColorDistance + 1;
							}
							
							if (actualVW.otherColorDistance != -1) {
								actualVW.otherColorDistance = Math.min(actualVW.otherColorDistance,actualVW.sameColorDistance + 1);
							} else {
								actualVW.otherColorDistance = actualVW.sameColorDistance + 1;
							}
							int bTGrade = Math.max(adjVW.maxNodeGrade, actualVW.maxNodeGrade);
							
							adjVW.backTNodeGrade = (adjVW.backTNodeGrade < 0) ? bTGrade : Math.min(bTGrade,adjVW.backTNodeGrade);
							actualVW.backTNodeGrade = (actualVW.backTNodeGrade < 0) ? bTGrade : Math.min(bTGrade,actualVW.backTNodeGrade);
							
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
	
	private void checkConn() throws IOException {
		if ( (System.currentTimeMillis() - startTime) > 1 * 500 ) { // 1 cada medio segundo
			session.sendMessage(new TextMessage(ping()));
			if (!session.isOpen() ) {
				throw new IOException();
			}
			startTime = System.currentTimeMillis();
		}
	}

	public void backTracking(VertexWrapperServer vw, int maxSize, HashSet<Integer> nodesNumbers) throws IOException {
		LinkedList<VertexBackTrackingServer> stack = new LinkedList<VertexBackTrackingServer>();
		stack.push(new VertexBackTrackingServer(vw));
		while (stack.size() > 0) {
			checkConn();
			VertexBackTrackingServer actualBT = stack.pop();
			if (actualBT.colorDistance + actualBT.grade > maxSize) {
				continue;
			}
			
			if (nodesNumbers.contains(actualBT.actVW.idVertex)) {
				makeEdges(actualBT.road);
			}
			
			for (VertexWrapperServer vwFrom: actualBT.actVW.from) {
				checkConn();
				if (! actualBT.nodes.contains(vwFrom.idVertex)) {
					VertexBackTrackingServer vbtNew = new VertexBackTrackingServer(actualBT, vwFrom);
					stack.push(vbtNew);
				}				
			}
		}
	}

	public void makeEdges (LinkedList<Integer> nodesList) throws IOException {
		if (nodesList.size() < 2) {
			return;
		}
		int i = 0;
		while (i < nodesList.size() - 1) {
			checkConn();
			sendEdges(nodesList.get(i), nodesList.get(i + 1));
			i++;
		}
		return;
	}
	
	public void sendEdges (int v1, int v2) throws IOException {
		checkConn();
		VertexWrapperServer vw1 = nodes.get(v1);
		VertexWrapperServer vw2 = nodes.get(v2);
		if (vw1.hasEdgeWith(v2) || vw2.hasEdgeWith(v1)) {
		return;
		}
		
		if (vw1.edgesWith != null) {
			vw1.addEdgeWith(v2);
		}
		else {
			vw2.addEdgeWith(v1);
		}
		
		//vw1.addEdgeWith(v2);
		//vw2.addEdgeWith(v1);
		
		ArrayList edges = graph.getEdges(v1, v2);
		// AddedNodes se puede borrar usando size de edgesNodes
		if (!addedNodes.contains(v1)) {
			addedNodes.add(v1);
			session.sendMessage(new TextMessage(vertexWrapperToJson(vw1,-1)));
		}
		else {
			session.sendMessage(new TextMessage(vertexWrapperUpdateToJson(vw1)));
		}
		
		if (!addedNodes.contains(v2)) {
			addedNodes.add(v2);
			session.sendMessage(new TextMessage(vertexWrapperToJson(vw2,-1)));
		}
		else {
			session.sendMessage(new TextMessage(vertexWrapperUpdateToJson(vw2)));
		}
		
		for (Object edge : edges) {
			checkConn();
			totalEdges+=1;
			
			int edgeRoadSize = Math.max(vw1.sameColorDistance + vw1.otherColorDistance, vw2.sameColorDistance + vw2.otherColorDistance);
			// TODO extensión del grafo
			if (actualDistance + 24 < totalEdges * 11) { // TODO Corregir tamaño
				actualDistance = totalEdges * 10;
				int index = 0;
				for (int idSearch : nodesNumbers) {
					VertexWrapperServer editVW = nodes.get(idSearch);
					session.sendMessage(new TextMessage(
							vertexWrapperEditPositionJson(editVW, (360/nodesNumbers.length)*index)));
					index++;
				}
			}
			session.sendMessage(new TextMessage(edgeToJson(edge,edgeRoadSize)));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public CharSequence edgeToJson(Object e, int roadSize) {
    	JSONObject json = new JSONObject();
    	
    	// Edge data
    	String[] edgeLabel;
    	String edgeLabelSmall = "";
		try {
			edgeLabel = Utils.getEntityName(graph.getPredicateEdge(e), lang, false);
			edgeLabelSmall = edgeLabel[0];
		} catch (IOException e1) {
		}
    	
    	// Arrow Config
    	JSONObject arrowInfo = new JSONObject();
    	arrowInfo.put("enabled", true);
    	arrowInfo.put("type", "arrow");
    	
    	JSONObject arrow = new JSONObject();
    	arrow.put("to", arrowInfo);
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", graph.getOriginEdge(e));
    	edge.put("to", graph.getDestinationEdge(e));
    	edge.put("labelWiki", graph.getPredicateEdge(e));
    	edge.put("label", edgeLabelSmall);
    	edge.put("title", edgeLabelSmall);
    	edge.put("roadSize", roadSize);

    	JSONObject smooth = new JSONObject();
    	smooth.put("type", "dynamic"); //dynamic curvedCCW discrete cubicBezier
    	smooth.put("roundness", 0.7);
    	edge.put("smooth", smooth);
    	
    	JSONObject font = new JSONObject();
    	font.put("align", "middle");
    	font.put("strokeColor", "#f5f5f5"); // TODO color
    	font.put("size", 18);
    	edge.put("font", font);
    	
    	edge.put("color", new JSONObject().put("color", "#848484")); // TODO color
    	edge.put("arrows", arrow);
    	
    	// TODO Fuerza aristas 500
    	//edge.put("length", 900);
    	
    	json.put("type", "edge");
    	json.put("data", edge);
    	
    	return json.toString();
    }

	/**
	 * @param string
	 */
	public void setLang(String language) {
		this.lang = language;
		
	}
}