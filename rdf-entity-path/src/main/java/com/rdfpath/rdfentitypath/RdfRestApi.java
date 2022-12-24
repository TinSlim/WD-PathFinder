package com.rdfpath.rdfentitypath;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rdfpath.graph.algorithms.BFSMix;
import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

@RestController
public class RdfRestApi {
	Graph graph = null;

	@RequestMapping("/querylol")
	public String home() throws IOException {//throws IOException {
		Vertex a = new Vertex (2);
		System.out.println(a);
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		graph = new Graph(filename); // TODO esta linea pide IOExcept
		System.out.println(graph.getNodes().get(1).myFather);
		System.out.println("graph2");
		
		BFSMix bfsAlg = new BFSMix(graph);
		
		Integer[] nodesNumbers = {18,20,19};
		ArrayList<Vertex> listNodes = new ArrayList<Vertex> ();
		String newName = "";
		for (Integer i : nodesNumbers) {
			listNodes.add(graph.getNodes().get(i));
			newName = String.join("_",newName,Integer.toString(i));
		}
		
		bfsAlg.setSearchNodes(listNodes);
		ArrayList<Edge> edges = bfsAlg.getRoadsOnline(3);
		System.out.println(edges);
		System.out.println("-------------");
	    return "---beser--";
	}
	
	@RequestMapping("/autocomplete")
	public ResponseEntity<String> autocomplete(@RequestParam String entity) {
		String entityEncod = URLEncoder.encode(entity, StandardCharsets.UTF_8);
		String uri = "https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&search="
			+ entityEncod
			+ "&language=en&formatversion=2";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> answer = restTemplate.getForEntity(uri, String.class);
		return answer;
	}
	
	@RequestMapping("/info")
	public ResponseEntity<String> info(@RequestParam String entity) {
		String entityEncod = URLEncoder.encode(entity, StandardCharsets.UTF_8);
		String uri = "https://www.wikidata.org/w/api.php?action=wbgetentities&format=json&ids="
			+ entityEncod
			+ "&languages=en&formatversion=2";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> answer = restTemplate.getForEntity(uri, String.class);
		return answer;
	}
}
