package com.rdfpath.graph.main;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;

public class ParseExample {

	public static void main(String[] args) throws Exception {
		String data = "/test.nt";

		RDFParser parser = Rio.createParser(Rio.getParserFormatForFileName(data).orElse(RDFFormat.NTRIPLES));
		RDFWriter writer = Rio.createWriter(RDFFormat.RDFJSON, System.out);
		parser.setRDFHandler(writer);
		parser.parse(ParseExample.class.getResourceAsStream(data), "");

	}

}
