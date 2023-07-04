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
	 * Imprime en un escritor los tiempos de búsqueda de vecinos de los nodos.
	 * @param ids						nodos de los que se van a buscar sus vecinos
	 * @param pw						escritor
	 * @param dataSet					nombre del set de datos usado
	 */
	public void writeSearchAdj(int[] ids, PrintWriter pw, String dataSet);

	/**
	 * Devuelve el nombre de la estructura
	 * @return							Nombre de la estructura
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


	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la cantidad de segundos de la búsqueda se supera lanza una interrupción.
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param seconds						segundos máximos de búsqueda
	 * @param startTime						tiempo de inicio
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws InterruptedException			Interrupción cuando se supera el tiempo máximo
	 */
	HashSet<Integer> getAdjacentVertexTimeout(int id, int seconds, long startTime) throws InterruptedException;

	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la cantidad de segundos de la búsqueda se supera lanza una interrupción.
	 * Si el nodo actual tiene más vecinos que límite máximo y no es inicial, no se retorna nodos vecinos.
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param seconds						segundos máximos de búsqueda
	 * @param startTime						tiempo de inicio
	 * @param maxEdgeSize					límite de vecinos para el nodo
	 * @param isInitial						si el nodo es inicial o no
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws InterruptedException			Interrupción cuando se supera el tiempo máximo
	 */
	HashSet<Integer> getAdjacentVertexTimeoutLimited(int id, int seconds, long startTime, int maxEdgeSize, boolean isInitial) throws InterruptedException;
	
	/**
	 * Devuelve un HashSet con llave el id del nodo y valor sus nodos vecinos.
	 * @param ids							id de los nodos que se buscarán vecinos
	 * @return								HashSet con llave el id del nodo y valor sus nodos vecinos
	 */
	public ArrayList checkAdj(int[] ids);

	
	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la sessión no sigue abierta lanza una interrupción.
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param session						conexión actual
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws IOException					Interrupción cuando no sigue la conexión
	 */
	HashSet<Integer> getAdjacentVertexSession(int id, WebSocketSession session) throws IOException;
	
	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la sessión no sigue abierta lanza una interrupción. 
	 * Si el nodo actual tiene más vecinos que límite máximo y no es inicial, no se retorna nodos vecinos.
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param session						conexión actual
	 * @param maxEdgeSize					límite de vecinos para el nodo
	 * @param isInitial						si el nodo es inicial o no
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws IOException					Interrupción cuando no sigue la conexión
	 */
	HashSet<Integer> getAdjacentVertexSessionLimited(int id, WebSocketSession session, int maxEdgeSize, boolean isInitial) throws IOException;
	
	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la sessión no sigue abierta o se agoto el tiempo lanza una interrupción. 
	 * Si el nodo actual tiene más vecinos que límite máximo y no es inicial, no se retorna nodos vecinos.
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param session						conexión actual
	 * @param maxEdgeSize					límite de vecinos para el nodo
	 * @param isInitial						si el nodo es inicial o no
	 * @param initTime						tiempo en que inicia la búsqueda
	 * @param limitTime						tiempo límite
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws InterruptedException			Interrupción cuando se alcanza el tiempo límite
	 * @throws IOException					Interrupción cuando no sigue la conexión
	 */
	HashSet<Integer> getAdjacentVertexSessionTimeoutLimited(int id, WebSocketSession session, int maxEdgeSize, boolean isInitial, long initTime, int limitTime) throws InterruptedException, IOException;

	/**
	 * Obtiene los nodos adyacentes de un nodo. Si la sessión no sigue abierta o se agoto el tiempo lanza una interrupción. 
	 * @param id							id del nodo que se obtendrán sus vecinos
	 * @param session						conexión actual
	 * @param initTime						tiempo en que inicia la búsqueda
	 * @param limitTime						tiempo límite
	 * @return								HashSet con los ids de nodos vecinos
	 * @throws InterruptedException			Interrupción cuando se alcanza el tiempo límite
	 * @throws IOException					Interrupción cuando no sigue la conexión
	 */
	HashSet<Integer> getAdjacentVertexSessionTimeout(int id, WebSocketSession session, long initTime, int limitTime) throws InterruptedException, IOException;
	/**
	 * Obtiene el grado del nodo, es decir cantidad de nodos vecinos.
	 * @param idVertex						nodo que se le obtendrá el grado
	 * @return								grado del nodo
	 */
	public int getGrade (int idVertex);

}
