package de.q2web.gis.core.api;

import java.util.Arrays;

/**
 * The Class Point.
 */
public class Point {

	private static final int NO_TIME = -1;

	/** The time. */
	private final int time;

	/** The point. */
	private final double[] point;

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Instantiates a new point.
	 * 
	 * @param time
	 *            the time
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Point(final int time, final double x, final double y) {
		this(time, new double[] { x, y });
	}

	/**
	 * Instantiates a new point with <code>-1</code> as time
	 * 
	 * @param point
	 *            the point
	 */
	public Point(final double x, final double y) {
		this(NO_TIME, new double[] { x, y });
	}

	/**
	 * Instantiates a new point with <code>-1</code> as time
	 * 
	 * @param point
	 *            the point
	 */
	public Point(final double[] point) {
		this(NO_TIME, point);
	}

	/**
	 * Instantiates a new point.
	 * 
	 * @param time
	 *            the time
	 * @param point
	 *            the point
	 */
	public Point(final int time, final double[] point) {
		this.time = time;
		this.point = point;
	}

	/**
	 * Gets the number of dimensions.
	 * 
	 * @return the number of dimensions
	 */
	public int getDimensions() {
		return point.length;
	}

	/**
	 * Gets the coordinate for the given dimension (starting with dimension
	 * <code>0</code>)
	 * 
	 * @param dimension
	 *            the dimension
	 * @return the double
	 */
	public double get(final int dimension) {
		return point[dimension];
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * (non-Javadoc)
	 * 
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
		final Point other = (Point) obj;
		if (!Arrays.equals(point, other.point)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (time == -1) {
			return "Point " + Arrays.toString(point);
		}

		return "Point [time=" + time + ", coordinates="
				+ Arrays.toString(point) + "]";
	}

}
