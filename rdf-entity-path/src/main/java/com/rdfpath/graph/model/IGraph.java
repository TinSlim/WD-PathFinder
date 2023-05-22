/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Cristóbal Torres G.
 * @github Tinslim
 *
 */
public interface IGraph {


	/**
	 * Entrega los IDs de los nodos adyacentes de un nodo.
	 *
	 * @param id			ID nodo del que se buscan nodos adyacentes.
	 * @return				Set de IDs de nodos adyacentes.
	 */
	public HashSet<Integer> getAdjacentVertex (int id);

	/**
	 * Obtiene la lista de Aristas entre dos Nodos del Grafo, el formato depende de cada
	 * implementación de Grafo.
	 * 
	 * @param idVertex		Nodo1
	 * @param idVertex2		Nodo2
	 * @return				Arista que unen los nodos
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList getEdges(int idVertex, int idVertex2);

	/**
	 * Obtiene el origen de una Arista, cada implementación de Grafo usa distinta arista,
	 * pero el método retorna el ID del origen.
	 * 
	 * @param e				Arista
	 * @return				ID origen de la arista
	 */
	public int getOriginEdge(Object e);

	/**
	 * Obtiene el destino de una Arista, cada implementación de Grafo usa distinta arista,
	 * pero el método retorna el ID del origen.
	 * 
	 * @param e				Arista
	 * @return				ID destino de la arista
	 */
	public int getDestinationEdge(Object e);

	/**
	 * @throws ParseException 
	 * 
	 */
	public void diffGroups() throws ParseException;
	
	/**
	 * @throws ParseException 
	 * 
	 */
	public void sameGroups() throws ParseException;

	public void writeSearchAdj(int[] ids, PrintWriter pw, String dataSet);

	/**
	 * Obtiene el nombre de la estructura.
	 * 
	 * @return				Nombre de la estructura
	 */
	public String getStructName();

	/**
	 * @return
	 */
	public String edgeToText(Object edge);

	/**
	 * @param e
	 * @return
	 */
	int getPredicateEdge(Object e);



	HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException;

	/**
	 * @param ids
	 * @return
	 */
	public ArrayList checkAdj(int[] ids);

	HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException;

}
