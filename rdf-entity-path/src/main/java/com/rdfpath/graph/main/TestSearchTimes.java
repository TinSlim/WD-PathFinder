package com.rdfpath.graph.main;
import java.io.BufferedReader;
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
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.utils.Utils;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class TestSearchTimes {

	public static void main (String[] args) {
		if (System.getProperty("subset") == null || System.getProperty("graph") == null) {
			return;
		}		
		
		IGraph graph;
		int[] ids;


		String path = "subsets/";
		//String path = "subsets_old/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000"};//{"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619};

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
		
		File csvOutputFileGraphs = new File("timeResults/tiemposGrafos_"+graphName + "_" + actSubset+".csv");
		File csvOutputFile = new File("timeResults/tiempos_"+graphName + "_" + actSubset+".csv");
		
		
		System.out.println("Entra al try");
		try {
			
			PrintWriter pwGraphs = new PrintWriter(csvOutputFileGraphs);
			PrintWriter pw = new PrintWriter(csvOutputFile);
			pwGraphs.println("Estructura;SetDatos;TiempoCreación;UsoMemoria");
			pw.println("Estructura;Tiempos;ID;SetDatos");

			System.out.println("Usando: " + files[index]);
			
			// Obtiene números aleatorios
			BufferedReader fileBuff = Utils.readFile(path+files[index]+"_random_values.csv",false);
			String line = "";
	        String[] tempArr;
	        line = fileBuff.readLine();
	        tempArr = line.split(",");
	        ids = new int[tempArr.length];
	        for (int j = 0; j < tempArr.length; j++) {
	        	ids[j] = Integer.parseInt(tempArr[j]);
	        }
	        
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
			endMakeTime = System.currentTimeMillis();
			afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
			
			System.out.println("Escribe memoria grafo clases");
			pwGraphs.println(graph.getStructName() +";"+ files[index]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
			System.out.println("Escribe datos grafo clases");
			graph.writeSearchAdj(ids,pw,files[index]);
			
			System.out.println("Terminó subset\n");
			pw.close();
			pwGraphs.close();
			System.out.println("Archivo cerrado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
