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
	HashMap<Integer, Vertex> nodes = null;
  
	public StatementCounter () {
		nodes = new HashMap<Integer, Vertex>();
	}
	
	public void handleStatement(Statement st) {
		// Obtiene IRI
		Value object = st.getObject();
		IRI subject = (IRI) st.getSubject();
		IRI predicate = st.getPredicate();
		
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
	 
	 public HashMap<Integer, Vertex> getNodes () {
		 return nodes;
	 }
}
