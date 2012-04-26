package de.q2web.gis.algorithms.core;

import de.q2web.gis.alg.dp.DouglasPeuckerAlgorithm;
import de.q2web.gis.alg.la.jgrapht.JGraphTLinearAppoximationAlgorithm;
import de.q2web.gis.alg.la.jung.JungLinearAppoximationAlgorithm;
import de.q2web.gis.geometry.Geometries;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Precision;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Algorithms {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Algorithm build(final AlgorithmTemplate algorithmTemplate,
			final Precision precision) {

		// for now we always use default geometry
		final Geometry geometry = Geometries.build(precision);

		switch (precision) {
		case DOUBLE:
			return buildDouble(algorithmTemplate, geometry);
		case FLOAT:
			return buildFloat(algorithmTemplate, geometry);
		}

		// can never happen
		return null;
	}

	@SuppressWarnings("incomplete-switch")
	private static Algorithm<Double> buildDouble(
			final AlgorithmTemplate algorithmTemplate,
			final Geometry<Double> geometry) {

		switch (algorithmTemplate.getType()) {
		case DOUGLAS_PEUCKER:
			return new DouglasPeuckerAlgorithm<Double>(geometry);
		case CUBIC_SPLINES:
			// TODO not yet implemented
			throw new IllegalArgumentException("Not yet implemented");
		case LINEAR_APPROXIMATION:

			switch (algorithmTemplate.getVariant()) {
			case JGRAPHT:
				return new JGraphTLinearAppoximationAlgorithm<Double>(geometry);
			case JUNG:
				return new JungLinearAppoximationAlgorithm<Double>(geometry);
			}
		}
		return null;
	}

	@SuppressWarnings("incomplete-switch")
	private static Algorithm<Float> buildFloat(
			final AlgorithmTemplate algorithmTemplate,
			final Geometry<Float> geometry) {

		switch (algorithmTemplate.getType()) {
		case DOUGLAS_PEUCKER:
			return new DouglasPeuckerAlgorithm<Float>(geometry);
		case CUBIC_SPLINES:
			// TODO not yet implemented
			throw new IllegalArgumentException("Not yet implemented");
		case LINEAR_APPROXIMATION:

			switch (algorithmTemplate.getVariant()) {
			case JGRAPHT:
				return new JGraphTLinearAppoximationAlgorithm<Float>(geometry);
			case JUNG:
				return new JungLinearAppoximationAlgorithm<Float>(geometry);
			}
		}
		return null;

	}

}
