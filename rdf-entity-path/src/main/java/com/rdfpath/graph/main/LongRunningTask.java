/**
 * 
 */
package com.rdfpath.graph.main;

import com.rdfpath.graph.model.GraphWrapper3;
import com.rdfpath.graph.model.IGraph;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class LongRunningTask implements Runnable {
    public GraphWrapper3 gr3;
    public int[] toSearch;
    
	
	/**
	 * @param nGW
	 */
	public LongRunningTask(GraphWrapper3 nGW) {
		gr3 = nGW;
		// TODO Auto-generated constructor stub
	}

	public void stop() {
		System.out.println("Edges: " + gr3.totalEdges);
		gr3 = null;
		return;
	}
	
	@Override
    public void run() {
		for (int i = 0; i < Long.MAX_VALUE; i++) {
            if(Thread.interrupted()) {
    	    	System.out.println("LRT TimeoutReached");
        		return;
    	    }          	
    	}
    }
}
