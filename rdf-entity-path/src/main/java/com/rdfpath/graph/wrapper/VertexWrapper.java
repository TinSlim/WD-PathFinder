/**
 * 
 */
package com.rdfpath.graph.wrapper;

import java.util.ArrayList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapper {
	public int idVertex;
	public int fatherNode;
	
	public int colorNode;
	public ArrayList<VertexWrapper> from;
	
	/**
	 * @param idSearch
	 */
	public VertexWrapper(Integer idSearch) {
		// TODO Auto-generated constructor stub
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.from = new ArrayList<VertexWrapper>();
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapper(VertexWrapper actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.fatherNode = actualVW.idVertex;
		this.from = new ArrayList<VertexWrapper>();
		this.from.add(actualVW);
	}

}
