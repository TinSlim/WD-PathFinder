package com.rdfpath.rdfentitypath;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;
import com.rdfpath.graph.utils.Utils;

@RestController
public class RdfRestApi {
	Graph graph = null;

	@RequestMapping("/querylol") // borrar
	public String home() throws IOException {//throws IOException {
	    return "---beser--";
	}
	
	@RequestMapping("/json")
	public ResponseEntity<String> json (@RequestParam String entity) {
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
	
	@RequestMapping(value="/autocomplete",produces = MediaType.APPLICATION_JSON_VALUE)
	public String autocomplete (@RequestParam String entity) {
		String entityEncod = URLEncoder.encode(entity, StandardCharsets.UTF_8);
		String url = "https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&search="
			+ entityEncod
			+ "&language=en&formatversion=2";
		String respuesta = "";
		
		try {
			// Petición y creación de respuesta (JSON)
			respuesta = Utils.peticionHttpGet(url);
			JSONObject resp = new JSONObject();
			JSONArray search = new JSONArray();
			JSONObject ans = new JSONObject(respuesta);
			JSONArray arr = ans.getJSONArray("search");
			
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = (JSONObject) arr.get(i);
				JSONObject newObj = new JSONObject();
				newObj.put("label", obj.getString("label"));
				//newObj.put("description", obj.getString("description"));
				newObj.put("id", obj.getString("id"));
				newObj.put("concepturi", obj.getString("concepturi"));
				search.put(newObj);
			}
			resp.put("search", search);
			return resp.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
