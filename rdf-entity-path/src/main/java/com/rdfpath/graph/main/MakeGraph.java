package com.rdfpath.graph.main;

import java.io.IOException;

import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;

public class MakeGraph {
    public GraphExample example; 
    public static void main( String[] args ) throws RDFParseException, RDFHandlerException, IOException
    {
        GraphExample example = new GraphExample();
        example.makeGrafo();
        example.printGrafo();
        example.makeGraph();
        System.out.println( "Hello World!" );
    }
}