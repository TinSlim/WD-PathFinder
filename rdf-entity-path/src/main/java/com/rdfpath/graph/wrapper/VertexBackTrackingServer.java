/**
 * 
 */
package com.rdfpath.graph.wrapper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class VertexBackTrackingServer {
	LinkedList<Integer> road;
	HashSet<Integer> nodes;
	VertexWrapperServer actVW;
	public int colorDistance;
	public int grade;
	
	public VertexBackTrackingServer (VertexWrapperServer actVW) {
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
	public VertexBackTrackingServer(VertexBackTrackingServer actualBT, VertexWrapperServer vwFrom) {
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
		
		
		if (vwFrom.maxNodeGrade != 1) {
			if (vwFrom.backTNodeGrade < 0) {
				vwFrom.backTNodeGrade = actualBT.actVW.backTNodeGrade;
			}
			else if (actualBT.actVW.backTNodeGrade > vwFrom.nodeGrade) {
				vwFrom.backTNodeGrade = Math.min(actualBT.actVW.backTNodeGrade, vwFrom.backTNodeGrade);
			}
			
		}
	}
}
