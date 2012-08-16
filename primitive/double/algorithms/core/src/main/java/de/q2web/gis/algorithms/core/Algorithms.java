package de.q2web.gis.algorithms.core;

import de.q2web.gis.alg.dp.ReferenceDouglasPeucker;
import de.q2web.gis.alg.la.jgrapht.JGraphTLinearAppoximationAlgorithm;
import de.q2web.gis.alg.la.jung.JungLinearAppoximationAlgorithm;
import de.q2web.gis.geometry.Geometries;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.Geometry;

/**
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Algorithms {

	public static Algorithm build(final AlgorithmTemplate algorithmTemplate) {

		// for now we always use default geometry
		final Geometry geometry = Geometries.build();
		return buildDouble(algorithmTemplate, geometry);
	}

	@SuppressWarnings("incomplete-switch")
	private static Algorithm buildDouble(
			final AlgorithmTemplate algorithmTemplate, final Geometry geometry) {

		switch (algorithmTemplate.getType()) {
		case DOUGLAS_PEUCKER:
			return new ReferenceDouglasPeucker(geometry);
		case CUBIC_SPLINES:
			// TODO not yet implemented
			throw new IllegalArgumentException("Not yet implemented");
		case LINEAR_APPROXIMATION:

			switch (algorithmTemplate.getVariant()) {
			case JGRAPHT:
				return new JGraphTLinearAppoximationAlgorithm(geometry);
			case JUNG:
				return new JungLinearAppoximationAlgorithm(geometry);
			}
		}
		return null;
	}

}
