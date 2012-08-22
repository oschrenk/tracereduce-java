package de.q2web.gis.alg.la.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.q2web.gis.geometry.EuclideanGeometry;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

public class LinearApproximationReferenceAlgorithmTest {

	private Point start;
	private Point end;
	private List<Point> trace;

	private double epsilon;

	@Before
	public void setup() {
		start = new Point(new double[] { 1d, 3d });
		final Point b = new Point(new double[] { 2d, 4d });
		final Point c = new Point(new double[] { 3d, 2d });
		final Point d = new Point(new double[] { 4d, 4d });
		final Point e = new Point(new double[] { 5d, 2d });
		final Point f = new Point(new double[] { 6d, 4d });
		end = new Point(new double[] { 7d, 3d });

		trace = new ArrayList<Point>();
		trace.add(start);
		trace.add(b);
		trace.add(c);
		trace.add(d);
		trace.add(e);
		trace.add(f);
		trace.add(end);

		epsilon = 1;
	}

	// TODO FIXME wrong weights
	@Ignore
	@Test
	public void test() {
		final Geometry geometry = new EuclideanGeometry();
		final Algorithm referenceAlgorithm = new LinearApproximationReferenceAlgorithm(
				geometry);

		final List<Point> reducedTrace = referenceAlgorithm.run(trace, epsilon);

		assertTrue(reducedTrace.size() == 2);
		assertEquals(start, reducedTrace.get(0));
		assertEquals(end, reducedTrace.get(1));
	}

}
