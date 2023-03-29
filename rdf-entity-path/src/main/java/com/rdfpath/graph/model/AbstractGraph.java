package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rdfpath.graph.utils.Utils;

public abstract class AbstractGraph implements IGraph {
	
	long startTime = System.currentTimeMillis();
	long actualTime = System.currentTimeMillis();
	long timeA = System.currentTimeMillis();
	int minute = 0;
	String structName = "graph";

	public void printMemory () {
		double maxHeapSize = Runtime.getRuntime().maxMemory();
		double kbSize = maxHeapSize / 1024;
		double mbSize = kbSize / 1024;
		double gbSize = mbSize / 1024;
		
		System.out.println("HeapSize:" + maxHeapSize);
		System.out.println("HeapSize kB:" + kbSize);
		System.out.println("HeapSize mB:" + mbSize);
		System.out.println("HeapSize gB:" + gbSize);
	}
	
	public BufferedReader readFile (String filename, Boolean isGz) throws IOException {
		if (isGz) {
			FileInputStream stream = new FileInputStream(filename);
			GZIPInputStream gzip = new GZIPInputStream(stream);
			return new BufferedReader(new InputStreamReader(gzip));
		}
		FileInputStream stream = new FileInputStream(filename);
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	public void writeSearchAdj (ArrayList<Integer> ids, PrintWriter pw, String dataSet) {
		for (int j = 0; j < ids.size(); j++) {
			int vertexId = ids.get(j);
			long st = System.currentTimeMillis();
			this.getAdjacentVertex(vertexId);
			long end = System.currentTimeMillis();
			long dif = end - st;
			pw.println(this.structName+";"+dif+";"+vertexId+";"+dataSet+";");
		}
	}
	
	public void sendNotificationTime (int perMinutes, String data) {
		if ((((timeA - actualTime)/1000) / 60) >= perMinutes) {
			actualTime = timeA;
			minute += perMinutes;
			sendNotification (data);
		}
	}

	public void sendNotification (String data) {
		System.out.println("Minutos: " + minute);
		System.out.println(data);
		if (System.getProperty("tg-token") != null && System.getProperty("tg-user") != null) {
			try {
				Utils.peticionHttpGet("https://api.telegram.org/bot"+System.getProperty("tg-token") + "/sendMessage?chat_id=\"+System.getProperty(\"tg-user\")+\"&text=Minutos:"+ minute + " " + data);
			
			} catch (Exception e) {
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void diffGroups () throws ParseException {
		JSONParser jsonParser = new JSONParser();
		String fileInput = "src/main/resources/test/json_dif_group.json";
		String fileOutput = "src/main/resources/results/json_dif_group.json";
		
		if (System.getProperty("graph-path") != null) {
			fileInput = "export/test_data/json_dif_group.json";
			fileOutput = "export/results/json_dif_group.json";
		}
		
        try (FileReader reader = new FileReader(fileInput))
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

                	while (counter < t.size()) {
                		int val = (int) Integer.parseInt(t.get(counter).toString());
                		array[counter] = val;
                		counter += 1;
                	}
                	
                	// tiempos resultantes
                	GraphWrapper2 graphWrapper = new GraphWrapper2(this); 
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
        
    	// Escribe respuestas
    	FileWriter file = new FileWriter(fileOutput);
        file.write(answer.toJSONString());
        file.flush();
        file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

	@SuppressWarnings("unchecked")
	public void sameGroups () throws ParseException {
		JSONParser jsonParser = new JSONParser();
		String fileInput = "src/main/resources/test/json_same_group.json";
		String fileOutput = "src/main/resources/results/json_same_group.json";
		
		if (System.getProperty("graph-path") != null) {
			fileInput = "export/test_data/json_same_group.json";
			fileOutput = "export/results/json_same_group.json";
		}
		
        try (FileReader reader = new FileReader(fileInput))
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
            			GraphWrapper2 graphWrapper = new GraphWrapper2(this); 
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
        	
        	FileWriter file = new FileWriter(fileOutput);
            file.write(answer.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


}