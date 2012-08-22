/*
 *******************************************************************************
 * EuclideanGeometry.java
 * $Id: $
 *
 *******************************************************************************
 *
 * Copyright:   Q2WEB GmbH
 *              quality to the web
 *
 *              Tel  : +49 (0) 211 / 159694-00	Kronprinzenstr. 82-84
 *              Fax  : +49 (0) 211 / 159694-09	40217 Düsseldorf
 *              eMail: info@q2web.de						http://www.q2web.de
 *
 *
 * Author:      University
 *
 * Created:     09.04.2012
 *
 * Copyright (c) 2009 Q2WEB GmbH.
 * All rights reserved.
 *
 *******************************************************************************
 */
package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 *
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanGeometry implements Geometry {

	@Override
	public double distance(final Point from, final Point to) {

		final int dimensions = from.getDimensions();
		double sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + square((to.get(i) - from.get(i)));
		}

		return Math.sqrt(sumSquared);
	}

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

		throw new IllegalArgumentException("Invalid dimensions");
	}

	/**
	 * is again given by projecting r onto v, giving
	 *
	 * denom = (sqrt((x_2-x_1)^2+(y_2-y_1)^2))
	 * distance=(|(x_2-x_1)(y_1-y_0)-(x_1-x_0)(y_2-y_1)|)/
	 *
	 *
	 * @return
	 */
	private static final double distance2d(final Point point,
			final Point lineStart, final Point lineEnd) {
		final double x_0 = point.get(0);
		final double y_0 = point.get(1);
		final double x_1 = lineStart.get(0);
		final double y_1 = lineStart.get(1);
		final double x_2 = lineEnd.get(0);
		final double y_2 = lineEnd.get(1);

		final double nom = Math.abs((x_2 - x_1) * (y_1 - y_0) - (x_1 - x_0)
				* (y_2 - y_1));
		final double denom = Math.sqrt((x_2 - x_1) * (x_2 - x_1) + (y_2 - y_1)
				* (y_2 - y_1));

		return nom / denom;
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
	private static final double distance3d(final Point point,
			final Point lineStart, final Point lineEnd) {
		final Point lineVector = distanceVector(lineEnd, lineStart);
		return length(cross(distanceVector(point, lineStart), lineVector))
				/ length(lineVector);
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Geometry#compare(double, double)
	 */
	@Override
	public int compare(final double a, final double b) {
		if (a < b) {
			return -1; // Neither val is NaN, thisVal is smaller
		}
		if (a > b) {
			return 1; // Neither val is NaN, thisVal is larger
		}

		// Cannot use doubleToRawLongBits because of possibility of NaNs.
		final long thisBits = Double.doubleToLongBits(a);
		final long anotherBits = Double.doubleToLongBits(b);

		return (thisBits == anotherBits ? 0 : // Values are equal
				(thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
						1)); // (0.0, -0.0) or (NaN, !NaN)
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

	/**
	 * Calculates p-a.
	 *
	 * @param p
	 *            the p
	 * @param a
	 *            the a
	 * @return the point
	 */
	private static final Point distanceVector(final Point p, final Point a) {
		final int dimensions = p.getDimensions();
		final double[] c = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			c[i] = p.get(i) - a.get(i);
		}
		return new Point(c);

	}

	/**
	 * Cross.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the point
	 */
	private static final Point cross(final Point a, final Point b) {
		final double[] c = new double[] {
				a.get(1) * b.get(2) - a.get(2) * b.get(1),
				a.get(2) * b.get(0) - a.get(0) * b.get(2),
				a.get(0) * b.get(1) - a.get(1) * b.get(0) };
		return new Point(c);
	}

	/**
	 * Length.
	 *
	 * @param p
	 *            the p
	 * @return the float
	 */
	private static final double length(final Point p) {
		final int dimensions = p.getDimensions();
		double sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + square(p.get(i));
		}
		return Math.sqrt(sumSquared);
	}

}
