package de.q2web.gis.alg.cs.ref;

import org.junit.Test;

import de.q2web.jocl.util.Arrays;

public class SplineInterpolationWithPointOmissionTest {

	private static final float X[] = { 0, 10, 30, 50, 70, 90, 100 };
	private static final float Y[] = { 30, 130, 150, 150, 170, 220, 320 };

	private static final int LENGTH = X.length;

	private static final SplineInterpolationWithPointOmission SPLINE_INTERPOLATION = new SplineInterpolationWithPointOmission();

	@Test
	public void testInterpolationWithNoExcludes() {

		final float distances[] = Arrays.prefilled(LENGTH - 2, Float.MAX_VALUE);
		
		// TODO make excludes LENGTH -2,
		// for now initialize it with full length
		final boolean excludes[] = Arrays.prefilled(LENGTH, false);

		System.out.println(java.util.Arrays.toString(distances));
		for (int id = 1; id < LENGTH - 1; id++) {
			SPLINE_INTERPOLATION.compute(X, Y, distances, excludes, LENGTH, id);
			System.out.println(java.util.Arrays.toString(distances));
		}

	}

	@Test
	public void testInterpolationWithAnExcludeInTheBeginning() {

		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { true, false, false, false, false, false,
				false };

		for (int id = 1; id < LENGTH - 1; id++) {
			SPLINE_INTERPOLATION.compute(X, Y, distances, excludes, LENGTH, id);
			System.out.println(java.util.Arrays.toString(distances));
		}
	}

	@Test
	public void testInterpolationWithAnExcludeInTheMiddle() {
		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { false, false, true, false, false, false,
				false };

		SPLINE_INTERPOLATION.compute(X, Y, distances, excludes, LENGTH, 2);
		System.out.println(java.util.Arrays.toString(distances));
		
		for (int id = 1; id < LENGTH - 1; id++) {
			SPLINE_INTERPOLATION.compute(X, Y, distances, excludes, LENGTH, id);
			System.out.println(java.util.Arrays.toString(distances));
		}
	}

	@Test
	public void testInterpolationWithMultipleExcludes() {

		final float distances[] = new float[LENGTH];
		final boolean excludes[] = { false, true, false, false, false, true,
				false };

		System.out.println(java.util.Arrays.toString(distances));
		for (int idToExclude = 1; idToExclude < LENGTH - 1; idToExclude++) {
			SPLINE_INTERPOLATION.compute(X, Y, distances, excludes, LENGTH,
					idToExclude);
			System.out.println(java.util.Arrays.toString(distances));
		}
	}

}
