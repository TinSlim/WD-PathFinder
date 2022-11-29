package com.rdfpath.graph.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

public class BFSMix {
	private ArrayList<Vertex> nodesSearch;
	private ArrayList<Vertex> toSearch;
	private ArrayList<Vertex> toJson;
	private List<Vertex> road;
	
	public BFSMix(Graph graph) {
		this.road = new LinkedList<Vertex>();
		this.toSearch = new ArrayList<Vertex>();
		this.toJson = new ArrayList<Vertex>();
		this.nodesSearch = new ArrayList<Vertex>();
	}
	
	public void setSearchNodes (ArrayList<Vertex> vertexList) {
		nodesSearch = vertexList;
		for (Vertex v : vertexList) {
			toSearch.add(v);
			v.father = v;
			v.myFather = v;
		}
	}

	public ArrayList<Edge> getRoads (int size) {
		System.out.println("GET ROADS");
		int x = 0;
		
		Vertex actFather = null;
	
		while (toSearch.size() > 0 && x < size * nodesSearch.size()) {
	
			Vertex actualV = toSearch.remove(0);

			// Other color
			if (actualV.father != actFather) {
				x+=1;
				actFather = actualV.father;
			}
			
			// Check adjacent
			for (Vertex v : actualV.getAdjacentVertex()) {

				// Adjacent didn't have color, uses my color
				if (v.father == null) {
					v.father = actualV.father;
					v.myFather = actualV;
					v.from.add(actualV);
					toSearch.add(v);
				}
				
				// Adjacent had color
				else {

					// We don't have the same color, a new RELATION
					if (v.father != actualV.father) {
						v.from.add(actualV);
						if (!road.contains(v)) road.add(v);
					}
					
					// We have the same color, can be a ROAD
					else {
						// If the adjacent is from other Vertex, we have a ROAD
						if (v.myFather != actualV && actualV.myFather != v) {
							actualV.from.add(v);
						}
					}
				}
			}
		}
		
		// We get the Edges for RELATIONS doing a backtracking
		ArrayList<Vertex> vList = new ArrayList<Vertex>();
		ArrayList<Edge> eList = new ArrayList<Edge>();
		while (road.size() > 0) {
			Vertex aVertex = road.remove(0);
			if (! vList.contains(aVertex)) {
				vList.add(aVertex);
				for (Vertex v : aVertex.from) {
					if (!road.contains(v)) {
						road.add(v);
					}
					//------------ If can exists more than 1 edge between vertexes -----------
					ArrayList<Edge> edges = aVertex.getEdges(v);
					for (Edge e : edges) {
						if (! eList.contains(e)) eList.add(e);
					}
					//------------ Else -----------
					/*Edge nEdge = aVertex.getEdge(v,false);
					if (! eList.contains(nEdge)) eList.add(nEdge);*/
					//-----------------------------
				}
			}
		}
		System.out.println("EXECUTE 5 end");
		
		System.out.println("----ROAD----");
		System.out.println(vList);
		System.out.println("------------");
		
		System.out.println("----EDGES----");
		System.out.println(eList);
		System.out.println("-------------");
		return eList;
	}
	

	public ArrayList<Edge> getRoadsOnline (int size) {
		System.out.println("EXECUTE 5");
		int x = 0;
		ArrayList<Edge> eList = new ArrayList<Edge>();
		Vertex actFather = null;

		while (toSearch.size() > 0 && x < size * nodesSearch.size()) {
			Vertex actualV = toSearch.remove(0);

			// Other color
			if (actualV.father != actFather) {
				x+=1;
				actFather = actualV.father;
			}
			
			// Check adjacent
			for (Vertex v : actualV.getAdjacentVertex()) {
				
				// Adjacent didn't have color, uses my color, first time visited
				if (v.father == null) {
					v.father = actualV.father;
					v.myFather = actualV;
					v.from.add(actualV);
					toSearch.add(v);
				}
				
				// Adjacent had color
				else {

					// We don't have the same color, a new RELATION
					if (v.father != actualV.father) {
						v.from.add(actualV);
						
						ArrayList<Vertex> tempList = new ArrayList<Vertex>();
						ArrayList<Vertex> tempList2 = new ArrayList<Vertex>();
						tempList.add(v);
						while (tempList.size() > 0) {
							Vertex act = tempList.remove(0);
							tempList2.add(act);
							for (Vertex vv : act.from) {
								if(!tempList.contains(vv) && !tempList2.contains(vv)) tempList.add(vv);
							}
						}

						for (Vertex vert : tempList2) {
							for (Vertex vert2 : vert.from) {
								ArrayList<Edge> edges = vert.getEdges(vert2);
								for (Edge e : edges) {
									if (! eList.contains(e)) {
										eList.add(e);
										System.out.println(e);
										//TimeUnit.SECONDS.sleep(1);
									}
								}
							}								
							if (!road.contains(vert)) road.add(vert);
						}

					}
					else {
						if (v.myFather != actualV && actualV.myFather != v) { 
							actualV.from.add(v);
							
							if (road.contains(actualV)) {
								System.out.println("entre aca");
								System.out.println("entre aca");
								ArrayList<Vertex> tempList = new ArrayList<Vertex>();
								ArrayList<Vertex> tempList2 = new ArrayList<Vertex>();
								tempList.add(actualV);
								while (tempList.size() > 0) {
									Vertex act = tempList.remove(0);
									tempList2.add(act);
									for (Vertex vv : act.from) {
										if(!tempList.contains(vv) && !tempList2.contains(vv)) tempList.add(vv);
									}
								}

								for (Vertex vert : tempList2) {
									for (Vertex vert2 : vert.from) {
										ArrayList<Edge> edges = vert.getEdges(vert2);
										for (Edge e : edges) {
											if (! eList.contains(e)) {
												eList.add(e);
												System.out.println(e);
												//TimeUnit.SECONDS.sleep(1);
											}
										}
									}								
									if (!road.contains(vert)) road.add(vert);
								}
							}
						}	
					}
					
				}
				
			}
			
		}
		
		System.out.println("----EDGES----");
		System.out.println(eList);
		System.out.println("-------------");
		return eList;
		}

