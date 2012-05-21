package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Geometry;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Geometries {

	@SuppressWarnings({})
	public static Geometry build() {
		// TODO more geometries
		return new EuclideanFloatGeometry();
	}

}
