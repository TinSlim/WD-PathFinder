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
		IGraph graph;
		ArrayList<Integer> ids;
		Random rand = new Random();
		
		File csvOutputFile = new File("CSV_FILE_NAME.csv");
		File csvMakeTime = new File("make_time.csv");
		
		String[] files = {"subset100"};
		int [] nodesSize = {2};
		int [] maxNodeId = {10};
		try {
			PrintWriter pw = new PrintWriter(csvOutputFile);
			pw.println("Estructura;Tiempos;ID;SetDatos");
			// Obtiene ids
			for (int i = 1; i < 1; i++) {
				
				// TODO Calcular tiempo
				graph = new GraphCompDense(files[i], true, nodesSize[i]);
				
				// Obtiene números aleatorios
				ids = new ArrayList();
				while (ids.size() < 10000) {
					int newNum = rand.nextInt(nodesSize[i]);
		            if ( ! ids.contains(((GraphCompDense) graph).nodes[newNum][0][0])) {
		            	ids.add(newNum);
		            }
			    }
				
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				graph = new Graph(files[i], true);
				
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				graph = new GraphNative(files[i], true);
				
				graph.writeSearchAdj(ids,pw,files[i]);
				
				// Calcular tiempo
				graph = null;
				graph = new GraphComp(files[i], true, maxNodeId[i]);
				
				graph.writeSearchAdj(ids,pw,files[i]);

			}
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
