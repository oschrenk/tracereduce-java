package de.q2web.gis.ui.cli.util;

import java.util.ArrayList;
import java.util.List;

import de.q2web.gis.alg.cs.opencl.CubicSplinesOpenClAlgorithm;
import de.q2web.gis.alg.cs.ref.CubicSplinesReferenceAlgorithm;
import de.q2web.gis.alg.dp.DouglasPeuckerReferenceAlgorithm;
import de.q2web.gis.alg.dp.opencl.DouglasPeuckerOpenClAlgorithm;
import de.q2web.gis.alg.lo.opencl.LinearOptimumOpenClAlgorithm;
import de.q2web.gis.alg.lo.ref.LinearOptimumReferenceAlgorithm;
import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Distance;
import de.q2web.gis.geom.EuclideanDistance;
import de.q2web.gis.geom.HaversineDistance;
import de.q2web.gis.geom.SphericalDistance;

/**
 * A factory for creating algorithms.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmFactory {

	private static final List<String> validAlgorithmTypes = new ArrayList<String>();

	private static final String DOUGLAS_PEUCKER = "douglas-peucker";
	private static final String IMAI = "imai";
	private static final String SPLINE = "spline";

	static {
		validAlgorithmTypes.add(DOUGLAS_PEUCKER);
		validAlgorithmTypes.add(IMAI);
		validAlgorithmTypes.add(SPLINE);
	}

	/**
	 * Builds the.
	 * 
	 * @param value
	 *            the value
	 * @return the algorithm template
	 */
	public static Algorithm build(final String name, final Distance distance) {
		final String[] template = name.trim().toLowerCase().split("#");

		if (template.length != 2) {
			throw new IllegalArgumentException(
					"Not a valid algorithm identfier");
		}
		final String type = template[0];
		final String variant = template[1];
		if (!validAlgorithmTypes.contains(type)) {
			throw new IllegalArgumentException("Not a valid algorithm type");
		}

		if (type.equals(DOUGLAS_PEUCKER)) {
			return getDouglasPeuckerAlgorithm(variant, distance);
		} else if (type.equals(IMAI)) {
			return getImaiAlgorithm(variant, distance);
		} else if (type.equals(SPLINE)) {
			return getCubicSplineAlgorithm(variant, distance);
		}

		// Can't happen as we checked all types before
		throw new IllegalArgumentException("Guru meditation error");
	}

	private static Algorithm getDouglasPeuckerAlgorithm(final String variant,
			final Distance distance) {

		if (variant.equals("reference")) {
			return new DouglasPeuckerReferenceAlgorithm(distance);
		} else if (variant.equals("opencl")) {

			if (distance instanceof EuclideanDistance) {
				return new DouglasPeuckerOpenClAlgorithm(
						distance,
						DouglasPeuckerOpenClAlgorithm.KERNEL_CROSSTRACK_EUCLIDEAN);
			} else if (distance instanceof SphericalDistance) {
				return new DouglasPeuckerOpenClAlgorithm(
						distance,
						DouglasPeuckerOpenClAlgorithm.KERNEL_CROSSTRACK_SPHERICAL);
			} else if (distance instanceof HaversineDistance) {
				return new DouglasPeuckerOpenClAlgorithm(
						distance,
						DouglasPeuckerOpenClAlgorithm.KERNEL_CROSSTRACK_HAVERSINE);
			}

			throw new IllegalArgumentException("Not a valid distance.");

		}

		throw new IllegalArgumentException("Not a valid variant.");
	}

	private static Algorithm getImaiAlgorithm(final String variant,
			final Distance distance) {
		if (variant.equals("reference")) {
			return new LinearOptimumReferenceAlgorithm(distance);
		} else if (variant.equals("opencl")) {

			if (distance instanceof EuclideanDistance) {
				return new LinearOptimumOpenClAlgorithm(
						LinearOptimumOpenClAlgorithm.KERNEL_CROSSTRACK_EUCLIDEAN);
			} else if (distance instanceof SphericalDistance) {
				return new LinearOptimumOpenClAlgorithm(
						LinearOptimumOpenClAlgorithm.KERNEL_CROSSTRACK_SPHERICAL);
			} else if (distance instanceof HaversineDistance) {
				return new LinearOptimumOpenClAlgorithm(
						LinearOptimumOpenClAlgorithm.KERNEL_CROSSTRACK_HAVERSINE);
			}

			throw new IllegalArgumentException("Not a valid distance.");
		}

		throw new IllegalArgumentException("Not a valid variant.");
	}

	private static Algorithm getCubicSplineAlgorithm(final String variant,
			final Distance distance) {
		if (variant.equals("reference")) {
			return new CubicSplinesReferenceAlgorithm();
		} else if (variant.equals("opencl")) {
			return new CubicSplinesOpenClAlgorithm();
		}

		throw new IllegalArgumentException("Not a valid variant.");
	}

}
