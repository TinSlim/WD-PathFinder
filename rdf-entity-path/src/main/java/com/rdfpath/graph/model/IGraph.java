/**
 * 
 */
package com.rdfpath.graph.model;

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
	
	public List<Integer> getAdjacentVertex (int id);

	/**
	 * @param idVertex
	 * @param idVertex2
	 * @return
	 */
	public ArrayList getEdges(int idVertex, int idVertex2);

	/**
	 * @param e
	 * @return
	 */
	public int getOriginEdge(Object e);

	/**
	 * @param e
	 * @return
	 */
	public int getDestinationEdge(Object e);

	/**
	 * @param e
	 * @param vList
	 * @return
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
}
