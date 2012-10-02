package de.q2web.gis.alg.lo.ref.dijkstra;

/**
 * from: package de.vogella.algorithms.dijkstra.model;
 * 
 * @param <T>
 */
public class Vertex<T> {
	final private int id;
	final private T value;

	public Vertex(final int id, final T value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public T getValue() {
		return value;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + this.id;
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
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
		final Vertex other = (Vertex) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vertex [id=" + this.id + ", value=" + value + "]";
	}

}
