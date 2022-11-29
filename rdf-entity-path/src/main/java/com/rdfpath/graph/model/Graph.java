package com.rdfpath.graph.model;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import com.rdfpath.graph.main.ParseExample;
import com.rdfpath.graph.utils.StatementCounter;

public class Graph {

    private HashMap<Integer, Vertex> nodes;

	public Graph (String filename) throws IOException {
		RDFParser parser = Rio.createParser(Rio.getParserFormatForFileName(filename).orElse(RDFFormat.NTRIPLES));
		StatementCounter myCounter = new StatementCounter();
		parser.setRDFHandler(myCounter);
		
		//RDFWriter writer = Rio.createWriter(RDFFormat.RDFJSON, System.out);
		//parser.setRDFHandler(writer);
		try {
			parser.parse(ParseExample.class.getResourceAsStream(filename), "");
		}
		catch (Exception e) {
			throw new IOException(e);
		}
		//int numberOfStatements = myCounter.getCountedStatements();
		//System.out.println(myCounter.getNodes());
		//System.out.println(numberOfStatements);
		this.nodes = myCounter.getNodes();
		return; 
    }
	
	public Graph() {
    	this.nodes = new HashMap<Integer, Vertex>();
    }
	
    public Graph(HashMap<Integer, Vertex> nodes) {
    	this.nodes = new HashMap<Integer, Vertex>(nodes);
    }

	public void addNode(int key, Vertex node) {
        nodes.put(key, node);
    }
	
	public HashMap<Integer, Vertex> getNodes() {
        return nodes;
    }
    
	public void setNodes(HashMap<Integer, Vertex> nodes) {
        this.nodes = nodes;
    }
	
	public void printGrafo () {
		System.out.println("Grafo");
	}
}