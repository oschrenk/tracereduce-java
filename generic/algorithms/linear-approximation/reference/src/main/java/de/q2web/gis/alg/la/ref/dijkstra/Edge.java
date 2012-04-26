package de.q2web.gis.alg.la.ref.dijkstra;

/**
 * package de.vogella.algorithms.dijkstra.model;
 * 
 * @param <P>
 */
public class Edge<P extends Number> {
	private final String id;
	private final Vertex<P> source;
	private final Vertex<P> destination;
	private final int weight;

	public Edge(final String id, final Vertex<P> source,
			final Vertex<P> destination, final int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public Vertex<P> getDestination() {
		return destination;
	}

	public Vertex<P> getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

}
