package com.rdfpath.graph.algorithms;

import java.util.ArrayList;

import com.rdfpath.graph.model.Edge;

public interface IAlgorithm {
	ArrayList<Edge> getRoads (int size);
	ArrayList<Edge> getRoadsOnline (int size);
}
