package com.barclays.routebaggage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.barclays.routebaggage.model.Edge;
import com.barclays.routebaggage.model.Vertex;
import com.barclays.routebaggage.model.Graph;

public class RouteBaggageSystem {
	
	private final List<Vertex> nodes;
	private final List<Edge> edges;
	private Set<Vertex> setNodes;
	private Set<Vertex> unSetNodes;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distance;

	//Constructor 
	public RouteBaggageSystem(Graph graph) {
		this.nodes = new ArrayList<Vertex>(graph.getVertices());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	//Traverse through Nodes 
	public void route(Vertex source) {
		setNodes = new HashSet<Vertex>();
		unSetNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSetNodes.add(source);
		while (unSetNodes.size() > 0) {
			Vertex node = getMin(unSetNodes);
			setNodes.add(node);
			unSetNodes.remove(node);
			findMinDistances(node);
		}
	}
	
	// Find Minimal distance between source and destination
	private void findMinDistances(Vertex node) {
		List<Vertex> adjacentNodes = getAdjacentNodes(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSetNodes.add(target);
			}
		}

	}
	
	//Find distance between nodes 
	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Runtime Exception...");
	}

	//Find adjacent nodes for each node
	private List<Vertex> getAdjacentNodes(Vertex node) {
		List<Vertex> adjacentNodes = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSet(edge.getDestination())) {
				adjacentNodes.add(edge.getDestination());
			}
			
		}
		return adjacentNodes;
	}

	//Find minimum weight of vertices 
	private Vertex getMin(Set<Vertex> vertices) {
		Vertex min = null;
		for (Vertex vertex : vertices) {
			if (min == null) {
				min = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(min)) {
					min = vertex;
				}
			}
		}
		return min;
	}

	//check Node set or unset
	private boolean isSet(Vertex vertex) {
		return setNodes.contains(vertex);
	}

	//Find shortest distance from source to destination
	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	//find the minimal path for the system
	public Map<Integer,LinkedList<Vertex>> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex count = target;
		int sum=0;
		if (predecessors.get(count) == null) {
			return null;
		}
		path.add(count);
		while (predecessors.get(count) != null) {
			sum+=getDistance(count, predecessors.get(count));
			count = predecessors.get(count);
			path.add(count);
		}
		Collections.reverse(path);
		Map<Integer,LinkedList<Vertex>> map = new HashMap<Integer, LinkedList<Vertex>>();
		map.put(Integer.valueOf(sum), path);
		return map;
	}	

}
