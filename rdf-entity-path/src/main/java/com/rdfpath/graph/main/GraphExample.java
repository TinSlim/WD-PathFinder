package com.rdfpath.graph.main;

import java.io.IOException;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import com.rdfpath.graph.model.Edge;
import com.rdfpath.graph.model.Graph;
import com.rdfpath.graph.model.Vertex;
import com.rdfpath.graph.utils.StatementCounter;

public class GraphExample {
    public Graph grafo = new Graph();
    public Vertex verticeOld = new Vertex(0);
    

    public void makeGrafo () {
        grafo.addNode(0,verticeOld);
        for (int i = 1; i < 5; i++) {
            Vertex vertice = new Vertex(i);
            vertice.addEdge(new Edge(i, vertice, verticeOld));
            grafo.addNode(i,vertice);
            System.out.println(i);
          }
    }
    
    public void makeGraph () throws RDFParseException, RDFHandlerException, IOException {
    	String data = "/test.nt";

		RDFParser parser = Rio.createParser(Rio.getParserFormatForFileName(data).orElse(RDFFormat.NTRIPLES));
		StatementCounter myCounter = new StatementCounter();
		parser.setRDFHandler(myCounter);
		
		//RDFWriter writer = Rio.createWriter(RDFFormat.RDFJSON, System.out);
		//parser.setRDFHandler(writer);
		
		try {
			parser.parse(ParseExample.class.getResourceAsStream(data), "");
		}
		catch (Exception e) {
		  // oh no!
		}
		int numberOfStatements = myCounter.getCountedStatements();
		System.out.println(myCounter.getNodes());
		System.out.println(numberOfStatements);
		grafo = new Graph(myCounter.getNodes());
		grafo.printGrafo();
		
    }
    
    
    public void printGrafo () {
        System.out.println(grafo.getNodes());
    }
}