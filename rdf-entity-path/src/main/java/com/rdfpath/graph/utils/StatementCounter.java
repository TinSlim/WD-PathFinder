package com.rdfpath.graph.utils;

import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;

public class StatementCounter extends AbstractRDFHandler {
	
	private Boolean debug = false;
	private int countedStatements = 0;
	private int countedLines = 0;
	HashMap<Integer, Vertex> nodes = null;
	private long startTime;
	private long actualTime;
	private int minute;
	
	public StatementCounter () {
		nodes = new HashMap<Integer, Vertex>();
		startTime = System.currentTimeMillis();
		actualTime = System.currentTimeMillis();
		minute = 0;
	}
	
	public void handleStatement(Statement st) {
		countedLines += 1;
		long timeA = System.currentTimeMillis();
		if ((((timeA - actualTime)/1000) / 60) >= 10) { // Minutos
			actualTime = timeA;
			minute += 10;
			System.out.println("Minutos: " + minute);
			System.out.println("Fila: " + countedLines);
			if (System.getProperty("tg-token") != null) {
				try {
					Utils.peticionHttpGet("https://api.telegram.org/bot"+System.getProperty("tg-token") + "/sendMessage?chat_id=542731494&text=Minutos:"+minute+"_Fila:"+countedLines);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
		
		// Obtiene IRI
		Value object;
		IRI subject;
		IRI predicate;
		try {
			object = st.getObject();
			subject = (IRI) st.getSubject();
			predicate = st.getPredicate();
		}
		catch (Exception e) {
			//throw new IOException(e);
			System.out.println("EXCEPTION IN:");
			System.out.println(countedLines);
			return;
		}
		
		// Transforma a String
		String strSubject = subject.toString();
		String strPredicate = predicate.toString();
		String strObject = object.toString();
		
		// Obtiene IDs de Sujeto, Objeto, Predicado
		int subjectKey;
		int ObjectKey;
		Integer PredicateKey;
		try {
			subjectKey = getObjectId(strSubject);
			ObjectKey = getObjectId(strObject);
			PredicateKey = getPredicateId(strPredicate);
		}
		catch (Exception e) {
			return;
		}
		
		countedStatements++;
		Vertex subjectNode;
		Vertex objectNode;	
		
		// Obtiene nodos si existen, sino los crea
		if(nodes.containsKey(subjectKey)){
			subjectNode = nodes.get(subjectKey);
		} else {
			subjectNode = new Vertex(subjectKey);
			nodes.put(subjectKey, subjectNode);
		}

		if(nodes.containsKey(ObjectKey)){
			objectNode = nodes.get(ObjectKey);
		} else {
			objectNode = new Vertex(ObjectKey);
			nodes.put(ObjectKey, objectNode);
		}
		
		//double weight = 1.0 + (double) edgesCount.get(getPredicateId(strPredicate))/maxEdgeCount;
		
		// Añade arista
		Edge edge = new Edge(PredicateKey, subjectNode, objectNode, 0);
		subjectNode.addEdge(edge);
		objectNode.addEdge(edge);
		
		if (debug) System.out.println(subjectKey + "->" + getPredicateId(strPredicate) + "->" + ObjectKey);
	}


	private Integer getObjectId(String obj) {
			return Integer.parseInt(obj.substring(32, obj.length()));	
		}
	
	private Integer getPredicateId(String pred) {
		if(pred.equals("http://www.w3.org/2002/07/owl#sameAs"))
			return -1;
		else return Integer.parseInt(pred.substring(37, pred.length()));	
	}

	 public int getCountedStatements() {
	   return countedStatements;
	 }
	 
	 public int getCountedLines () {
		 return countedLines;
	 }
	 
	 public HashMap<Integer, Vertex> getNodes () {
		 return nodes;
	 }
}
