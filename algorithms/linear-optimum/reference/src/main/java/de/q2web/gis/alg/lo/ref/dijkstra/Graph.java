package de.q2web.gis.alg.lo.ref.dijkstra;

import java.util.List;

/**
 * The Class Graph.
 * 
 * @param <T>
 *            the generic type
 */
public class Graph<T> {

	/** The vertexes. */
	private final List<Vertex<T>> vertexes;

	/** The edges. */
	private final List<Edge<T>> edges;

	/**
	 * Instantiates a new graph.
	 * 
	 * @param vertexes
	 *            the vertexes
	 * @param edges
	 *            the edges
	 */
	public Graph(final List<Vertex<T>> vertexes, final List<Edge<T>> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	/**
	 * Gets the vertexes.
	 * 
	 * @return the vertexes
	 */
	public List<Vertex<T>> getVertexes() {
		return vertexes;
	}

	/**
	 * Gets the edges.
	 * 
	 * @return the edges
	 */
	public List<Edge<T>> getEdges() {
		return edges;
	}

}
