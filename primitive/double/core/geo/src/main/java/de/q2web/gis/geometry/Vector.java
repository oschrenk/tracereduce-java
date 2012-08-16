package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Vector {

	/**
	 * Calculates p-a.
	 * 
	 * @param p
	 *            the p
	 * @param a
	 *            the a
	 * @return the point
	 */
	static final Point distanceVector(final Point p, final Point a) {
		final int dimensions = p.getDimensions();
		final double[] c = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			c[i] = p.get(i) - a.get(i);
		}
		return new DoublePoint(c);

	}

	/**
	 * Length.
	 * 
	 * @param p
	 *            the p
	 * @return the float
	 */
	static final double length(final Point p) {
		final int dimensions = p.getDimensions();
		double sumSquared = 0;
		for (int i = 0; i < dimensions; i++) {
			sumSquared = sumSquared + EuclideanGeometry.square(p.get(i));
		}
		return Math.sqrt(sumSquared);
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
	static final Point cross(final Point a, final Point b) {
		final double[] c = new double[] {
				a.get(1) * b.get(2) - a.get(2) * b.get(1),
				a.get(2) * b.get(0) - a.get(0) * b.get(2),
				a.get(0) * b.get(1) - a.get(1) * b.get(0) };
		return new DoublePoint(c);
	}

	static final double dot(final Point a, final Point b) {
		final int dimensions = a.getDimensions();
		double sum = 0;
		for (int i = 0; i < dimensions; i++) {
			sum = sum + a.get(i) * b.get(i);
		}
		return sum;
	}

	static final Point mult(final double d, final Point p) {
		final double[] c = new double[] { d * p.get(0), d * p.get(1),
				d * p.get(2) };
		return new DoublePoint(c);
	}

}
