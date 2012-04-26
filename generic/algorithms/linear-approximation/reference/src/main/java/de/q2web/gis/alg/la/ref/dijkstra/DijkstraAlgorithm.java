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

public class DijkstraAlgorithm<P extends Number> {

	private final List<Edge<P>> edges;
	private Set<Vertex<P>> settledNodes;
	private Set<Vertex<P>> unSettledNodes;
	private Map<Vertex<P>, Vertex<P>> predecessors;
	private Map<Vertex<P>, Integer> distance;

	public DijkstraAlgorithm(final Graph<P> graph) {
		// Create a copy of the array so that we can operate on this array
		edges = new ArrayList<Edge<P>>(graph.getEdges());
	}

	public void execute(final Vertex<P> source) {
		settledNodes = new HashSet<Vertex<P>>();
		unSettledNodes = new HashSet<Vertex<P>>();
		distance = new HashMap<Vertex<P>, Integer>();
		predecessors = new HashMap<Vertex<P>, Vertex<P>>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			final Vertex<P> node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(final Vertex<P> node) {
		final List<Vertex<P>> adjacentNodes = getNeighbors(node);
		for (final Vertex<P> target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(final Vertex<P> node, final Vertex<P> target) {
		for (final Edge<P> edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex<P>> getNeighbors(final Vertex<P> node) {
		final List<Vertex<P>> neighbors = new ArrayList<Vertex<P>>();
		for (final Edge<P> edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex<P> getMinimum(final Set<Vertex<P>> vertexes) {
		Vertex<P> minimum = null;
		for (final Vertex<P> vertex : vertexes) {
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

	private boolean isSettled(final Vertex<P> vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(final Vertex<P> destination) {
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
	public LinkedList<Vertex<P>> getPath(final Vertex<P> target) {
		final LinkedList<Vertex<P>> path = new LinkedList<Vertex<P>>();
		Vertex<P> step = target;
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
