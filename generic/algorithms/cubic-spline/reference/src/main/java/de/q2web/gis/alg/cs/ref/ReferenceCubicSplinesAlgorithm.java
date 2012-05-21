package de.q2web.gis.alg.cs.ref;

import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 * @param <P>
 */
public class ReferenceCubicSplinesAlgorithm<P extends Number> implements
Algorithm<P> {

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * java.lang.Number)
	 */
	@Override
	public List<Point<P>> run(final List<Point<P>> points, final P epsilon) {

		/*

		p = Original Trace
		r = Reduced Set of Points
		e = epsilon, error bound

		----

		r = p

		do {
			fore (j in r_j) {



			}

			if (e_min <= e) {

			}

		} until no further knot could be removed
		return reduced know sequence


		 */

		return null;
	}

}
