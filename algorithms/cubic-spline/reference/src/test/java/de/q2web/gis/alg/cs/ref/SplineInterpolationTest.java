package de.q2web.gis.alg.cs.ref;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.q2web.gis.core.api.Point;

public class SplineInterpolationTest {

	private static final float X[] = { 0, 10, 30, 50, 70, 90, 100 };
	private static final float Y[] = { 30, 130, 150, 150, 170, 220, 320 };

	private static final int LENGTH = X.length;

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

	@SuppressWarnings("deprecation")
	private static final SplineInterpolation SPLINE_INTERPOLATION = new SplineInterpolation();

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		SPLINE_INTERPOLATION.compute(X, Y, LENGTH);
	}
}
