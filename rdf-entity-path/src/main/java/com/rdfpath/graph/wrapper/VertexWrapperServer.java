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
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperServer(int idSearch) {
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.sameColorDistance = 0;
		this.from = new HashSet<VertexWrapperServer>();
		this.added = new LinkedList<Integer>();
		this.otherColorDistance = -1;
		edgesWith = new HashSet<Integer>();
		//this.father = -1;
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperServer(VertexWrapperServer actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.sameColorDistance = actualVW.sameColorDistance + 1;
		
		this.from = new HashSet<VertexWrapperServer>();
		this.from.add(actualVW);
		
		this.added = new LinkedList<Integer>();
		added.add(actualVW.idVertex);
		
		this.otherColorDistance = -1;
		
		edgesWith = new HashSet<Integer>();
		
		int[] newColor = {actualVW.color[0] - 26,actualVW.color[1] - 24,actualVW.color[2] - 25};
		this.color = newColor;
		//this.father = actualVW.idVertex;
	}
	
	public Boolean addFrom (VertexWrapperServer actVW) {
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
	public boolean fromFather(VertexWrapperServer adjVW) {
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
