package de.q2web.gis.geom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.q2web.gis.core.api.Distance;
import de.q2web.gis.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SphericalDistanceTest {

	/**
	 * 
	 * Solultion does not work correctly on antipodal points as the plane
	 * through A, B and the center is not completely defined,
	 * 
	 */
	@Test
	public void testAntiPodal() {

		final double radius = 1;
		final Distance distance = new SphericalDistance(radius);

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 180d });
		final Point p = new Point(new double[] { 0d, 90d });

		final double d = distance.distance(p, a, b);

		assertEquals(0d, d, 0.0);
	}

	@Test
	public void testNonAntiPodal() {

		final double radius = 1;
		final Distance distance = new SphericalDistance(radius);

		// lat-lng
		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });
		final Point p = new Point(new double[] { 90d, 0d });

		final double d = distance.distance(p, a, b);

		assertEquals(Math.PI / 2, d, 0.0);
	}

	@Test
	public void testNonAntiPodalNonNormalRadius() {

		final double radius = 2;
		final Distance distance = new SphericalDistance(radius);

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });
		final Point p = new Point(new double[] { 90d, 90d });

		final double d = distance.distance(p, a, b);

		assertEquals(Math.PI, d, 0.0);
	}

	@Test
	public void testRealDistances() {
		final Distance distance = new SphericalDistance();

		final Point a = new Point(new double[] { 0d, 0d });
		final Point b = new Point(new double[] { 0d, 90d });

		final double d = distance.distance(a, b);

		System.out.println(d);

	}

	@Test
	// FIXME the reference uses haversine distance
	// therefore comparison not applicable
	// find better example
	// cross check with http://williams.best.vwh.net/avform.htm#Example
	public void testDtoLAXtoJFK() {
		// LAX: (33deg 57min N, 118deg 24min W)
		Point LAX = new Point(118 + 24d / 60, 33 + 57d / 60);
		// JFK: (40deg 38min N, 73deg 47min W)
		Point JFK = new Point(73 + 47d / 60, 40 + 38d / 60);
		// (D): N34:30 W116:30
		Point D = new Point(116 + 30d / 60, 34 + 30d / 60);

		final double distance = new SphericalDistance().distance(D, LAX, JFK);

		assertEquals(-13810.91, distance, 0.01d);
	}
}
