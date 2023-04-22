/**
 * 
 */
package com.rdfpath.graph.main.experiments;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};
		String end = ".nt.gz";
		//String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
		//int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};
		
		int index = 1;
		
		IGraph graph;
		System.out.println("Loading graph");
		graph = new GraphNativeFullDense(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], nodesSize[index]);
		System.out.println("GraphLoaded");
	
		
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

		
		int i = 0;
		System.out.println("Estructura;ID;Aristas;Tiempo;Memoria");
		MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
		
		while (i < ids.length) {
			
			
			System.out.print(graph.getStructName()+";"+i+";");
			
			long memoryBefore = mbean.getHeapMemoryUsage().getUsed();
			int[] a = {ids[i][0], ids[i][1]};
			GraphWrapper3 nGW = new GraphWrapper3(graph);
			WeakReference a1 = new WeakReference(nGW);
			nGW.search(a, 3);
			System.out.println((mbean.getHeapMemoryUsage().getUsed() - memoryBefore));
			nGW = null;

			while (a1.get() != null) {
				System.gc();
			}
			i++;
		}
		
	}
}