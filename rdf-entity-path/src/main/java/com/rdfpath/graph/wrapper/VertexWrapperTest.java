/**
 * 
 */
package com.rdfpath.graph.wrapper;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexWrapperTest {
	public int vertexID;
	public VertexWrapperTest father;
	//public LinkedList<Integer> path;
	public int initId;
	public int actSize;
	
	/**
	 * @param x
	 */
	public VertexWrapperTest(int x) {
		this.vertexID = x;
		this.father = null;
		//path = new LinkedList<Integer>();
		//path.add(x);
		this.initId = x;
		this.actSize = 0;
	}
	
	/**
	 * @param actVW
	 */
	public VertexWrapperTest(VertexWrapperTest actVW, int neighbor) {
		this.vertexID = neighbor;
		this.father = actVW;
		//this.path = (LinkedList<Integer>) actVW.path.clone();
		this.initId = actVW.initId;
		this.actSize = actVW.actSize + 1;
	}

	public String toString() {
        return vertexID + " | (" + father + ") | " + initId + " | " + actSize;
    }

}