package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IStringConverter;

import de.q2web.gis.core.api.Distance;
import de.q2web.gis.geom.EuclideanDistance;
import de.q2web.gis.geom.HaversineDistance;
import de.q2web.gis.geom.SphericalDistance;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DistanceConverter implements IStringConverter<Distance> {

	/*
	 * @see com.beust.jcommander.IStringConverter#convert(java.lang.String)
	 */
	@Override
	public Distance convert(final String argument) {

		final String distance = argument.trim().toLowerCase();

		if (distance.equals(DistanceValidator.EUCLIDEAN)) {
			return new EuclideanDistance();
		} else if (distance.equals(DistanceValidator.SPHERICAL)) {
			return new SphericalDistance();
		} else if (distance.equals(DistanceValidator.HAVERSINE)) {
			return new HaversineDistance();
		}

		throw new IllegalArgumentException("No valid distance given");
	}
}