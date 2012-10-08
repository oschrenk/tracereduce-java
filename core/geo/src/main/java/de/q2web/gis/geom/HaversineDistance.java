package de.q2web.gis.geom;

import de.q2web.gis.core.api.Distance;
import de.q2web.gis.core.api.Point;

/**
 * The Class HaversineDistance.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class HaversineDistance implements Distance {

	private static final int LATITUDE = 1;
	private static final int LONGITUDE = 0;
	/** The Constant EARTH_VOLUMETRIC_MEAN_RADIUS. */
	public static final int EARTH_VOLUMETRIC_MEAN_RADIUS = 6371000;

	/*
	 * @see de.q2web.gis.core.api.Distance#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.core.api.Point)
	 */
	/**
	 * Lat-Lng[-Height] in spherical coordinates.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the double
	 */
	@Override
	public double distance(final Point from, final Point to) {
		return haversineDistance(from, to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.q2web.gis.core.api.Distance#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.core.api.Point,
	 * de.q2web.gis.core.api.Point)
	 */
	@Override
	public double distance(final Point point, final Point lineStart,
			final Point lineEnd) {
		return Math.abs(distance2d(point, lineStart, lineEnd));
	}

	private static final double distance2d(final Point point,
			final Point lineStart, final Point lineEnd) {

		final double r = EARTH_VOLUMETRIC_MEAN_RADIUS;
		final double b12 = orthodromeBearing(lineStart, lineEnd);
		final double b13 = orthodromeBearing(lineStart, point);
		final double d13 = haversineDistance(lineStart, point);

		// @formatter:off
		final double dt = //
		Math.asin( //
		Math.sin(d13 / r) //
				* Math.sin(b13 - b12) //
		) * r; //
		// @formatter:on

		return dt;
	}

	private static double orthodromeBearing(final Point from, final Point to) {
		// read longitude as x
		// read latitude as y

		return orthodromeBearing(from.get(0), from.get(1), to.get(0), to.get(1));
	}

	private static double orthodromeBearing(final double lon1, double lat1,
			final double lon2, double lat2) {
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		final double deltaLongitude = Math.toRadians(lon2 - lon1);

		final double y = Math.sin(deltaLongitude) * Math.cos(lat2);
		final double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(deltaLongitude);

		return Math.atan2(y, x);
	}

	private static double haversineDistance(final Point from, final Point to) {
		// read x longitude
		// read y as latitude

		return haversineDistance(from.get(LONGITUDE), from.get(LATITUDE),
				to.get(LONGITUDE), to.get(LATITUDE));
	}

	private static double haversineDistance(final double longitudeFrom,
			final double latitudeFrom, final double longitudeTo,
			final double latitudeTo) {
		final double deltaLatitude = Math.toRadians(latitudeFrom - latitudeTo);
		final double deltaLongitude = Math
				.toRadians((longitudeFrom - longitudeTo));

		final double sinusHalfDeltaLatitude = Math.sin(deltaLatitude / 2);
		final double sinusHalfDeltaLongitude = Math.sin(deltaLongitude / 2);

		final double a = sinusHalfDeltaLatitude * sinusHalfDeltaLatitude
				+ Math.cos(Math.toRadians(latitudeFrom))
				* Math.cos(Math.toRadians(latitudeTo))
				* sinusHalfDeltaLongitude * sinusHalfDeltaLongitude;
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_VOLUMETRIC_MEAN_RADIUS * c;
	}
}
