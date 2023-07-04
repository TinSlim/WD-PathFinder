package com.rdfpath.graph.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

import org.springframework.web.socket.WebSocketSession;

/**
*
* @author Cristóbal Torres G.
* @github Tinslim
*
*/
public abstract class AbstractGraph implements IGraph {
	
	long startTime = System.currentTimeMillis();
	long actualTime = System.currentTimeMillis();
	long timeA = System.currentTimeMillis();
	int minute = 0;
	protected String structName = "graph";

	protected int edgesLoaded = 0;
	protected int countedStatements = 0;
	protected int nodesLoaded = 0;
    
	/**
	 * Imprime tamaño del Heap
	 */
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
	
	/**
	 * Compara el tiempo actual con el tiempo de inicio, si es mayor a una cantidad de segundos lanza una interrupción.
	 * @param seconds					segundos
	 * @param startTime					tiempo de inicio
	 * @throws InterruptedException		Interrupción de termino del tiempo
	 */
	public void checkTime (int seconds, long startTime) throws InterruptedException {
		if ( (System.currentTimeMillis() - startTime) > seconds * 1000 ) {
			if (System.getProperty("debug") != null) {
				System.out.println("GetAdjVertex");
			}
			System.out.println("done time");
			System.out.println("done time");
			throw new InterruptedException("Done Time");
			
		}
	}
	
	
	/**
	 * Revisa si la conexión sigue abierta, si no lo está lanza una interrupción.
	 * @param session					session actual
	 * @throws IOException				Interrupción cuando termina la conexión
	 */
	public void checkConn(WebSocketSession session) throws IOException {
		if (!session.isOpen() ) {
			throw new IOException();
		}
	}

	@Override
	public String getStructName() {
		return structName;
	}
	
	/**
	 * A partir de un archivo (que puede ser comprimido o no) genera un lector.
	 * @param filename					nombre del archivo
	 * @param isGz						es comprimido gz
	 * @return							lector del archivo					
	 * @throws IOException				Interrupción si no existe el archivo
	 */
	public BufferedReader readFile (String filename, Boolean isGz) throws IOException {
		if (isGz) {
			FileInputStream stream = new FileInputStream(filename);
			GZIPInputStream gzip = new GZIPInputStream(stream);
			return new BufferedReader(new InputStreamReader(gzip));
		}
		FileInputStream stream = new FileInputStream(filename);
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	@Override
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
	
	@Override
	public ArrayList<HashSet<Integer>> checkAdj (int[] ids) {
		ArrayList<HashSet<Integer>> ans = new ArrayList();
		for (int j = 0; j < ids.length; j++) {
			int vertexId = ids[j];
		
			HashSet<Integer> adj = this.getAdjacentVertex(vertexId);
			ans.add(adj);
		}
		return ans;
	}

	@Override
	public HashSet<Integer> getAdjacentVertexSessionLimited (int id, WebSocketSession session, int maxEdgeSize, boolean isInitial) throws IOException {
		HashSet<Integer> adjVertex = getAdjacentVertexSession(id, session);
		if ( maxEdgeSize == -1 || adjVertex.size() < maxEdgeSize || isInitial ) {
			return adjVertex;
		}
		return new HashSet<Integer>();
	}
	
	public HashSet<Integer> getAdjacentVertexSessionTimeoutLimited(int id, WebSocketSession session, int maxEdgeSize, boolean isInitial, long initTime, int limitTime) throws InterruptedException, IOException {
		HashSet<Integer> adjVertex = getAdjacentVertexSessionTimeout(id, session, initTime, limitTime);
		if ( maxEdgeSize == -1 || adjVertex.size() < maxEdgeSize || isInitial ) {
			return adjVertex;
		}
		return new HashSet<Integer>();
	}

	@Override
	public HashSet<Integer> getAdjacentVertexTimeoutLimited(int id, int seconds, long startTime, int maxEdgeSize, boolean isInitial) throws InterruptedException {
		HashSet<Integer> adjVertex = getAdjacentVertexTimeout(id, seconds, startTime);
		if ( maxEdgeSize == -1 || adjVertex.size() < maxEdgeSize || isInitial ) {
			return adjVertex;
		}
		return new HashSet<Integer>();
	}

	@Override
	public String edgeToText(Object edge) {
		return "{"+getOriginEdge(edge) + "->" + getPredicateEdge(edge) + "->" + getDestinationEdge(edge) + "}";
	}
	
	
}