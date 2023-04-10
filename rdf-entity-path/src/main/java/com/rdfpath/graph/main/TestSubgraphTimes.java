/**
 * 
 */
package com.rdfpath.graph.main;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class TestSubgraphTimes {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        LinkedList<Integer> list = new LinkedList<Integer>();
        
        BFSMix tarea = new BFSMix(list);
        
        System.out.println("Executor");
        System.out.println(tarea.ind);
        Future<String> future = executor.submit(tarea);
        System.out.println("Try..");
        try {
            System.out.println("Started..");
            System.out.println(tarea.ind);
            System.out.println(future.get(1, TimeUnit.NANOSECONDS));
            System.out.println("Finished!");
            System.out.println(tarea.ind);
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Terminated!");
            System.out.println(tarea.ind);
        }
        if (!executor.isTerminated()) {
            executor.shutdownNow(); // If you want to stop the code that hasn't finished.
        	System.out.println("done");
        }
        
        while (true) {
        	try {
        		if (executor.awaitTermination(1, TimeUnit.SECONDS)) break;
        		} 
        	catch (InterruptedException ie) {}
        }
        
        System.out.println("ie");
        //executor.shutdownNow();
        //System.exit(0);
    }
}

