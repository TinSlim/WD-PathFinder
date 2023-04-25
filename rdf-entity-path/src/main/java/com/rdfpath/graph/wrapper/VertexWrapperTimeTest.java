package com.rdfpath.graph.wrapper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapperTimeTest {
	public int idVertex;
	public int colorNode;
	public int sameColorDistance;
	public HashSet<VertexWrapperTimeTest> from;
	public LinkedList<Integer> added;
	public Boolean inStack;
	public int otherColorDistance;
	public HashSet<Integer> edgesWith;
	//public int father;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperTimeTest(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.sameColorDistance = 0;
		this.from = new HashSet<VertexWrapperTimeTest>();
		this.added = new LinkedList<Integer>();
		this.otherColorDistance = -1;
		edgesWith = new HashSet<Integer>();
		//this.father = -1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperTimeTest(VertexWrapperTimeTest actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		this.from = new HashSet<VertexWrapperTimeTest>();
		this.from.add(actualVW);
		this.added = new LinkedList<Integer>();
		added.add(actualVW.idVertex);
		this.otherColorDistance = -1;
		edgesWith = new HashSet<Integer>();
		//this.father = actualVW.idVertex;
	}
	
	public Boolean addFrom (VertexWrapperTimeTest actVW) {
		if (!from.contains(actVW)) {
			this.added.add(actVW.idVertex);
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
	public boolean fromFather(VertexWrapperTimeTest adjVW) {
		if (added.size() == 0) {
			return false;
		}
		else if (added.get(0) == adjVW.idVertex) {
			return true;
		}
		return false;
	}
	
	public void removeFather( ) {
		added.pop();
		
	}

	/**
	 * @param v1
	 * @return
	 */
	public boolean hasEdgeWith(int v1) {
		return edgesWith.contains(v1);
	}

	public void addEdgeWith(int v1) {
		edgesWith.add(v1);
	}

}
