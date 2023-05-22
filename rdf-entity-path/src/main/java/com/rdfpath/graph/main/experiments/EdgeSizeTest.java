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
public class EdgeSizeTest {

	public static void main (String[] args) throws IOException {

		if (System.getProperty("subset") == null ||
				System.getProperty("graph") == null ||
				System.getProperty("group") == null) {
			System.out.println("-Dsubset -Dgraph -Dgroup");
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
		
		String[] groupNames = {"double", "triple", "cuadra", "penta"};
		int[] groupSizes = {2, 3, 4, 5};

		
		String actSubset = System.getProperty("subset");
		String graphName = System.getProperty("graph");
		String groupName = System.getProperty("group");
		
		int indexGroup = 0;
		while (true) {
			if (groupNames[indexGroup].equals(groupName)) {
				break;
			}
			indexGroup += 1;
		}
		
		int indexSubset = 0;
		while (true) {
			if (files[indexSubset].equals(actSubset)) {
				break;
			}
			indexSubset += 1;
		}	
		
		IGraph graph;
		
		if (graphName.equals("graphGt")) {
			graph = new Graph(path + files[indexSubset] + end, true);
		}
		else if (graphName.equals("graphNative")) {
			graph = new GraphNative(path + files[indexSubset] + end, true, edgesSize[indexSubset]);
		}
		else if (graphName.equals("graphCompDense")) {
			graph = new GraphCompDense(path + files[indexSubset] + endComp, true, nodesSize[indexSubset]);
		}
		else if (graphName.equals("graphComp")) {
			graph = new GraphComp(path + files[indexSubset] + endComp, true, maxNodeId[indexSubset]);
		}
		else if (graphName.equals("graphNativeFull")) {
			graph = new GraphFullNative(path + files[indexSubset] + end, path + files[indexSubset] + endNative, true, true, edgesSize[indexSubset], maxNodeId[indexSubset]);
		}
		else if (graphName.equals("graphNativeFullDense")) {
			graph = new GraphNativeFullDense(path + files[indexSubset] + end, path + files[indexSubset] + endNative, true, true, edgesSize[indexSubset], nodesSize[indexSubset]);
		}
		else {
			System.out.println("Fail");
			return;
		}	
		
		BufferedReader fileBuff = Utils.readFile(path+"sparqlGroups/"+files[indexSubset]+"_random_"+groupNames[indexGroup]+".csv",false);
		
		String line = "";
        String[] tempArr;
        line = fileBuff.readLine();
        tempArr = line.split(";");
        //int [][] ids = new int[100][2];
        int [][] ids = new int[100][ groupSizes[indexGroup] ];
        String[] tempArr1;
        for (int j = 0; j < tempArr.length; j++) {
        	tempArr1 = tempArr[j].split(",");
        	for (int r=0;r<groupSizes[indexGroup];r++) {
        		ids[j][r] = Integer.parseInt(tempArr1[r]);
        	}
        	
        }

		
		int i = 0;
		int seconds = 60 * 60 * 24 * 360;
		System.out.println("Estructura;ID;Grupos;Aristas;Tiempo;Memoria");
		MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
		
		while (i < ids.length) {
			System.gc();
			System.out.print(graph.getStructName()+";"+i+";"+groupNames[indexGroup]+";");
			
			long memoryBefore = mbean.getHeapMemoryUsage().getUsed();
			int[] a = ids[i];
			GraphWrapperTimeTest nGW = new GraphWrapperTimeTest(graph,seconds);
			nGW.search(a, 2);
			System.out.println((mbean.getHeapMemoryUsage().getUsed() - memoryBefore));
			System.gc();
			i++;
		}
		
	}
}