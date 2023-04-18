/**
 * 
 */
package com.rdfpath.graph.model;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexBackTracking {
	LinkedList<Integer> road;
	HashSet<Integer> nodes;
	VertexWrapper2 actVW;
	public int colorDistance;
	public int grade;
	
	public VertexBackTracking (VertexWrapper2 actVW) {
		road = new LinkedList<>();
		nodes = new HashSet<>();
		this.actVW = actVW;
		road.add(actVW.idVertex);
		nodes.add(actVW.idVertex);
		grade = actVW.grade;
		colorDistance = actVW.colorDistance;
	}

	/**
	 * @param actualBT
	 */
	public VertexBackTracking(VertexBackTracking actualBT, VertexWrapper2 vwFrom) {
		road = (LinkedList<Integer>) actualBT.road.clone();
		nodes = (HashSet<Integer>) actualBT.nodes.clone();
		road.add(vwFrom.idVertex);
		nodes.add(vwFrom.idVertex);
		this.actVW = vwFrom;
		
		colorDistance = actualBT.colorDistance + 1;
		this.colorDistance = actualBT.colorDistance + 1;
		if (vwFrom.colorDistance > colorDistance + 1) {
			vwFrom.colorDistance = colorDistance + 1;
		}
		grade = vwFrom.grade;
	}
}
