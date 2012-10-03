package de.q2web.gis.alg.cs.ref;

import java.util.List;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;

/**
 * The Class ReferenceCubicSplinesAlgorithm.
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CubicSplinesReferenceAlgorithm implements Algorithm {

	/*
	 * @see de.q2web.gis.core.api.Algorithm#run(java.util.List,
	 * double)
	 */
	@Override
	public List<Point> run(final List<Point> points, final double epsilon) {

		/*
		 *
		 * p = Original Trace r = Reduced Set of Points e = epsilon, error bound
		 *
		 * ----
		 *
		 * r = p
		 *
		 * do { fore (j in r_j) {
		 *
		 *
		 *
		 * }
		 *
		 * if (e_min <= e) {
		 *
		 * }
		 *
		 * } until no further knot could be removed return reduced know sequence
		 */

		return null;
	}

}
