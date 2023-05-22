package com.rdfpath.graph.wrapper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexBackTrackingTimeTestOptMem {
	LinkedList<Integer> road;
	HashSet<Integer> nodes;
	VertexWrapperTimeTestOptMem actVW;
	public int colorDistance;
	public int grade;
	
	public VertexBackTrackingTimeTestOptMem (VertexWrapperTimeTestOptMem actVW) {
		road = new LinkedList<>();
		nodes = new HashSet<>();
		this.actVW = actVW;
		road.add(actVW.idVertex);
		nodes.add(actVW.idVertex);
		grade = actVW.sameColorDistance;
		colorDistance = actVW.otherColorDistance;
	}

	
	/**
	 * @param actualBT
	 */
	public VertexBackTrackingTimeTestOptMem(VertexBackTrackingTimeTestOptMem actualBT, VertexWrapperTimeTestOptMem vwFrom) {
		road = (LinkedList<Integer>) actualBT.road.clone();
		nodes = (HashSet<Integer>) actualBT.nodes.clone();
		road.add(vwFrom.idVertex);
		nodes.add(vwFrom.idVertex);
		this.actVW = vwFrom;
		
		colorDistance = actualBT.colorDistance + 1;
		this.colorDistance = actualBT.colorDistance + 1;
		if (vwFrom.otherColorDistance > colorDistance + 1) {
			vwFrom.otherColorDistance = colorDistance + 1;
		}
		grade = vwFrom.sameColorDistance;
	}
}
