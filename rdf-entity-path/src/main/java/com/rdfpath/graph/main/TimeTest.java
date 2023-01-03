package com.rdfpath.graph.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.GraphWrapper;

public class TimeTest {
	public static Graph graph;
	public ArrayList groups;
	public int maxSize = 3;
	
	public static void buildGraph () throws IOException {
		long startTimeCreatingGraph = System.currentTimeMillis();
		String filename = "/nt/star.nt";//"/nt/myGraph.nt";
		graph = new Graph(filename, false);
		long endTimeCreatingGraph = System.currentTimeMillis();
		// TODO TIEMPO CREACIÓN GRAFO
		long durationCreatingGraph = (endTimeCreatingGraph - startTimeCreatingGraph);
	}
	
	@SuppressWarnings("unchecked")
	public static void diffGroups () throws ParseException {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("src/main/resources/test/json_dif_group.json"))
        {
            //Read JSON file
        	JSONObject obj = (JSONObject) jsonParser.parse(reader);
        	JSONObject answer = new JSONObject();
        	
        	obj.keySet().forEach(keyStr ->
            {
            	JSONArray keyAnswer = new JSONArray();
            	final JSONArray keyvalue = (JSONArray) obj.get(keyStr);
                for (Object vals : keyvalue) {
                	
                	JSONArray t = (JSONArray) vals;
                	int [] array = new int[t.size()];
                	int counter = 0;
                	//System.out.println(t);
                	while (counter < t.size()) {
                		int val = (int) Integer.parseInt(t.get(counter).toString());
                		array[counter] = val;
                		counter += 1;
                	}
                	 // tiempos resultantes
                	
                	GraphWrapper graphWrapper = new GraphWrapper(graph); 
                	try {
						graphWrapper.search(array, (int) Integer.parseInt(keyStr.toString()));						
						JSONArray mJSONArray = new JSONArray();
						for (long x : graphWrapper.getTimes()) {
							mJSONArray.add(x);
						}
						keyAnswer.add(mJSONArray);
                	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
                }
                answer.put(keyStr, keyAnswer);
            });
            
    	FileWriter file = new FileWriter("src/main/resources/results/json_dif_group.json");
        file.write(answer.toJSONString());
        file.flush();
        file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public static void sameGroups () throws ParseException {
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("src/main/resources/test/json_same_group.json"))
        {
            //Read JSON file
        	JSONObject obj = (JSONObject) jsonParser.parse(reader);
        	JSONObject answer = new JSONObject();
        	
        	
        	obj.keySet().forEach(keyStr ->  // numSize
            {
            	
            	JSONObject keyAnswer = new JSONObject(); // numSize
            	JSONObject keyvalue = (JSONObject) obj.get(keyStr);
            	keyvalue.keySet().forEach(keyName ->
                {
                	JSONArray nameAnswer = new JSONArray();
                	
                	
                	JSONArray arrayOfNums = (JSONArray) keyvalue.get(keyName);
            		int counter = 0;
            		while (counter < arrayOfNums.size()) {
            			JSONArray arrayNum = (JSONArray) arrayOfNums.get(counter);
            			//System.out.println(arrayNum);
            			int counterSec = 0;
            			int[] array = new int[arrayNum.size()];
            			while (counterSec < arrayNum.size()) {
                            int val = (int) Integer.parseInt(arrayNum.get(counterSec).toString());
                            array[counterSec] = val;
            				counterSec += 1;
                        }
            			
            			// tiempos resultantes
            			GraphWrapper graphWrapper = new GraphWrapper(graph); 
                    	try {
    						graphWrapper.search(array, (int) Integer.parseInt(keyStr.toString()));
    						
    						JSONArray mJSONArray = new JSONArray();
    						for (long x : graphWrapper.getTimes()) {
    							mJSONArray.add(x);
    						}
    						nameAnswer.add(mJSONArray);
                    	} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
            			counter += 1;
                    }

            		keyAnswer.put(keyName, nameAnswer);
            		
                });
            	
            	answer.put(keyStr, keyAnswer);
            });
        	
        	FileWriter file = new FileWriter("src/main/resources/results/json_same_group.json");
            file.write(answer.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main (String[] args) throws ParseException, IOException {
		buildGraph();
		diffGroups();
		sameGroups();
	}
}
