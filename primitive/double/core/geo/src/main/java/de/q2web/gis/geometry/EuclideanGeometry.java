package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class EuclideanGeometry.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanGeometry extends AbstractGeometry {

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public double distance(final Point from, final Point to) {

		final int dimensions = from.getDimensions();
		double sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + square((to.get(i) - from.get(i)));
		}

		return Math.sqrt(sumSquared);
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public double distance(final Point point, final Point lineStart,
			final Point lineEnd) {

		final int dimensions = lineStart.getDimensions();
		if (dimensions == 2) {
			return distance2d(point, lineStart, lineEnd);
		}

		if (dimensions == 3) {
			return distance3d(point, lineStart, lineEnd);
		}

		throw new IllegalArgumentException("Invalid number of dimensions");
	}

	/**
	 * Distance2d.
	 * 
	 * @param point
	 *            the point
	 * @param lineStart
	 *            the line start
	 * @param lineEnd
	 *            the line end
	 * @return the double
	 */
	protected static final double distance2d(final Point point,
			final Point lineStart, final Point lineEnd) {
		final double normalLength = Math.sqrt((lineEnd.get(0) - lineStart.get(0))
				* (lineEnd.get(0) - lineStart.get(0))
				+ (lineEnd.get(1) - lineStart.get(1))
				* (lineEnd.get(1) - lineStart.get(1)));
		return Math.abs((point.get(0) - lineStart.get(0))
				* (lineEnd.get(1) - lineStart.get(1))
				- (point.get(1) - lineStart.get(1))
				* (lineEnd.get(0) - lineStart.get(0)))
				/ normalLength;
	}

	/**
	 * Distance3d.
	 * 
	 * @param point
	 *            the point
	 * @param lineStart
	 *            the line start
	 * @param lineEnd
	 *            the line end
	 * @return the float
	 */
	protected static final double distance3d(final Point point,
			final Point lineStart, final Point lineEnd) {
		final Point lineVector = Vector.distanceVector(lineEnd, lineStart);
		return Vector.length(Vector.cross(
				Vector.distanceVector(point, lineStart), lineVector))
				/ Vector.length(lineVector);
	}

	/**
	 * Square.
	 * 
	 * @param f
	 *            the f
	 * @return the float
	 */
	static final double square(final double f) {
		return f * f;
	}

}
