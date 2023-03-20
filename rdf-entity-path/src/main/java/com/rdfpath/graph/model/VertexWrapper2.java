/**
 * 
 */
package com.rdfpath.graph.model;

import java.util.ArrayList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapper2 {
	public int idVertex;
	public int fatherNode;
	
	public int colorNode;
	public ArrayList<VertexWrapper2> from;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapper2(Integer idSearch) {
		// TODO Auto-generated constructor stub
		idVertex = idSearch;
		colorNode = idSearch;
		this.from = new ArrayList<VertexWrapper2>();
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapper2(VertexWrapper2 actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.fatherNode = actualVW.idVertex;
		this.from = new ArrayList<VertexWrapper2>();
		this.from.add(actualVW);
	}

}
