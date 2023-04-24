package com.rdfpath.graph.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rdfpath.graph.model.Edge;

public class Utils {
	
	public Utils () {
	}
	
	public static void saveGraph (ArrayList<Edge> edges, String filename) throws IOException {
		System.out.println(filename);
		FileWriter myWriter = new FileWriter(filename);
		for (Edge e : edges) {
			myWriter.write(
					"<http://www.wikidata.org/entity/Q" + Integer.toString(e.getOrigin().getId()) + "> " +
					"<http://www.wikidata.org/prop/direct/P" + Integer.toString(e.getId()) + "> " +
					"<http://www.wikidata.org/entity/Q" + Integer.toString(e.getDestination().getId()) + "> .\n");
		}
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
	}
	
	public static String getEntityName (String id) {
		/*
		 * String url = "https://www.wikidata.org/w/api.php?action=wbgetentities&format=json&ids="
				+ id
				+ "&languages=en&formatversion=2";
		 */

		String url = "https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&search=" + id + "&language=en&formatversion=2";
		String respuesta = "";
		try {
			respuesta = peticionHttpGet(url);
			JSONObject obj = new JSONObject(respuesta);
			if (obj.getJSONArray("search").length() > 0) {
				String label = (((JSONObject) obj.getJSONArray("search").get(0)).getJSONObject("display").getJSONObject("label").getString("value"));
				return label;
			}
		} catch (Exception e) {
			// Manejar excepción
			e.printStackTrace();
		}
		
		return id;
	}
	
	// TODO ACA USA IDIOMA
	public static String getAutocomplete (String label) {
		String entityEncod = URLEncoder.encode(label, StandardCharsets.UTF_8);
		String url = "https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&search="
			+ entityEncod
			+ "&language=en&formatversion=2";
		String respuesta = "";
		
		try {
			respuesta = peticionHttpGet(url);
			JSONObject resp = new JSONObject();
			JSONArray search = new JSONArray();
			JSONObject ans = new JSONObject(respuesta);
			JSONArray arr = ans.getJSONArray("search");
			
			for (Object ob : arr) {
				JSONObject obj = (JSONObject) ob;
				JSONObject newObj = new JSONObject();
				newObj.put("label", obj.getString("label"));
				newObj.put("description", obj.getString("description"));
				newObj.put("id", obj.getString("id"));
				newObj.put("concepturi", obj.getString("concepturi"));
				search.put(newObj);
			}
			resp.put("search", search);
			return resp.toString();
			//return (((JSONObject) obj.getJSONArray("search").get(0)).getJSONObject("display").getJSONObject("label").getString("value"));
		} catch (Exception e) {
			//e.printStackTrace();
			JSONObject resp = new JSONObject();
			JSONArray search = new JSONArray();
			resp.put("search", search);
			return resp.toString();
		}
	}
	

	public static String peticionHttpGet(String urlParaVisitar) throws Exception {
		// Esto es lo que vamos a devolver
		StringBuilder resultado = new StringBuilder();
		// Crear un objeto de tipo URL
		URL url = new URL(urlParaVisitar);

		// Abrir la conexión e indicar que será de tipo GET
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		// Búferes para leer
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		// Mientras el BufferedReader se pueda leer, agregar contenido a resultado
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		// Cerrar el BufferedReader
		rd.close();
		// Regresar resultado, pero como cadena, no como StringBuilder
		return resultado.toString();
	}



	public static BufferedReader readFile (String filename, Boolean isGz) throws IOException {
		if (isGz) {
			FileInputStream stream = new FileInputStream(filename);
			GZIPInputStream gzip = new GZIPInputStream(stream);
			return new BufferedReader(new InputStreamReader(gzip));
		}
		FileInputStream stream = new FileInputStream(filename);
		return new BufferedReader(new InputStreamReader(stream));
	}

}
