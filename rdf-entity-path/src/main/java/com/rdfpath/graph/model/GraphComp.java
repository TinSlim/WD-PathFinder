/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

import com.rdfpath.graph.utils.StatementCounter;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public class GraphComp implements IGraph {

	public int[][][] nodes;
	
	public GraphComp (String filename, Boolean isGz ) throws IOException {
		String filename2 = "C:/Users/Cristóbal/Documents/RDF-Path-server/python/prearchivo/compressed_struct.gz";
		FileInputStream stream = new FileInputStream(filename2);
		GZIPInputStream gzip = new GZIPInputStream(stream);
		BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
		
		String line = "";
        String[] tempArr;
        while((line = br.readLine()) != null) {
           tempArr = line.split(" ");
           String id = tempArr[0];
           int edgeSize = Integer.parseInt(tempArr[1]);
           int i = 2;
           int[] edgeArray = new int[edgeSize * 2];
           for(String tempStr : tempArr) {
               System.out.print(tempStr + "\n");
            }
           //while (i < edgeSize) {
           //
           //}
           //System.out.println();
        }
        br.close();
		//RDFParser parser = Rio.createParser(RDFFormat.NTRIPLES);
		//StatementCounter myCounter = new StatementCounter();
		//
		//GraphCounterNative myCounter = new GraphCounterNative();
		//parser.setRDFHandler(myCounter);
		
        //try {
        //	parser.parse(br, "");
        //}
        //catch (Exception e) {
			//throw new IOException(e);
        //System.out.println("ERROR:STACKTRACE::"); // TODO
        //e.printStackTrace();
        //System.out.println("ERROR:_______::"); // TODO
        //System.out.println(e);
        //}
        //this.nodes = myCounter.getNodes();
        //myCounter.printCounters();
        System.out.println("comp");
		return;
	}
	
	
	@Override
	public List<Integer> getAdjacentVertex(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList getEdges(int idVertex, int idVertex2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOriginEdge(Object e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDestinationEdge(Object e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence edgeToJson(Object e, ArrayList<Integer> vList) {
		// TODO Auto-generated method stub
		return null;
	}

}
