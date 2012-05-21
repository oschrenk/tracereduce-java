package de.q2web.gis.trajectory.core.api;

import java.util.Arrays;

public class DoublePoint implements Point {

	private final double[] point;

	public DoublePoint(final double[] point) {
		this.point = point;
	}

	@Override
	public int getDimensions() {
		return point.length;
	}

	@Override
	public double get(final int dimension) {
		return point[dimension];
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(point);
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		final DoublePoint other = (DoublePoint) obj;
		if (!Arrays.equals(point, other.point)) {
			return false;
		}
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DoublePoint [point=" + Arrays.toString(point) + "]";
	}

}
