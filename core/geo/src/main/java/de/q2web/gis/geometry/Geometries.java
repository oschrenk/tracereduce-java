package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Precision;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Geometries {

	@SuppressWarnings({ "rawtypes", "unused" })
	public static Geometry build(final Precision precision) {

		// TODO more geometries
		return new EuclideanFloatGeometry();
	}

}
