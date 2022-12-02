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
import com.rdfpath.graph.model.Vertex;


@Component
public class SocketTextHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
		String response = message.getPayload();
		int[] nodesNumbers = Arrays.stream(response.split(",")).mapToInt(Integer::parseInt).toArray();  
		String filename = "/nt/star.nt";
		Graph graph = new Graph(filename);
		BFSMix bfsAlg = new BFSMix(graph);
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		//Integer[] nodesNumbers = {12,25};//{4,31,22};
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		String newName = "";
		for (Integer i : nodesNumbers) {
			listNodes.add(nodes.get(i));
			newName = String.join("_",newName,Integer.toString(i));
		}
		
		bfsAlg.setSearchNodes(listNodes);
		ArrayList<Edge> edges = bfsAlg.getRoadsOnlineServer(5,session);
	}

}
