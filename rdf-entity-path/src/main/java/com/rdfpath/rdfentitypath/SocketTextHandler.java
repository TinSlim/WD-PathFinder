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
import com.rdfpath.graph.model.GraphNative;
import com.rdfpath.graph.model.GraphWrapper2;
import com.rdfpath.graph.model.IGraph;


@Component
public class SocketTextHandler extends TextWebSocketHandler {
	public IGraph graph;
	
	public SocketTextHandler () throws IOException, ParseException {
		super();
		
		String path = "src/main/resources";
		String filename = path + "/nt/star_compressed_struct.gz";
		System.out.println("==    Cargando Grafo   ==");
		System.out.println("Archivo: "+ filename+ "\n");
		if (System.getProperty("graph-path") != null) {
			System.out.println("USING GRAPH FROM VAR\n\n");
			filename = System.getProperty("graph-path");
			graph = new GraphComp(filename, true, 98347590);
		}
		else {
			graph = new GraphComp(filename, true, 37);//89968);
		}
		//String filename = "/nt/subset100000.nt"; //myGraph.nt;//star.nt";// subset100000.nt"
		System.out.println("==    Grafo Cargado    ==\n");
		//System.out.println("==   Test Mismo grupo  ==");
		//graph.diffGroups();
		//System.out.println("==    Test terminado   ==\n");
		//System.out.println("== Test Distinto grupo ==");
		//graph.sameGroups();
		//System.out.println("==  Test terminado  ==\n");
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		String response = message.getPayload();
		int[] nodesNumbers = Arrays.stream(response.split(",")).mapToInt(Integer::parseInt).toArray();  
		GraphWrapper2 graphWrapper = new GraphWrapper2(graph);
		graphWrapper.setSession(session);
		graphWrapper.search(nodesNumbers, 2);
		System.out.println("end");
	}

}
