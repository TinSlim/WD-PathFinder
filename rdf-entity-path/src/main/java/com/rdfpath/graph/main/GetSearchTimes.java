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
public class GetSearchTimes {

	public static void main (String[] args) {
		IGraph graph;
		int[] ids;
		
		File csvOutputFileGraphs = new File("tiemposGrafos.csv");
		File csvOutputFile = new File("tiempos.csv");

		//String path = "subsets/";
		String path = "subsets_old/";
		String[] files = {"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		
		int [] nodesSize = {8163693};//{92633, 829294, 8163693};
		int [] maxNodeId = {10000000};//{100000, 1000000, 10000000};
		int [] edgesSize = {42340828};//{598840, 5955914, 42340828};
		System.out.println("Entra al try");
		try {
			PrintWriter pwGraphs = new PrintWriter(csvOutputFileGraphs);
			PrintWriter pw = new PrintWriter(csvOutputFile);
			pwGraphs.println("Estructura;SetDatos;TiempoCreación;UsoMemoria");
			pw.println("Estructura;Tiempos;ID;SetDatos");

			for (int i = 0; i < 1; i++) { // TODO al exportar al servidor usar 3 y no 1
				System.out.println("Usando: " + files[i]);
				
				// Obtiene números aleatorios
				BufferedReader fileBuff = Utils.readFile(path+files[i]+"_random_values.csv",false);
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
				graph = new Graph(path + files[i] + end, true);
				endMakeTime = System.currentTimeMillis();
				afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
				
				System.out.println("Escribe memoria grafo clases 1/4");
				pwGraphs.println(graph.getStructName() +";"+ files[i]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
				System.out.println("Escribe datos grafo clases 1/4");
				graph.writeSearchAdj(ids,pw,files[i]);


				graph = null;
				System.out.println("Carga grafo");
				
				beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
				startMakeTime = System.currentTimeMillis();
				graph = new GraphNative(path + files[i] + end, true, edgesSize[i]);
				endMakeTime = System.currentTimeMillis();
				afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
				
				System.out.println("Escribe memoria grafo nativo 2/4");
				pwGraphs.println(graph.getStructName() +";"+ files[i]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
				
				System.out.println("Escribe datos grafo nativo 2/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				
				graph = null;
				System.out.println("Carga grafo comprimido denso");

				beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
				startMakeTime = System.currentTimeMillis();
				graph = new GraphCompDense(path + files[i] + endComp, true, nodesSize[i]);
				endMakeTime = System.currentTimeMillis();
				afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
				
				
				System.out.println("Escribe memoria grafo comprimido denso 3/4");
				pwGraphs.println(graph.getStructName() +";"+ files[i]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
				System.out.println("Escribe datos grafo comprimido denso 3/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				
				
				
				
				
				// Calcular tiempo
				graph = null;
				System.out.println("Carga grafo");
				
				beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
				startMakeTime = System.currentTimeMillis();
				graph = new GraphComp(path + files[i] + endComp, true, maxNodeId[i]);
				endMakeTime = System.currentTimeMillis();
				afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
				
				System.out.println("Escribe memoria grafo comprimido 4/4");
				pwGraphs.println(graph.getStructName() +";"+ files[i]+";"+(endMakeTime-startMakeTime)+";"+(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed()));
		
				System.out.println("Escribe datos grafo comprimido 4/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				System.out.println("Terminó un subset\n");

			}
			
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
