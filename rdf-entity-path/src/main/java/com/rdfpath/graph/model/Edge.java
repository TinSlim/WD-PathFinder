package com.rdfpath.graph.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rdfpath.graph.utils.Utils;

public class Edge  {
    public final int id;
    public final Vertex origin;
    public final Vertex destination;
    
    public Edge(Integer id, Vertex origin, Vertex destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
    }

    public Integer getId() {
        return id;
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
    	
    	String edgeLabel = Utils.getEntityName("P"+id+"&type=property");
    	String edgeLabelSmall = edgeLabel;
    	if (edgeLabel.length() > 7) {edgeLabelSmall = edgeLabel.substring(0,Math.min(edgeLabel.length(), 7)) + "...";}
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", origin);
    	edge.put("to", destination);
    	//edge.put("label", "K"+id);
    	edge.put("label", edgeLabelSmall);//Utils.getEntityName("P" + id));
    	edge.put("title", edgeLabel);
    	edge.put("font", new JSONObject().put("align", "middle"));
    	edge.put("color", new JSONObject().put("color", "#848484"));
    	edge.put("arrows", new JSONObject().put("to", new JSONObject().put("enabled", true).put("type", "arrow")));
    	edge.put("length", 500);
    	// Vertex
    	JSONArray vertexArray = new JSONArray();
    	for (Vertex v : vertexList) {
    		String color = (v.father == v) ? "#cc76FC" : "#97C2FC";
    		String vertexLabel = Utils.getEntityName("Q" + v);
        	String vertexLabelSmall = vertexLabel;
        	if (vertexLabel.length() > 7) {vertexLabelSmall = vertexLabel.substring(0,Math.min(vertexLabel.length(), 7)) + "...";}
    		vertexArray.put(
    				new JSONObject()
    				.put("id", v)
    				//.put("label",Utils.getEntityName("Q" + v) + "_" + v)
    				.put("label", vertexLabelSmall)
    				.put("title", vertexLabel)
    				.put("color",color));
    	}
    	json.put("edge", edge);
    	json.put("vertex", vertexArray);
    	message = json.toString();
    	
    	return message;
    }

}
