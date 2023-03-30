/**
 * 
 */
package com.rdfpath.graph.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

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
	 * @return				Lista de IDs de nodos adyacentes.
	 */
	public List<Integer> getAdjacentVertex (int id);

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
	 * Convierte una abstracción de arista a una sequencia Json que usa el cliente para dibujar
	 * la arista, también usa una lista que incluye nodos que también debe dibujar.
	 * 
	 * @param e				Arista
	 * @param vList			Lista de ID de nodos
	 * @return				Json que lee VisJS en el cliente
	 */
	public CharSequence edgeToJson(Object e, ArrayList<Integer> vList);

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

}
