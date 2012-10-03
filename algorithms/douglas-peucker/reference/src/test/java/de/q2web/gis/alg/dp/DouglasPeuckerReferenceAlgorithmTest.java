package de.q2web.gis.alg.dp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;
import de.q2web.gis.geom.EuclideanDistance;

public class DouglasPeuckerReferenceAlgorithmTest {

	private Coordinate start;
	private Coordinate end;
	private Coordinate[] coordinates;

	private double epsilon;

	@Before
	public void setup() {
		start = new Coordinate(1, 3);
		final Coordinate b = new Coordinate(2, 4);
		final Coordinate c = new Coordinate(3, 2);
		final Coordinate d = new Coordinate(4, 4);
		final Coordinate e = new Coordinate(5, 2);
		final Coordinate f = new Coordinate(6, 4);
		end = new Coordinate(7, 3);

		coordinates = new Coordinate[] { start, b, c, d, e, f, end };
		epsilon = 1;
	}

	@Test
	public void testJTS() {
		final GeometryFactory factory = new GeometryFactory();
		final CoordinateSequence coordinateSequence = factory
				.getCoordinateSequenceFactory().create(coordinates);

		final Geometry trace = new LineString(coordinateSequence, factory);
		final Geometry reducedTrace = DouglasPeuckerSimplifier.simplify(trace,
				epsilon);

		final Coordinate[] coordinates = reducedTrace.getCoordinates();
		assertTrue(coordinates.length == 2);
		assertEquals(start, coordinates[0]);
		assertEquals(end, coordinates[1]);
	}

	@Test
	public void crossTestWithJTS() {
		final List<Point> trace = transform(coordinates);
		final Algorithm algorithm = new DouglasPeuckerReferenceAlgorithm(
				new EuclideanDistance());
		final List<Point> reducedTrace = algorithm.run(trace, epsilon);

		assertTrue(reducedTrace.size() == 2);
		assertEquals(start.x, reducedTrace.get(0).get(0),0.0);
		assertEquals(start.y, reducedTrace.get(0).get(1),0.0);
		assertEquals(end.x, reducedTrace.get(1).get(0),0.0);
		assertEquals(end.y, reducedTrace.get(1).get(1),0.0);
	}

	private List<Point> transform(final Coordinate[] coordinates) {
		final List<Point> points = new ArrayList<Point>(coordinates.length);
		for (final Coordinate coordinate : coordinates) {
			points.add(new Point(new double[] { coordinate.x, coordinate.y }));
		}
		return points;
	}
}
