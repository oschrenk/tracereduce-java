package de.q2web.gis.alg.la.ref.dijkstra;

/**
 * package de.vogella.algorithms.dijkstra.engine;
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm<T> {

	private final List<Edge<T>> edges;
	private Set<Vertex<T>> settledNodes;
	private Set<Vertex<T>> unSettledNodes;
	private Map<Vertex<T>, Vertex<T>> predecessors;
	private Map<Vertex<T>, Integer> distance;

	public DijkstraAlgorithm(final Graph<T> graph) {
		// Create a copy of the array so that we can operate on this array
		edges = new ArrayList<Edge<T>>(graph.getEdges());
	}

	public void execute(final Vertex<T> source) {
		settledNodes = new HashSet<Vertex<T>>();
		unSettledNodes = new HashSet<Vertex<T>>();
		distance = new HashMap<Vertex<T>, Integer>();
		predecessors = new HashMap<Vertex<T>, Vertex<T>>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			final Vertex<T> node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(final Vertex<T> node) {
		final List<Vertex<T>> adjacentNodes = getNeighbors(node);
		for (final Vertex<T> target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(final Vertex<T> node, final Vertex<T> target) {
		for (final Edge<T> edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex<T>> getNeighbors(final Vertex<T> node) {
		final List<Vertex<T>> neighbors = new ArrayList<Vertex<T>>();
		for (final Edge<T> edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex<T> getMinimum(final Set<Vertex<T>> vertexes) {
		Vertex<T> minimum = null;
		for (final Vertex<T> vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(final Vertex<T> vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(final Vertex<T> destination) {
		final Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Vertex<T>> getPath(final Vertex<T> target) {
		final LinkedList<Vertex<T>> path = new LinkedList<Vertex<T>>();
		Vertex<T> step = target;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

}
