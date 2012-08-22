package de.q2web.gis.alg.la.ref.dijkstra;

/**
 * package de.vogella.algorithms.dijkstra.model;
 *
 * @param <T>
 */
public class Edge<T> {

	private static final int DEFAULT_WEIGHT = 1;

	private final int id;
	private final Vertex<T> source;
	private final Vertex<T> destination;
	private final int weight;

	public Edge(final int id, final Vertex<T> source,
			final Vertex<T> destination) {
		this(id, source, destination, DEFAULT_WEIGHT);
	}

	public Edge(final int id, final Vertex<T> source,
			final Vertex<T> destination, final int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public Vertex<T> getDestination() {
		return destination;
	}

	public Vertex<T> getSource() {
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