	public ArrayList<Edge> getRoadsOnlineServer (int size,WebSocketSession session) throws IOException {
		System.out.println("EXECUTE 5");
		int x = 0;
		ArrayList<Edge> eList = new ArrayList<Edge>();
		Vertex actFather = null;

		while (toSearch.size() > 0 && x < size * nodesSearch.size()) {

			Vertex actualV = toSearch.remove(0);

			// Other color
			if (actualV.father != actFather) {
				x+=1;
				actFather = actualV.father;
			}
			
			// Check adjacent
			for (Vertex v : actualV.getAdjacentVertex()) {
				
				// Adjacent didn't have color, uses my color
				if (v.father == null) {
					v.father = actualV.father;
					v.myFather = actualV;
					v.from.add(actualV);
					toSearch.add(v);
				}
				
				// Adjacent had color
				else {

					// We don't have the same color, a new RELATION
					if (v.father != actualV.father) {
						v.from.add(actualV);
						
						ArrayList<Vertex> tempList = new ArrayList<Vertex>();
						ArrayList<Vertex> tempList2 = new ArrayList<Vertex>();
						tempList.add(v);
						while (tempList.size() > 0) {
							Vertex act = tempList.remove(0);
							tempList2.add(act);
							for (Vertex vv : act.from) {
								if(!tempList.contains(vv) && !tempList2.contains(vv)) tempList.add(vv);
							}
						}

						for (Vertex vert : tempList2) {
							for (Vertex vert2 : vert.from) {
								ArrayList<Edge> edges = vert.getEdges(vert2);
								for (Edge e : edges) {
									if (! eList.contains(e)) {
										eList.add(e);
										ArrayList<Vertex> temp3 = new ArrayList<Vertex>();
										if (!toJson.contains(vert2)) {
											temp3.add(vert2);
											toJson.add(vert2);
										}
										if (!toJson.contains(vert)) {
											temp3.add(vert);
											toJson.add(vert);
										}
										session.sendMessage(new TextMessage(e.toJson(temp3)));
										//TimeUnit.SECONDS.sleep(1);
									}
								}
							}								
							if (!road.contains(vert)) road.add(vert);
						}
					}
					else {
						if (v.myFather != actualV && actualV.myFather != v) { 
								actualV.from.add(v);
								
								if (road.contains(actualV)) {
									ArrayList<Vertex> tempList = new ArrayList<Vertex>();
									ArrayList<Vertex> tempList2 = new ArrayList<Vertex>();
									tempList.add(actualV);
									while (tempList.size() > 0) {
										Vertex act = tempList.remove(0);
										tempList2.add(act);
										for (Vertex vv : act.from) {
											if(!tempList.contains(vv) && !tempList2.contains(vv)) tempList.add(vv);
										}
									}

									for (Vertex vert : tempList2) {
										for (Vertex vert2 : vert.from) {
											ArrayList<Edge> edges = vert.getEdges(vert2);
											for (Edge e : edges) {
												if (! eList.contains(e)) {
													eList.add(e);
													ArrayList<Vertex> temp3 = new ArrayList<Vertex>();
													if (!toJson.contains(vert2)) {
														temp3.add(vert2);
														toJson.add(vert2);
													}
													if (!toJson.contains(vert)) {
														temp3.add(vert);
														toJson.add(vert);
													}
													session.sendMessage(new TextMessage(e.toJson(temp3)));
													//TimeUnit.SECONDS.sleep(1);
												}
											}
										}								
										if (!road.contains(vert)) road.add(vert);
									}
								}
								
							
						
						}
						
					}
					
				}
				
			}
		}

		System.out.println("----EDGES----");
		System.out.println(eList);
		System.out.println("-------------");
		return eList;
		}
}
	
	