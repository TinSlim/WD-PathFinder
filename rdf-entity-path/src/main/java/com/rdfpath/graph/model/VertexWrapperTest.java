/**
 * 
 */
package com.rdfpath.graph.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapperTest {
	public int vertexID;
	public LinkedList<Integer> path;
	public int initId;
	public int actSize;
	
	/**
	 * @param x
	 */
	public VertexWrapperTest(int x) {
		this.vertexID = x;
		path = new LinkedList<Integer>();
		path.add(x);
		this.initId = x;
		this.actSize = 0;
	}
	
	/**
	 * @param actVW
	 */
	public VertexWrapperTest(VertexWrapperTest actVW) {
		this.vertexID = actVW.vertexID;
		this.path = (LinkedList<Integer>) actVW.path.clone();
		this.initId = actVW.initId;
		this.actSize = actVW.actSize + 1;
	}

	public String toString() {
        return vertexID + " | " + path + " | " + initId + " | " + actSize;
    }

}