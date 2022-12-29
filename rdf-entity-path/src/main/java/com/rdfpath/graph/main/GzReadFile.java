package com.rdfpath.graph.main;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import com.rdfpath.graph.utils.StatementCounter;

public class GzReadFile {
	
	public static void main( String[] args ) throws IOException, IOException {
		System.out.println("ok");
		String path = "C:/Users/Cristóbal/Documents/RDF-Path-server/rdf-entity-path/src/main/resources";
		String filename = path + "/nt/star.nt";
		String filename2 = path + "/borrar/star.nt.gz";
		String filename3 = path + "/borrar/test.nt";

		FileInputStream stream = new FileInputStream(filename2);
		GZIPInputStream gzip = new GZIPInputStream(stream);
		
		
		//String content = br.readLine();
		//System.out.println(content);
		//System.out.println("ok22");
		
		
		RDFParser parser = Rio.createParser(RDFFormat.NTRIPLES);
		StatementCounter myCounter = new StatementCounter();
		parser.setRDFHandler(myCounter);
		try {
			parser.parse(new InputStreamReader(gzip), "");
			//parser.parse(new InputStreamReader(stream), "");
		}
		catch (Exception e) {
			throw new IOException(e);
		}
		System.out.println(myCounter.getNodes());
		
		
		return; 
	}
	
	
}
