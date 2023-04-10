package com.rdfpath.rdfentitypath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.GraphCompDense;
import com.rdfpath.graph.model.GraphFullNative;
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphWrapper;
import com.rdfpath.graph.model.IGraph;


@Component
public class SocketTextHandler extends TextWebSocketHandler {
	public IGraph graph;
	
	public SocketTextHandler () throws IOException, ParseException {
		super();
		
		String path = "subsets/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};//{"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
		int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};
		

		String actFile = (System.getProperty("graph-data") != null) ?
					System.getProperty("graph-data") :
					"subset1000000";
		String filename = path + actFile;
		
		int index = 0;
		while (index < files.length) {
			if (files[index].equals(actFile)) {
				break;
			}
			index ++;
		};
		
		if (index >= files.length) {
			System.out.println("End, file "+filename+" not found");
			System.exit(0);
			return;
		}
		

		System.out.println("==    Cargando Grafo   ==\n"+
				(filename + end) + "\n" +
				(filename + endComp) + "\n" +
				(filename + endNative)); 
		
		//graph = new Graph(path + files[index] + end, true);
		//graph = new GraphNative(path + files[index] + end, true, edgesSize[index]);
		//graph = new GraphCompDense(path + files[index] + endComp, true, nodesSize[index]);
		//graph = new GraphComp(path + files[index] + endComp, true, maxNodeId[index]);
		//graph = new GraphFullNative(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], maxNodeId[index]);
		graph = new GraphFullNative(
				path + files[index] + end,
				path + files[index] + endNative,
				true,
				true,
				edgesSize[index],
				maxNodeId[index]);
		
		System.out.println("==    Grafo Cargado    ==\n");
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		String response = message.getPayload();
		int[] nodesNumbers = Arrays.stream(response.split(",")).mapToInt(Integer::parseInt).toArray();  
		GraphWrapper graphWrapper = new GraphWrapper(graph);
		graphWrapper.setSession(session);
		graphWrapper.search(nodesNumbers, 2);
		System.out.println("end");
	}

}
