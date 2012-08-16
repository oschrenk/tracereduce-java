package de.q2web.gis.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SpheroidalGeometryTest {

	/**
	 *
	 * Solution does not work correctly on antipodal points as the plane through
	 * A, B and the center is not completely defined,
	 *
	 */
	@Test
	public void testAntiPodal() {

		final double semiMajorAxis = 1;
		final double semiMinorAxis = 1;
		final Geometry geometry = new SpheroidalGeometry(semiMajorAxis,
				semiMinorAxis);

		final Point a = new DoublePoint(new double[] { 0d, 0d });
		final Point b = new DoublePoint(new double[] { 0d, 180d });
		final Point p = new DoublePoint(new double[] { 0d, 90d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(0d, distance, 0.0);
	}

	@Ignore
	// TODO
	public void testNonAntiPodal() {

		final double semiMajorAxis = 1;
		final double semiMinorAxis = 1;
		final Geometry geometry = new SpheroidalGeometry(semiMajorAxis,
				semiMinorAxis);

		final Point a = new DoublePoint(new double[] { 0d, 0d });
		final Point b = new DoublePoint(new double[] { 0d, 90d });
		final Point p = new DoublePoint(new double[] { 90d, 90d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(Math.PI / 2, distance, 0.0);
	}

	@Ignore
	// TODO
	public void testNonAntiPodalNonNormalRadius() {

		final double semiMajorAxis = 2;
		final double semiMinorAxis = 2;
		final Geometry geometry = new SpheroidalGeometry(semiMajorAxis,
				semiMinorAxis);
		final Point a = new DoublePoint(new double[] { 0d, 0d });
		final Point b = new DoublePoint(new double[] { 0d, 90d });
		final Point p = new DoublePoint(new double[] { 90d, 90d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(Math.PI, distance, 0.0);
	}
}
