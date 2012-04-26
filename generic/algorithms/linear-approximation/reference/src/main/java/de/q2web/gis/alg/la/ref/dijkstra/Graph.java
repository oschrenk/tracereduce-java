package de.q2web.gis.alg.la.ref.dijkstra;

import java.util.List;

/**
 * The Class Graph.
 * 
 * @param <P>
 *            the generic type
 */
public class Graph<P extends Number> {

	/** The vertexes. */
	private final List<Vertex<P>> vertexes;

	/** The edges. */
	private final List<Edge<P>> edges;

	/**
	 * Instantiates a new graph.
	 * 
	 * @param vertexes
	 *            the vertexes
	 * @param edges
	 *            the edges
	 */
	public Graph(final List<Vertex<P>> vertexes, final List<Edge<P>> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	/**
	 * Gets the vertexes.
	 * 
	 * @return the vertexes
	 */
	public List<Vertex<P>> getVertexes() {
		return vertexes;
	}

	/**
	 * Gets the edges.
	 * 
	 * @return the edges
	 */
	public List<Edge<P>> getEdges() {
		return edges;
	}

}
