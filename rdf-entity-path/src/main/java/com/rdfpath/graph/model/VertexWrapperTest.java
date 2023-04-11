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
public class VertexWrapperTest {
	public int idVertex;
	public int fatherNode;
	
	public int colorNode;
	public ArrayList<VertexWrapperTest> from;
	
	public int[] union = {-1,-1};
	public int[] sizes = {0,0};
	
	/**
	 * @param idSearch
	 */
	public VertexWrapperTest(Integer idSearch) {
		// TODO Auto-generated constructor stub
		this.idVertex = idSearch;
		this.colorNode = idSearch;
		this.from = new ArrayList<VertexWrapperTest>();
	}

	/**
	 * @param actualVW
	 * @param adjVertex
	 */
	public VertexWrapperTest(VertexWrapperTest actualVW, int adjVertex) {
		this.idVertex = adjVertex;
		this.colorNode = actualVW.colorNode;
		this.fatherNode = actualVW.idVertex;
		this.sizes[0] = actualVW.getMinSize() + 1;
		this.sizes[0] = 0;
		this.from = new ArrayList<VertexWrapperTest>();
		this.from.add(actualVW);
	}
	
	public int getSize () {
		return sizes[0] + sizes[1];
	}
	
	public int getMinSize () {
		if (sizes[0] < sizes[1]) {
			return sizes[0];
		}
		return sizes[1];
	}
}
