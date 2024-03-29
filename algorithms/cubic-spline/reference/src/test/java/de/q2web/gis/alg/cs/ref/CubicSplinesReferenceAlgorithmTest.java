package de.q2web.gis.alg.cs.ref;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;

public class CubicSplinesReferenceAlgorithmTest {

	private static List<Point> TRACE = new ArrayList<Point>();

	static {
		TRACE.add(new Point(new double[] { 0, 30 }));
		TRACE.add(new Point(new double[] { 10, 130 }));
		TRACE.add(new Point(new double[] { 30, 150 }));
		TRACE.add(new Point(new double[] { 50, 150 }));
		TRACE.add(new Point(new double[] { 70, 170 }));
		TRACE.add(new Point(new double[] { 90, 220 }));
		TRACE.add(new Point(new double[] { 100, 230 }));
	}

	private static final double EPSILON = 5;

	@Test
	public void testCubicSplinesReferenceAlgorithm() {
		final Algorithm algorithm = new CubicSplinesReferenceAlgorithm();
		final List<Point> run = algorithm.run(TRACE, EPSILON);
		System.out.println(run);
	}

}
