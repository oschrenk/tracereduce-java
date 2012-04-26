package de.q2web.gis.alg.la.ref.dijkstra;

import de.q2web.gis.trajectory.core.api.Point;

/**
 * from: package de.vogella.algorithms.dijkstra.model;
 * 
 * @param <P>
 */
public class Vertex<P extends Number> {
	final private String id;
	final private Point<P> coordinates;

	public Vertex(final String id, final Point<P> coordinates) {
		this.id = id;
		this.coordinates = coordinates;
	}

	public String getId() {
		return id;
	}

	public Point<P> getCoordinates() {
		return coordinates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		final Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return id;
	}

}
