package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IStringConverter;

import de.q2web.gis.geometry.EuclideanGeometry;
import de.q2web.gis.geometry.SphericalGeometry;
import de.q2web.gis.geometry.SpheroidalGeometry;
import de.q2web.gis.trajectory.core.api.Geometry;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class GeometryConverter implements IStringConverter<Geometry> {

	/*
	 * @see com.beust.jcommander.IStringConverter#convert(java.lang.String)
	 */
	@Override
	public Geometry convert(final String argument) {

		final String geometry = argument.trim().toLowerCase();

		if (geometry.equals(GeometryValidator.EUCLIDEAN)) {
			return new EuclideanGeometry();
		} else if (geometry.equals(GeometryValidator.SPHERICAL)) {
			return new SphericalGeometry();
		} else if (geometry.equals(GeometryValidator.SPHEROIDAL)) {
			return new SpheroidalGeometry();
		}

		throw new IllegalArgumentException("No valid geoemtry given");
	}
}