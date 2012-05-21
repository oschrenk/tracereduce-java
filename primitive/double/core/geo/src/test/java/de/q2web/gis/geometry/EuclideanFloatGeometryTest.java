package de.q2web.gis.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanFloatGeometryTest {

	private Geometry geometry;

	@Before
	public void setUp() {
		geometry = new EuclideanFloatGeometry();
	}

	@Test
	public void distanceOfPoints() {
		final Point from = new DoublePoint(new double[] { 0, 0 });
		final Point to = new DoublePoint(new double[] { 3, 4 });
		final double distance = geometry.distance(from, to);

		assertEquals(5f, distance, 0.0);
	}

	@Test
	public void distanceOfPointToLine3D() {
		final Point point = new DoublePoint(new double[] { 10, 5, 7 });
		final Point lineStart = new DoublePoint(new double[] { -2, 1, 7 });
		final Point lineEnd = new DoublePoint(new double[] { 2, 2, 4 });
		final double distance = geometry.distance(point, lineStart, lineEnd);

		assertEquals(Math.sqrt(56), distance, 0.0);
	}

	@Test
	public void compare() {
		final double a = 10;
		final double b = 0;

		assertEquals(true, geometry.compare(a, b) > 0);
	}

}
