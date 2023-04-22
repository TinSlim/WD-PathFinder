/**
 * 
 */
package com.rdfpath.graph.main.experiments;

import com.rdfpath.graph.model.GraphWrapper3;
import com.rdfpath.graph.model.IGraph;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class LongRunningTask implements Runnable {
    
	
	/**
	 * @param nGW
	 */
	public LongRunningTask() {
	}
	
	@Override
    public void run() {
		 try {
            System.out.println("starting sleep!");
            Thread.sleep(10000);
            System.out.println("woke up!");
          }
          catch (InterruptedException e) {
        	  System.out.println("was interrupted!");
          }
		
    }
}
