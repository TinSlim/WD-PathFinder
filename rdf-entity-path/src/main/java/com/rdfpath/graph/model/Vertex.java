package com.rdfpath.graph.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Vertex {

    public int id; // TODO hacer privado
    private List<Edge> adjacentEdges;
    
    
    public Vertex(Integer id) {
    	this.adjacentEdges = new LinkedList<>();
    	this.id = id;
    }

    public void addEdge(Edge edge) {
        adjacentEdges.add(edge);
    }

    public Integer getId() {
        return id;
    }

    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }
    
    public void checkTime (int seconds, long startTime) throws InterruptedException {
		if ( (System.currentTimeMillis() - startTime) > seconds * 1000 ) {
			if (System.getProperty("debug") != null) {
				System.out.println("GetAdjVertex");
			}
			throw new InterruptedException("Done Time");
		}
		/*
		 if (Thread.interrupted()) {
		    throw new InterruptedException();
		}
		 */
	}
    
    public HashSet<Integer> getAdjacentVertex() {
    	List<Edge> actEdges = getAdjacentEdges();
    	HashSet<Integer> vertexSet = new HashSet<Integer>();
    	for (Edge e : actEdges) {
    		vertexSet.add(e.getOppositeVertex(this).getId());
    	}
    	return vertexSet;
    }
    
    public HashSet<Integer> getAdjacentVertexTimeout(int seconds, long startTime) throws InterruptedException {
    	List<Edge> actEdges = getAdjacentEdges();
    	HashSet<Integer> vertexSet = new HashSet<Integer>();
    	for (Edge e : actEdges) {
    		checkTime(seconds, startTime);
    		vertexSet.add(e.getOppositeVertex(this).getId());
    	}
    	return vertexSet;
    }

    public void setAdjacentEdges(List<Edge> adjacentEdges) {
        this.adjacentEdges = adjacentEdges;
    }

    public Edge getEdge(Vertex destination, boolean weightedEdges) {
    	Edge edge = null;
    	for (Edge e : adjacentEdges) {
            if(e.getOppositeVertex(this).equals(destination))
                return e;
        }
    	return edge;
    }
    
    public ArrayList<Edge> getEdges(Vertex destination) {
    	ArrayList<Edge> edgesList = new ArrayList<Edge>();
    	for (Edge e : adjacentEdges) {
            if(e.getOppositeVertex(this).equals(destination))
                edgesList.add(e);
        }
    	return edgesList;
    }
    
    public List<Edge> getOutgoingEdges() {
    	List<Edge> edges = new ArrayList<Edge>();
    	for (Edge e : adjacentEdges) {
            if(e.isOutgoing(this))
            	edges.add(e);
    	}
    	return edges;
    }

}
