package com.rdfpath.rdfentitypath;

import java.io.IOException;
import java.util.Arrays;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.rdfpath.graph.model.GraphComp;
import com.rdfpath.graph.model.IGraph;
import com.rdfpath.graph.wrapper.GraphWrapperServer;


@Component
public class SocketTextHandler extends TextWebSocketHandler {
	public IGraph graph;
	Logger logger = LoggerFactory.getLogger(SocketTextHandler.class);
	
	public SocketTextHandler () throws IOException, ParseException {
		super();
		
		long heapSize = Runtime.getRuntime().totalMemory(); 
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		long heapFreeSize = Runtime.getRuntime().freeMemory(); 

		System.out.println(heapSize);
		System.out.println(heapMaxSize);
		System.out.println(heapFreeSize);
		
		String path = "subsets/";
		String[] files = {"subset100000", "subset1000000", "subset10000000", "subset100000000","latest-truthy_small"};//{"subset10000000"};//{"subset100000", "subset1000000", "subset10000000"};
		String end = ".nt.gz";
		String endComp = "_compressed.gz";
		String endNative = "_native.gz";
		
		//int [] nodesSize = {92654, 829794, 8159611, 87110322, 99609308};
		int [] maxNodeId = {100000, 1000000, 10000000, 99999996, 117288116};
		//int [] edgesSize = {600493, 5977585, 42682387, 652319619, 715906922};
		

		String actFile = (System.getProperty("graph-data") != null) ?
					System.getProperty("graph-data") :
					"subset1000000";
					//"subset1000000";
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
		

		logger.info("[Carga Datos] Cargando Grafo: " + filename + endComp);
		//System.out.println("==    Cargando Grafo   ==\n"+
		//		(filename + end) + "\n" +
		//		(filename + endComp) + "\n" +
		//		(filename + endNative)); 
		
		//graph = new Graph(path + files[index] + end, true);
		//graph = new GraphNative(path + files[index] + end, true, edgesSize[index]);
		//graph = new GraphCompDense(path + files[index] + endComp, true, nodesSize[index]);
		//graph = new GraphComp(path + files[index] + endComp, true, maxNodeId[index]);
		//graph = new GraphFullNative(path + files[index] + end, path + files[index] + endNative, true, true, edgesSize[index], maxNodeId[index]);
		graph = new GraphComp(
				path + files[index] + endComp,
				true,
				maxNodeId[index]);
		
		logger.info("[Carga Datos] Grafo Cargado");
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws IOException {
		
		String response = message.getPayload();
		logger.info("[ID:"+session.getId()+"] Consulta: " + response);
		
		String[] splitAns = response.split(",");
		int[] nodesNumbers = Arrays.stream(Arrays.copyOfRange(splitAns,0,splitAns.length - 1)).mapToInt(Integer::parseInt).toArray();  
		
		GraphWrapperServer graphWrapper = new GraphWrapperServer(graph);
		graphWrapper.setSession(session);
		graphWrapper.setLang(splitAns[splitAns.length - 1]);
		
		int sizeSearch = 3;
		if (nodesNumbers.length == 1) {
			sizeSearch = 0;
		}
		
		try {
			graphWrapper.search(nodesNumbers, sizeSearch, 100000);
			session.close();;
		}
		catch (IOException ioE) {
			// Session is closed
		}

		//graphWrapper = null;
		
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
		logger.info("[ID:"+session.getId()+"] Cierre conexión: " + closeStatus.getReason());
	}

}
