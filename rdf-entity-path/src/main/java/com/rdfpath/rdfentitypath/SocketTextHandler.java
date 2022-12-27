package com.rdfpath.rdfentitypath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.rdfpath.graph.algorithms.BFSMix;
import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphWrapper;
import com.rdfpath.graph.model.Vertex;


@Component
public class SocketTextHandler extends TextWebSocketHandler {
	public Graph graph;
	
	public SocketTextHandler () throws IOException {
		super();
		System.out.println("== Cargando Grafo ==\n");
		String filename = "/nt/subset100000.nt";// star.nt"
		System.out.println("--"+filename+"\n");
		graph = new Graph(filename);
		System.out.println("== Grafo Cargado ==\n");		
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
		String response = message.getPayload();
		int[] nodesNumbers = Arrays.stream(response.split(",")).mapToInt(Integer::parseInt).toArray();  
		GraphWrapper graphWrapper = new GraphWrapper(graph);
		graphWrapper.setSession(session);
		graphWrapper.search(nodesNumbers, 2);
	}

}
