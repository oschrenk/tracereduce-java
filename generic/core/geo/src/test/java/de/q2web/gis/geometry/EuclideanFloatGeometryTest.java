package de.q2web.gis.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.trajectory.core.api.FloatPoint;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanFloatGeometryTest {

	private Geometry<Float> geometry;

	@Before
	public void setUp() {
		geometry = new EuclideanFloatGeometry();
	}

	@Test
	public void distanceOfPoints() {
		final Point<Float> from = new FloatPoint(new float[] { 0, 0 });
		final Point<Float> to = new FloatPoint(new float[] { 3, 4 });
		final float distance = geometry.distance(from, to);

		assertEquals(5f, distance, 0.0);
	}

	@Test
	public void distanceOfPointToLine3D() {
		final Point<Float> point = new FloatPoint(new float[] { 10, 5, 7 });
		final Point<Float> lineStart = new FloatPoint(new float[] { -2, 1, 7 });
		final Point<Float> lineEnd = new FloatPoint(new float[] { 2, 2, 4 });
		final float distance = geometry.distance(point, lineStart, lineEnd);

		assertEquals((float) Math.sqrt(56), distance, 0.0);
	}

	@Test
	public void compare() {
		final float a = 10;
		final float b = 0;

		assertEquals(true, geometry.compare(a, b) > 0);
	}

}
