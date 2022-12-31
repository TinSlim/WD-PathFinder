package com.rdfpath.graph.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rdfpath.graph.model.Graph;

public class TimeTest {
	public Graph graph;
	public ArrayList groups;
	public int maxSize = 3;
	
	public void buildGraph () throws IOException {
		long startTimeCreatingGraph = System.currentTimeMillis();
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		graph = new Graph(filename, false);
		long endTimeCreatingGraph = System.currentTimeMillis();
		
		// TODO TIEMPO CREACIÓN GRAFO
		long durationCreatingGraph = (endTimeCreatingGraph - startTimeCreatingGraph);
	}
	
	@SuppressWarnings("unchecked")
	public static void buildGroups () throws ParseException {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("C:/Users/Cristóbal/Documents/RDF-Path-server/python/json_dif_group.json"))
        {
            //Read JSON file
        	JSONObject obj = (JSONObject) jsonParser.parse(reader);
 
            //JSONObject employeeList = (JSONArray) obj;
        	System.out.println(obj.keySet());
        	obj.keySet().forEach(keyStr ->
            {
            	final JSONArray keyvalue = (JSONArray) obj.get(keyStr);
                System.out.println("key: "+ keyStr + " value: " + (JSONArray) keyvalue);
                for (Object vals : keyvalue) {
                	JSONArray t = (JSONArray) vals;
                	int [] array = new int[t.size()];
                	int counter = 0;
                	while (counter < t.size()) {
                		array[counter] = Integer.parseInt((String) t.get(counter));
                	}
                	System.out.println(t.get(0));
                }
                
            });
             
            //Iterate over employee array
            //employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
            //employeeList.forEach( emp -> System.out.println( (JSONObject) emp ) );
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main (String[] args) throws ParseException {
		buildGroups();
	}
}
