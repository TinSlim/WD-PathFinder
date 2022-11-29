package com.rdfpath.graph.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Vertex {

    public Integer id; // TODO hacer privado
    private List<Edge> adjacentEdges = new LinkedList<>();
    private double degree;
    private double degreeNormalized;
    private double distance = Integer.MAX_VALUE;
    public ArrayList<Vertex> from; //TODO make private
    public Vertex father;
    public Vertex myFather;
    
    public Vertex(Integer id) {
    	this.from = new ArrayList<Vertex>();
        this.id = id;
        this.degree = 1;
        this.degreeNormalized = 1;
    }

    public void addEdge(Edge edge) {
        adjacentEdges.add(edge);
    }

    public Integer getId() {
        return id;
    }

    public double getDegree() {
        return degree;
    }

    public double getDegreeNormalized() {
        return degreeNormalized;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public void setDegreeNormalized(double degreeNormalized) {
        this.degreeNormalized = degreeNormalized;
    }

    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }
    
    public List<Vertex> getAdjacentVertex() {
    	List<Edge> actEdges = getAdjacentEdges();
    	List <Vertex> vertexList = new LinkedList<>();
    	for (Edge e : actEdges) {
    		vertexList.add(e.getOppositeVertex(this));
    	}
    	return vertexList;
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return id.toString();
    }

	public void setDistance(double d) {
        this.distance = d;
    }

	public double getDistance() {
		return this.distance;
	}
	
	public double getBaselineWeight () {
		return 1;
	}

}
