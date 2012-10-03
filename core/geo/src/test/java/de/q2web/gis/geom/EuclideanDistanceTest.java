package de.q2web.gis.geom;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.core.api.Distance;
import de.q2web.gis.core.api.Point;
import de.q2web.gis.geom.EuclideanDistance;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanDistanceTest {

	private Distance distance;

	@Before
	public void setUp() {
		distance = new EuclideanDistance();
	}

	@Test
	public void distanceOfPoints() {
		final Point from = new Point(new double[] { 0, 0 });
		final Point to = new Point(new double[] { 3, 4 });
		final double d = distance.distance(from, to);

		assertEquals(5f, d, 0.0);
	}

	@Test
	public void distanceOfPointToLine3D() {
		final Point point = new Point(new double[] { 10, 5, 7 });
		final Point lineStart = new Point(new double[] { -2, 1, 7 });
		final Point lineEnd = new Point(new double[] { 2, 2, 4 });
		final double d = distance.distance(point, lineStart, lineEnd);

		assertEquals(Math.sqrt(56), d, 0.0);
	}

	@Test
	public void compare() {
		final double a = 10;
		final double b = 0;

		assertEquals(true, a > b);
	}

}
