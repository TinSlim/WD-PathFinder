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
	public int sameColorDistance;
	public HashSet<VertexWrapperTimeTestOptMem> from;
	public LinkedList<Integer> added;
	public Boolean inStack;
	public int otherColorDistance;
	public HashSet<Integer> edgesWith;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperTimeTestOptMem(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.sameColorDistance = 0;

		this.from = new HashSet<VertexWrapperTimeTestOptMem>();
		this.added = new LinkedList<Integer>();
		
		this.otherColorDistance = -1;

		edgesWith = null;
		//edgesWith = new HashSet<Integer>();
		//this.father = -1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperTimeTestOptMem(VertexWrapperTimeTestOptMem actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		
		this.from = new HashSet<VertexWrapperTimeTestOptMem>();
		this.from.add(actualVW);
		
		this.added = new LinkedList<Integer>();
		added.add(actualVW.idVertex);
		
		this.otherColorDistance = -1;
		
		edgesWith = null;
		//edgesWith = new HashSet<Integer>();
	}
	
	public void addVertexAdded (int idVertex) {
		if (added == null) {
			this.added = new LinkedList<Integer>();
		}
		added.add(idVertex);
	}
	
	public void removeFather () {
		added.pop();
		if (added.size() == 0) {
			this.added = null;
		}
	}
	
	public Boolean addFrom (VertexWrapperTimeTestOptMem actVW) {
		if (!from.contains(actVW)) {
			addVertexAdded(actVW.idVertex);
			this.from.add(actVW);
			return true;
		}
		this.from.add(actVW);
		return false;
		
	}

	/**
	 * @param adjVW
	 * @return
	 */
	public boolean fromFather(VertexWrapperTimeTestOptMem adjVW) {
		if (added == null) {
			return false;
		}
		else if (added.get(0) == adjVW.idVertex) {
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
