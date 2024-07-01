/**
 * 
 */
package com.rdfpath.graph.main.experiments;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphNativeFullDense;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class LoadTimeMem {

	public static void main (String[] args) {
		if (System.getProperty("subset") == null || System.getProperty("graph") == null) {
			System.out.println("-Dsubset -Dgraph");
			return;
		}		
		
		IGraph graph;
		int[] ids;


		String path = "subsets/";
		//String path = "subsets_old/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322,99609308};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996,117288116};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619,715906922};

		int index = 0;
		String actSubset = System.getProperty("subset");
		String graphName = System.getProperty("graph");
		while (index < files.length) {
			if (files[index].equals(actSubset)) {
				break;
			}
			index += 1;
		}		
		
		System.out.println("Indice: "+index);
		System.out.println("Subset: "+actSubset);
		System.out.println("Graph : "+graphName);
		
		File csvOutputFileGraphs = new File("lts/lts_"+graphName + "_" + actSubset+".csv");
		
		System.out.println("Entra al try");
		try {
			PrintWriter pwGraphs = new PrintWriter(csvOutputFileGraphs);
			pwGraphs.println("Estructura;SetDatos;TiempoCreación;UsoMemoria");

			System.out.println("Usando: " + files[index]);
	        
	        // Variables para calcular info del grafo
	        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
			long startMakeTime;
			long endMakeTime;
			MemoryUsage beforeHeapMemoryUsage;
			MemoryUsage afterHeapMemoryUsage;
			
			graph = null;
			System.out.println("Carga grafo");
			
			beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
			startMakeTime = System.currentTimeMillis();

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
				pwGraphs.close();
				return;
			}
			endMakeTime = System.currentTimeMillis();
			afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
			
			System.out.println("Escribe memoria grafo clases");
			pwGraphs.println(graph.getStructName() +";"+ files[index]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
	
			System.out.println("Terminó subset\n");
			pwGraphs.close();
			System.out.println("Archivo cerrado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

