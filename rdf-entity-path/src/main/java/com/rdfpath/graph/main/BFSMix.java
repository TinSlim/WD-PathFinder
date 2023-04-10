/**
 * 
 */
package com.rdfpath.graph.main;

import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class BFSMix implements Callable<String> {
	LinkedList<Integer> list;
	public int ind;
	
	public BFSMix (LinkedList<Integer> list) {
		this.ind = 0;
		this.list = list;
	}
	
    @Override
    public String call() throws Exception {
        int i = 0;
    	while (i < 99999) {
    		ind++;
    		list.add(i);
        	//Thread.sleep(10);
        	i++;
        	
        	// Revisa interrupción
        	if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
                throw new InterruptedException();
             }
        }
    	System.out.println("do" + ind);
    	//Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
        return "Ready!";
    }
}