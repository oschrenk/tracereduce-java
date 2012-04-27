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

import de.q2web.gis.trajectory.core.api.FloatPoint;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanFloatGeometry implements Geometry<Float> {

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public Float distance(final Point<Float> from, final Point<Float> to) {

		final int dimensions = from.getDimensions();
		float sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + square((to.get(i) - from.get(i)));
		}

		return (float) Math.sqrt(sumSquared);
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public Float distance(final Point<Float> point,
			final Point<Float> lineStart, final Point<Float> lineEnd) {

		final int dimensions = lineStart.getDimensions();
		if (dimensions == 2) {
			// TODO 2 dimensions
		}

		if (dimensions == 3) {
			return distance3d(point, lineStart, lineEnd);
		}

		throw new IllegalArgumentException("Invalid dimensions");
	}

	private static final float distance3d(final Point<Float> point,
			final Point<Float> lineStart, final Point<Float> lineEnd) {
		final Point<Float> lineVector = distanceVector(lineEnd, lineStart);
		return length(cross(distanceVector(point, lineStart), lineVector))
				/ length(lineVector);
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Geometry#compare(java.lang.Number,
	 * java.lang.Number)
	 */
	@Override
	public int compare(final Float a, final Float b) {
		return a.compareTo(b);
	}

	/**
	 * Square.
	 * 
	 * @param f
	 *            the f
	 * @return the float
	 */
	private static final float square(final float f) {
		return f * f;
	}

	/**
	 * Calculates p-a
	 * 
	 * @param p
	 * @param a
	 * @return
	 */
	private static final Point<Float> distanceVector(final Point<Float> p,
			final Point<Float> a) {
		final int dimensions = p.getDimensions();
		final float[] c = new float[dimensions];
		for (int i = 0; i < dimensions; i++) {
			c[i] = p.get(i) - a.get(i);
		}
		return new FloatPoint(c);

	}

	private static final Point<Float> cross(final Point<Float> a,
			final Point<Float> b) {
		final float[] c = new float[] {
				a.get(1) * b.get(2) - a.get(2) * b.get(1),
				a.get(2) * b.get(0) - a.get(0) * b.get(2),
				a.get(0) * b.get(1) - a.get(1) * b.get(0) };
		return new FloatPoint(c);
	}

	private static final float length(final Point<Float> p) {
		final int dimensions = p.getDimensions();
		float sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + square(p.get(i));
		}
		return (float) Math.sqrt(sumSquared);
	}

}
