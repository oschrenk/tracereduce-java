package de.q2web.gis.alg.cs.opencl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.jocl.util.Arrays;

public class CubicSplinesRawOpenClAlgorithmTest {

	private static final float X[] = { 0, 10, 30, 50, 70, 90, 100 };
	private static final float Y[] = { 30, 130, 150, 150, 170, 220, 320 };

	private static final int LENGTH = X.length;

	private static List<Point> TRACE = new ArrayList<Point>();

	static {
		TRACE.add(new DoublePoint(new double[] { 0, 30 }));
		TRACE.add(new DoublePoint(new double[] { 10, 130 }));
		TRACE.add(new DoublePoint(new double[] { 30, 150 }));
		TRACE.add(new DoublePoint(new double[] { 50, 150 }));
		TRACE.add(new DoublePoint(new double[] { 70, 170 }));
		TRACE.add(new DoublePoint(new double[] { 90, 220 }));
		TRACE.add(new DoublePoint(new double[] { 100, 230 }));
	}

	private static final double EPSILON = 5;

	@Test
	public void test() {
		ConstrainedCubicSplinesRawOpenClAlgorithm.algorithm(X, Y, LENGTH);
	}

	@Test
	public void testAlgorithmWithNoExcludes() {

		final float distances[] = Arrays.prefilled(LENGTH - 2, Float.MAX_VALUE);
		final boolean excludes[] = Arrays.prefilled(LENGTH, false);

		System.out.println(java.util.Arrays.toString(distances));
		for (int id = 1; id < LENGTH - 1; id++) {
			ConstrainedCubicSplinesRawOpenClAlgorithm.algorithmWithExcludes(X,
					Y, distances, excludes, LENGTH, id);
			System.out.println(java.util.Arrays.toString(distances));
		}

	}

	@Test
	public void testAlgorithmWithAnExcludeInTheMiddle() {
		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { false, false, true, false, false, false,
				false };

		for (int id = 1; id < LENGTH - 1; id++) {
			ConstrainedCubicSplinesRawOpenClAlgorithm.algorithmWithExcludes(X,
					Y, distances, excludes, LENGTH, id);
		}
	}

	@Test
	public void testAlgorithmWithAnExcludeInTheBeginning() {

		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { true, false, false, false, false, false,
				false };

		for (int id = 1; id < LENGTH; id++) {
			ConstrainedCubicSplinesRawOpenClAlgorithm.algorithmWithExcludes(X,
					Y, distances, excludes, LENGTH, id);
		}
	}

	@Test
	public void testAlgorithmWithMultipleExcludes() {

		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { false, true, false, false, false, true,
				false };

		System.out.println(java.util.Arrays.toString(distances));
		for (int idToExclude = 1; idToExclude < LENGTH - 1; idToExclude++) {
			ConstrainedCubicSplinesRawOpenClAlgorithm.algorithmWithExcludes(X,
					Y, distances, excludes, LENGTH, idToExclude);
			System.out.println(java.util.Arrays.toString(distances));
		}
	}

	@Test
	public void testCubicSplinesRawOpenClAlgorithm() {
		final Algorithm algorithm = new ConstrainedCubicSplinesRawOpenClAlgorithm();
		final List<Point> run = algorithm.run(TRACE, EPSILON);
		System.out.println(run);
	}

}
