package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.WebSocketSession;

import com.rdfpath.graph.utils.Utils;

public abstract class AbstractGraph implements IGraph {
	
	long startTime = System.currentTimeMillis();
	long actualTime = System.currentTimeMillis();
	long timeA = System.currentTimeMillis();
	int minute = 0;
	protected String structName = "graph";

	protected int edgesLoaded = 0;
	protected int countedStatements = 0;
	protected int nodesLoaded = 0;
    
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
	
	public void checkTime (int seconds, long startTime) throws InterruptedException {
		if ( (System.currentTimeMillis() - startTime) > seconds * 1000 ) {
			if (System.getProperty("debug") != null) {
				System.out.println("GetAdjVertex");
			}
			throw new InterruptedException("Done Time");
		}
		/*
		 if (Thread.interrupted()) {
		    throw new InterruptedException();
		}
		 */
	}
	
	public void checkConn(WebSocketSession session) throws IOException {
		if (!session.isOpen() ) {
			throw new IOException();
		}
	}

	public String getStructName() {
		return structName;
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
	
	public void writeSearchAdj (int[] ids, PrintWriter pw, String dataSet) {
		for (int j = 0; j < ids.length; j++) {
			System.out.print(j+"/"+ids.length+"\r");
			int vertexId = ids[j];
			long st = System.nanoTime();
			HashSet<Integer> adj = this.getAdjacentVertex(vertexId);
			long end = System.nanoTime();
			long dif = end - st;
			pw.println(this.structName+";"+dif+";"+vertexId+";"+dataSet);
		}
	}
	
	public ArrayList<HashSet<Integer>> checkAdj (int[] ids) {
		ArrayList<HashSet<Integer>> ans = new ArrayList();
		for (int j = 0; j < ids.length; j++) {
			int vertexId = ids[j];
		
			HashSet<Integer> adj = this.getAdjacentVertex(vertexId);
			ans.add(adj);
		}
		return ans;
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
                	GraphWrapper graphWrapper = new GraphWrapper(this); 
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
            			GraphWrapper graphWrapper = new GraphWrapper(this); 
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
	
	@Override
	public String edgeToText(Object edge) {
		return "{"+getOriginEdge(edge) + "->" + getPredicateEdge(edge) + "->" + getDestinationEdge(edge) + "}";
	}
	
	@Override
	public CharSequence nodeToJson(int idSearch) {
		String color = "#97C2FC";
		
		// Node
		String vertexLabel = Utils.getEntityName("Q" + idSearch);
    	String vertexLabelSmall = vertexLabel;
    	if (vertexLabel.length() > 7) {vertexLabelSmall = vertexLabel.substring(0,Math.min(vertexLabel.length(), 7)) + "...";}
    	
    	JSONObject newVertex = new JSONObject();
    	newVertex.put("label", vertexLabelSmall);
    	newVertex.put("color",color);
    	newVertex.put("title", vertexLabel);
    	
    	// Json
    	JSONObject json = new JSONObject();
    	json.put("type","vertex");
    	json.put("data",newVertex);
    	//newVertex.put("x",200);
    	//newVertex.put("fixed",true);
    	newVertex.put("id", idSearch);
    	return json.toString();
	}

	
	public CharSequence edgeToJson(Object e) {
    	JSONObject json = new JSONObject();
    	
    	// Edge data
    	String edgeLabel = Utils.getEntityName("P"+	getPredicateEdge(e) +"&type=property");
    	String edgeLabelSmall = edgeLabel;
    	if (edgeLabel.length() > 7) {edgeLabelSmall = edgeLabel.substring(0,Math.min(edgeLabel.length(), 7)) + "...";}

    	// Arrow Config
    	JSONObject arrowInfo = new JSONObject();
    	arrowInfo.put("enabled", true);
    	arrowInfo.put("type", "arrow");
    	
    	JSONObject arrow = new JSONObject();
    	arrow.put("to", arrowInfo);
    	
    	// Edge
    	JSONObject edge = new JSONObject();
    	edge.put("from", getOriginEdge(e));
    	edge.put("to", getDestinationEdge(e));
    	edge.put("label", edgeLabelSmall);//Utils.getEntityName("P" + id));
    	edge.put("title", edgeLabel);
    	
    	JSONObject font = new JSONObject();
    	font.put("align", "middle");
    	font.put("strokeColor", "#dbdbdb"); // TODO color
    	font.put("size", 18);
    	edge.put("font", font);
    	
    	edge.put("color", new JSONObject().put("color", "#848484")); // TODO color
    	edge.put("arrows", arrow);
    	
    	// TODO Fuerza aristas
    	edge.put("length", 500);
    	
    	json.put("type", "edge");
    	json.put("data", edge);
    	
    	return json.toString();
    }
	
	
	
}