/**
 * 
 */
package com.rdfpath.graph.main.experiments;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphNativeFullDense;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;
import com.rdfpath.graph.wrapper.GraphWrapperTimeTest;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class PathFindTest {

	  
	public static void main (String[] args) throws IOException {

		if (System.getProperty("subset") == null || System.getProperty("graph") == null) {
			System.out.println("-Dsubset -Dgraph");
			return;
		}		

		String path = "subsets/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};

		int index = 0;
		String actSubset = System.getProperty("subset");
		String graphName = System.getProperty("graph");
		while (index < files.length) {
			if (files[index].equals(actSubset)) {
				break;
			}
			index += 1;
		}	
		
		IGraph graph;
		
		if (graphName.equals("graphGt")) {
			graph = new Graph(path + files[index] + end, true);
		}
		else if (graphName.equals("graphNative")) {
			graph = new GraphNative(path + files[index] + end, true, edgesSize[index]);
		}
		else if (graphName.equals("graphCompDense")) {
			graph = new GraphCompDense(path + files[index] + endComp, true, nodesSize[index]);
		}
		else if (graphName.equals("graphComp")) {
			graph = new GraphComp(path + files[index] + endComp, true, maxNodeId[index]);
		}
		else if (graphName.equals("graphNativeFull")) {
			graph = new GraphFullNative(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], maxNodeId[index]);
		}
		else if (graphName.equals("graphNativeFullDense")) {
			graph = new GraphNativeFullDense(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], nodesSize[index]);
		}
		else {
			System.out.println("Fail");
			return;
		}	
		
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
		int seconds = 60;
		System.out.println("Estructura;ID;Aristas;Tiempo;Memoria");
		MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
		
		while (i < ids.length) {
			
			
			System.out.print(graph.getStructName()+";"+i+";");
			
			long memoryBefore = mbean.getHeapMemoryUsage().getUsed();
			int[] a = {ids[i][0], ids[i][1]};
			GraphWrapperTimeTest nGW = new GraphWrapperTimeTest(graph,seconds);
			nGW.search(a, 3);
			//System.gc();
			System.out.println((mbean.getHeapMemoryUsage().getUsed() - memoryBefore));
			System.gc();
			
			
			i++;
		}
		
	}
}