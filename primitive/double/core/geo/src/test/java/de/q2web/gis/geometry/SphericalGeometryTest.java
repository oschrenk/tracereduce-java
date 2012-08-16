package de.q2web.gis.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SphericalGeometryTest {

	/**
	 * 
	 * SOultion does not work correctly on antipodal points as the plane through
	 * A, B and the center is not completely defined,
	 * 
	 */
	@Test
	public void testAntiPodal() {

		final double radius = 1;
		final Geometry geometry = new SphericalGeometry(radius);

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 180d });
		final Point p = new Point(new double[] { 0d, 90d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(0d, distance, 0.0);
	}

	@Test
	public void testNonAntiPodal() {

		final double radius = 1;
		final Geometry geometry = new SphericalGeometry(radius);

		// lat-lng
		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });
		final Point p = new Point(new double[] { 90d, 0d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(Math.PI / 2, distance, 0.0);
	}

	@Test
	public void testNonAntiPodalNonNormalRadius() {

		final double radius = 2;
		final Geometry geometry = new SphericalGeometry(radius);

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });
		final Point p = new Point(new double[] { 90d, 90d });

		final double distance = geometry.distance(p, a, b);

		assertEquals(Math.PI, distance, 0.0);
	}
}
