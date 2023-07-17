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
	public boolean initial;
	
	public int sameColorDistance;
	public int otherColorDistance;

	public HashSet<VertexWrapperServer> from;
	public HashSet<Integer> edgesWith;
	
	public int nodeGrade;
	public int maxNodeGrade;
	public int backTNodeGrade;
	
	public int queueTimes;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperServer(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		initial = true;
		
		
		this.sameColorDistance = 0;
		this.otherColorDistance = -1;
		
		this.from = new HashSet<VertexWrapperServer>();
		this.edgesWith = null;
		
		nodeGrade = 0;
		maxNodeGrade = 0;
		backTNodeGrade = 1;
		
		queueTimes = 1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperServer(VertexWrapperServer actualVW, int adjVertex, int nodeGrade) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		int[] newColor = {actualVW.color[0] - 26,actualVW.color[1] - 24,actualVW.color[2] - 25};
		this.color = newColor;
		initial = false;
		
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		this.otherColorDistance = -1;
		
		this.from = new HashSet<VertexWrapperServer>();
		this.from.add(actualVW);
		edgesWith = null;
		
		this.nodeGrade = nodeGrade;	
		this.maxNodeGrade = Math.max(nodeGrade,actualVW.maxNodeGrade);
		backTNodeGrade = -1;
		
		queueTimes = 1;
	}
	
	
	public boolean onlyFather (VertexWrapperServer adjVW) {
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
