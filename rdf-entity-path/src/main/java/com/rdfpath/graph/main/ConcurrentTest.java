/**
 * 
 */
package com.rdfpath.graph.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphNativeFullDense;
import com.rdfpath.graph.model.GraphWrapper3;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class ConcurrentTest {

	  
	public static void main (String[] args) throws IOException {

		String path = "subsets/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};//{"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};
		
		int index = 1;
		
		IGraph graph;
		graph = new GraphNativeFullDense(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], nodesSize[index]);
		System.out.println("GraphLoaded");
		
		/*
		BufferedReader fileBuff = Utils.readFile(path+files[index]+"_random_pairs.csv",false);
		String line = "";
        String[] tempArr;
        line = fileBuff.readLine();
        tempArr = line.split(";");
        int [][] ids = new int[100][2];
        String[] tempArr1;
        for (int j = 0; j < tempArr.length; j++) {
        	tempArr1 = tempArr[j].split(",");
        	ids[j][0] = Integer.parseInt(tempArr1[0]);
        	ids[j][1] = Integer.parseInt(tempArr1[1]);
        }
		*/
		
		int i = 0;
		ExecutorService executor;
		
		while (i < 5) {
			executor = Executors.newSingleThreadExecutor();
			int[] a = {261, 298};
			GraphWrapper3 nGW = new GraphWrapper3(graph);
			nGW.search(a, 3);
			
			Future future = executor.submit(new LongRunningTask(nGW));
			//try {
			//    future.get(12, TimeUnit.SECONDS);
			//} catch (TimeoutException e) {
			//    future.cancel(true);
			//} catch (Exception e) {
			//    // handle other exceptions
			//} finally {
			//   executor.shutdownNow();
			//}
			//lrt.stop();
			System.out.println("Terminó el: " + i);
			i++;
		}
		
	}
}