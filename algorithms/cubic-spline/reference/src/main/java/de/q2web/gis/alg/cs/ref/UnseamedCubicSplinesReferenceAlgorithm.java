package de.q2web.gis.alg.cs.ref;

import java.util.Iterator;
import java.util.List;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;
import de.q2web.jocl.util.Arrays;

/**
 * The Class CubicSplinesReferenceAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class UnseamedCubicSplinesReferenceAlgorithm implements Algorithm {

	private final SplineInterpolationWithPointOmission splineInterpolation = new SplineInterpolationWithPointOmission();

	/*
	 * @see de.q2web.gis.core.api.Algorithm#run(java.util.List, double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		final int length = trace.size();
		final float[] srcArrayX = new float[length];
		final float[] srcArrayY = new float[length];

		for (int i = 0; i < trace.size(); i++) {
			final Point point = trace.get(i);
			srcArrayX[i] = (float) point.get(0);
			srcArrayY[i] = (float) point.get(1);
		}

		float distances[];
		final boolean excludes[] = Arrays.prefilled(length, false);
		int positionOfMinimum;
		do {
			distances = Arrays.prefilled(length - 2, Float.MAX_VALUE);
			for (int idToExclude = 1; idToExclude < length - 1; idToExclude++) {
				if (!excludes[idToExclude]) {
					splineInterpolation.compute(srcArrayX, srcArrayY,
							distances, excludes, length, idToExclude);
				}
			}

			// distances is length - 2 big
			positionOfMinimum = Arrays.positionOfMinimum(distances);
			// TODO this check is feels weird, try to remove it
			if (positionOfMinimum >= 0
					&& distances[positionOfMinimum] <= epsilon) {
				// positions are counted from knot 2, as first and last knot are
				// always included
				excludes[positionOfMinimum + 1] = true;
			}
		} while (positionOfMinimum >= 0);

		int i = 0;
		final Iterator<Point> iterator = trace.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			if (excludes[i]) {
				iterator.remove();
			}
			i++;
		}

		return trace;
	}

}
