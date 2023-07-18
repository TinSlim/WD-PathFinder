package com.rdfpath.graph.wrapper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapperTimeTestOptMem {
	public int idVertex;
	public int colorNode;
	public boolean initial;
	
	public int sameColorDistance;
	public int otherColorDistance;
	
	public HashSet<VertexWrapperTimeTestOptMem> from;
	public HashSet<Integer> edgesWith;
	
	public int queueTimes;

	
	/**
	 * @param idSearch
	 */
	public VertexWrapperTimeTestOptMem(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		initial = true;
		
		this.sameColorDistance = 0;
		this.otherColorDistance = -1;
		
		this.from = new HashSet<VertexWrapperTimeTestOptMem>();
		edgesWith = null;
		
		queueTimes = 1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperTimeTestOptMem(VertexWrapperTimeTestOptMem actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		this.otherColorDistance = -1;
		
		this.from = new HashSet<VertexWrapperTimeTestOptMem>();
		this.from.add(actualVW);
		edgesWith = null;
		
		queueTimes = 1;
	}
	

	public boolean onlyFather (VertexWrapperTimeTestOptMem adjVW) {
		if (from != null && from.size() == 1 && from.contains(adjVW)) {
			return true;
		}
		return false;
	}


	/**
	 * @param v1
	 * @return
	 */
	public boolean hasEdgeWith(int v1) {
		if (edgesWith == null) {
			return false;
		}
		return edgesWith.contains(v1);
	}

	public void addEdgeWith(int v1) {
		if (edgesWith == null) {
			edgesWith = new HashSet<Integer>();
		}
		edgesWith.add(v1);
	}

}
