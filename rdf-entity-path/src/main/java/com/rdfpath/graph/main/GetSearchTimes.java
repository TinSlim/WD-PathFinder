package com.rdfpath.graph.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.IGraph;
public class GetSearchTimes {

	public static void main (String[] args) {
		System.out.println("empieza");
		IGraph graph;
		ArrayList<Integer> ids;
		Random rand = new Random();
		
		File csvOutputFile = new File("CSV_FILE_NAME.csv");
		File csvMakeTime = new File("make_time.csv");
		
		String path = "src/main/resources/nt/";
		String[] files = {"subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		
		int [] nodesSize = {8163693};
		int [] maxNodeId = {10000000};
		int [] edgesSize = {42340828};
		System.out.println("Entra al try");
		try {
			PrintWriter pw = new PrintWriter(csvOutputFile);
			pw.println("Estructura;Tiempos;ID;SetDatos");
			// Obtiene ids
			for (int i = 0; i < 1; i++) {
				System.out.println("Usando: subset");
				// TODO Calcular tiempo
				System.out.println("Carga grafo comprimido denso");
				graph = new GraphCompDense(path + files[i] + endComp, true, nodesSize[i]);
				System.out.println("Obtención id aleatorios");
				// Obtiene números aleatorios
				ids = new ArrayList();
				while (ids.size() < 10000) {
					int newNum = rand.nextInt(nodesSize[i]); // 90510
		            if ( ! ids.contains(((GraphCompDense) graph).nodes[newNum][0][0])) {
		            	ids.add( ((GraphCompDense) graph).nodes[newNum][0][0] );
		            }
			    }
				System.out.println("Escribe datos grafo comprimido denso 1/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				System.out.println("Carga grafo");
				graph = new Graph(path + files[i] + end, true);
				
				System.out.println("Escribe datos grafo clases 2/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				System.out.println("Carga grafo");
				graph = new GraphNative(path + files[i] + end, true, edgesSize[i]);
				
				System.out.println("Escribe datos grafo nativo 3/4");
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				System.out.println("Carga grafo");
				graph = new GraphComp(path + files[i] + endComp, true, maxNodeId[i]);
				
				System.out.println("Escribe datos grafo comprimido 4/4");
				graph.writeSearchAdj(ids,pw,files[i]);

			}
			
			pw.close();
			System.out.println("Archivo cerrado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
