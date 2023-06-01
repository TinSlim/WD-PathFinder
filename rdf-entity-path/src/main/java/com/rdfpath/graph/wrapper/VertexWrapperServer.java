package com.rdfpath.graph.wrapper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapperServer {
	public int[] color;
	public int idVertex;
	public int colorNode;
	public int sameColorDistance;
	public HashSet<VertexWrapperServer> from;
	public LinkedList<Integer> added;
	public Boolean inStack;
	public int otherColorDistance;
	public HashSet<Integer> edgesWith;
	public int nodeGrade;
	public int maxNodeGrade;
	public int backTNodeGrade;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperServer(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.sameColorDistance = 0;
		this.from = new HashSet<VertexWrapperServer>();
		this.otherColorDistance = -1;
		edgesWith = null;
		
		nodeGrade = 0;
		maxNodeGrade = 0;
		backTNodeGrade = 1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperServer(VertexWrapperServer actualVW, int adjVertex, int nodeGrade) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		
		this.from = new HashSet<VertexWrapperServer>();
		this.from.add(actualVW);
		
		this.added = new LinkedList<Integer>();
		added.add(actualVW.idVertex);
		
		this.otherColorDistance = -1;
		
		edgesWith = null;
		this.nodeGrade = nodeGrade;
		int[] newColor = {actualVW.color[0] - 26,actualVW.color[1] - 24,actualVW.color[2] - 25};
		this.color = newColor;
		
		this.maxNodeGrade = Math.max(nodeGrade,actualVW.maxNodeGrade);
		backTNodeGrade = -1;
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
	
	public Boolean addFrom (VertexWrapperServer actVW) {
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
	public boolean fromFather(VertexWrapperServer adjVW) {
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
	
	public String getHexColor() {
		String ans = "#";
		String num;
		for (int i = 0;i<3;i++) {
			num = Integer.toHexString(color[i]);
			while (num.length() < 2) {
				num = "0"+num;
			}
			ans = ans+num;
		}
		return ans;
	}
}
