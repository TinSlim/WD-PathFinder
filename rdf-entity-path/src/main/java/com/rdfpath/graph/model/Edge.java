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

}
