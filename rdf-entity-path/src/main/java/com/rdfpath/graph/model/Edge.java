package com.rdfpath.graph.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Edge  {
    private final Integer id;
    private final Vertex origin;
    private final Vertex destination;
    private double weight;
    
    public Edge(Integer id, Vertex origin, Vertex destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.weight = 1;
    }
    
    public Edge(Integer id, Vertex origin, Vertex destination, double weight) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }
    
    public double getWeight() {
    	return weight;
    }
    
    public void setWeight(double weight) {
    	this.weight = weight;
    }
    
    public Vertex getOrigin() {
    	return origin;
    }
    
    public Vertex getDestination() {
        return destination;
    }
    
    public Vertex getOppositeVertex(Vertex node) {
        return origin != node ? origin : destination;
    }

    public Boolean isOutgoing(Vertex node) {
        return node.equals(origin);
    }

    @Override
    public String toString() {
        return origin + "->" + id + "->" + destination;
    }
    
    public String toJson (ArrayList<Vertex> vertexList) {
    	String message;
    	JSONObject json = new JSONObject();
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", origin);
    	edge.put("to", destination);
    	edge.put("label", id);
    	edge.put("font", new JSONObject().put("align", "middle"));
    	edge.put("color", new JSONObject().put("color", "#848484"));
    	edge.put("arrows", new JSONObject().put("to", new JSONObject().put("enabled", true).put("type", "arrow")));

    	// Vertex
    	JSONArray vertexArray = new JSONArray();
    	for (Vertex v : vertexList) {
    		String color = (v.father == v) ? "#cc76FC" : "#97C2FC";
    		vertexArray.put(new JSONObject().put("id", v).put("label", v).put("color",color));
    	}
    	json.put("edge", edge);
    	json.put("vertex", vertexArray);
    	message = json.toString();
    	
    	return message;
    }

}
