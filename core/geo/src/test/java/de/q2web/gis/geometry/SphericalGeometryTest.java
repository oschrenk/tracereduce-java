package de.q2web.gis.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SphericalGeometryTest {

	/**
	 * 
	 * Solultion does not work correctly on antipodal points as the plane
	 * through A, B and the center is not completely defined,
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

	@Test
	public void testRealDistances() {
		final Geometry geometry = new SphericalGeometry();

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });

		final double distance = geometry.distance(a, b);

		System.out.println(distance);

	}

	// @Ignore
	@Test
	// cross check with http://williams.best.vwh.net/avform.htm#Example
	public void testDtoLAXtoJFK() {
		// LAX: (33deg 57min N, 118deg 24min W)
		Point LAX = new Point(33 + 57d / 60, 118 + 24d / 60);
		// JFK: (40deg 38min N, 73deg 47min W)
		Point JFK = new Point(40 + 38d / 60, 73 + 47d / 60);
		// (D): N34:30 W116:30
		Point D = new Point(34 + 30d / 60, 116 + 30d / 60);

		final double distance = new SphericalGeometry().distance(D, LAX, JFK);

		assertEquals(-13810.91, distance, 0.01d);
	}
}
