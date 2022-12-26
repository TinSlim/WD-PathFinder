package com.rdfpath.graph.model;

import java.util.ArrayList;

public class VertexWrapper {
	public Vertex node;
	
	public Vertex fatherNode;
	public Vertex colorNode;
	public ArrayList<VertexWrapper> from;
	
	// No viene de alguien
	public VertexWrapper (Vertex n) {
		this.node = n;
		this.colorNode = n;
		this.fatherNode = null;
		this.from = new ArrayList<VertexWrapper>();;
	}
	
	// Viene de alguien
	public VertexWrapper (VertexWrapper vw, Vertex n) {
		this.node = n;
		this.colorNode = vw.colorNode;
		this.fatherNode = vw.node;
		this.from = new ArrayList<VertexWrapper>();
		this.from.add(vw);
	}
	
}
