package util;

import java.util.ArrayList;

import de.q2web.gis.alg.la.ref.dijkstra.Vertex;

/**
 * This class provides static methods for the work with geodetic coordinates
 * 
 * @author Markus Koegel, Heinrich Heine Universität Düsseldorf
 *         (koegel@cs.uni-duesseldorf.de)
 */
public class Geo {
	/** Earth radius WGS84 */
	public static double earthRadius = 6378.137;
	/** Earth eccentricity WGS84 */
	public static double eccentricity = 0.081819190842622;

	/**
	 * This method calculates the radius of an ellipsoidal Earth at a given
	 * latitude
	 * 
	 * @param lat
	 *            The lateral angle in degree
	 * @return The radius in meters of an ellipsoidal globe at latitude lat
	 */
	public static double earthRadiusEcc(final double lat) {
		/*
		 * the radius of curvature of an ellipsoidal Earth in the plane of the
		 * meridian is given by
		 * 
		 * R' = a * (1 - e^2) / (1 - e^2 * (sin(lat))^2)^(3/2)
		 * 
		 * 2 a * (1 - e ) R' = ----------------------- 2 2 3/2 (1 - e * sin(lat)
		 * )
		 * 
		 * where a is the equatorial radius, b is the polar radius, and e is the
		 * eccentricity of the ellipsoid = sqrt(1 - b^2/a^2)
		 * 
		 * a = 6378.137 km (3963 mi) Equatorial radius (surface to center
		 * distance) b = 6356.752 km (3950 mi) Polar radius (surface to center
		 * distance) e = 0.081819190842622 Eccentricity
		 */
		final double e_sq = eccentricity * eccentricity;
		final double numerator = earthRadius * (1.0 - e_sq);
		final double sinLat = Math.sin(Math.toRadians(lat));
		final double denominator = Math.pow(1.0 - e_sq * sinLat * sinLat, 1.5);
		return 1000.0 * numerator / denominator; /* result is in meters */
	}

	/**
	 * This method calculates the grid coordinates with respect to a geo-point
	 * of origin
	 * 
	 * @param geopos_r
	 *            geo coordinates in degree of a reference point (longitude,
	 *            latitude)
	 * @param geopos_i
	 *            geo coordinates in degree of the point to be converted
	 *            (longitude, latitude)
	 * @return the grid coordinates (x, y) of the converted point
	 */
	public static double[] geo2grid(final double[] geopos_r,
			final double[] geopos_i) {
		final int dimensions = geopos_i.length;
		final double[] gridpos = new double[dimensions];
		final double r = earthRadiusEcc(geopos_r[1]);
		/*
		 * Differenz auf x-Achse:
		 * 
		 * Umfang des Breitenkreises in Metern u =
		 * Math.cos(Math.toRadians(geopos_r[1])) * r * 2 * PI
		 * 
		 * Abschnitt zwischen den Längengraden in Metern d = u * (geopos_i[0] -
		 * geopos_r[0])/360 = Math.cos(Math.toRadians(geopos_r[1])) * r * PI/180
		 * * (geopos_i[0] - geopos_r[0])
		 */
		// gridpos[0] = Math.toRadians(r) *
		// Math.cos(Math.toRadians(geopos_r[1])) * (geopos_i[0] - geopos_r[0]);
		gridpos[0] = Math.cos(Math.toRadians(geopos_r[1])) * (r * Math.PI)
				/ 180 * (geopos_i[0] - geopos_r[0]);

		/*
		 * Differenz auf y-Achse:
		 * 
		 * Umfang des Längenkreises in Metern u = r * 2 * PI
		 * 
		 * Abschnitt zwischen den Breitengraden in Metern d = u * (geopos_i[1] -
		 * geopos_r[1])/360 = r * PI/180 * (geopos_i[1] - geopos_r[1])
		 */
		// gridpos[1] = Math.toRadians(r) * (geopos_i[1] - geopos_r[1]);
		gridpos[1] = (r * Math.PI) / 180 * (geopos_i[1] - geopos_r[1]);

		// the first two dimensions contain the location in any case.
		// there may be more dimensions with context-depending content; copy
		// these
		for (int i = 2; i < dimensions; i++) {
			gridpos[i] = geopos_i[i];
		}

		return gridpos;
	}

	/**
	 * This method calculates the grid coordinates of geopos_i with respect to a
	 * geo-point of origin
	 * 
	 * @param geopos_r
	 *            geo coordinates in degree of a reference point (longitude,
	 *            latitude)
	 * @param geopos_i
	 *            geo coordinates in degree of the point to be converted
	 *            (longitude, latitude)
	 */
	public static void geo2grid_ref(final double[] geopos_r,
			final double[] geopos_i) {
		final double r = earthRadiusEcc(geopos_r[1]);
		geopos_i[0] = Math.cos(Math.toRadians(geopos_r[1])) * (r * Math.PI)
				/ 180 * (geopos_i[0] - geopos_r[0]);
		geopos_i[1] = (r * Math.PI) / 180 * (geopos_i[1] - geopos_r[1]);
	}

	/**
	 * This method calculates the geo coordinates with respect to a geo-point of
	 * origin
	 * 
	 * @param geopos_r
	 *            geo coordinates in degree of a reference point (longitude,
	 *            latitude)
	 * @param gridpos
	 *            grid-coordinates (x, y)
	 * @return the geo coordinates (longitude, latitude) of the converted point
	 */
	public static double[] grid2geo(final double[] geopos_r,
			final double[] gridpos) {
		final int dimensions = gridpos.length;
		final double[] geopos = new double[dimensions];
		final double r = earthRadiusEcc(geopos_r[1]);

		/* These are merely the inverse functions from geo2grid */
		geopos[0] = geopos_r[0] + (gridpos[0] * 180)
				/ (r * Math.PI * Math.cos(Math.toRadians(geopos_r[1])));
		geopos[1] = geopos_r[1] + (gridpos[1] * 180) / (r * Math.PI);
		for (int i = 2; i < dimensions; i++) {
			geopos[i] = gridpos[i];
		}

		return geopos;
	}

	/**
	 * This method calculates the geo coordinates with respect to a geo-point of
	 * origin
	 * 
	 * @param geopos_r
	 *            geo coordinates in degree of a reference point (longitude,
	 *            latitude)
	 * @param gridpos
	 *            grid-coordinates (x, y)
	 */
	public static void grid2geo_ref(final double[] geopos_r,
			final double[] gridpos) {
		final double r = earthRadiusEcc(geopos_r[1]);

		/* These are merely the inverse functions from geo2grid */
		gridpos[0] = geopos_r[0] + (gridpos[0] * 180)
				/ (r * Math.PI * Math.cos(Math.toRadians(geopos_r[1])));
		gridpos[1] = geopos_r[1] + (gridpos[1] * 180) / (r * Math.PI);
	}

	/**
	 * This method calculates the unsigned distance between two geo points
	 * 
	 * @param geopos_a
	 *            First geo point (longitude, latitude)
	 * @param geopos_b
	 *            Second geo point (longitude, latitude)
	 * @return the unsigned distance in meters between geopos_a and geopos_b
	 */
	public static double geoDistance(final double[] geopos_a,
			final double[] geopos_b) {
		final double lat1 = Math.toRadians(geopos_a[1]);
		final double lon1 = Math.toRadians(geopos_a[0]);
		final double lat2 = Math.toRadians(geopos_b[1]);
		final double lon2 = Math.toRadians(geopos_b[0]);

		final double a = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1);
		if ((lat1 == lat2 && lon1 == lon2) || (a >= 1.0)) {
			return 0.0;
		} else {
			return (earthRadius * Math.acos(a) * 1000.0);
		}
	}

	/**
	 * This method calculates the unsigned distance between two grid points
	 * 
	 * @param gridpos_a
	 *            First grid point (longitude, latitude)
	 * @param gridpos_b
	 *            Second grid point (longitude, latitude)
	 * @return the unsigned distance in meters between gridpos_a and gridpos_b
	 */
	public static double gridDistance(final double[] gridpos_a,
			final double[] gridpos_b) {
		return Math.sqrt(Math.pow(gridpos_a[0] - gridpos_b[0], 2)
				+ Math.pow(gridpos_a[1] - gridpos_b[1], 2));
	}

	/**
	 * This method calculates the bearing (the course) between two points given
	 * as lat/long coordinates
	 * 
	 * @param geopos_a
	 *            first geo position (longitude, latitude)
	 * @param geopos_b
	 *            second geo position (longitude, latitude)
	 * @return
	 */
	public static double calcBearing(final double[] geopos_a,
			final double[] geopos_b) {
		final double lat1 = Math.toRadians(geopos_a[1]);
		final double lat2 = Math.toRadians(geopos_b[1]);
		final double dLon = Math.toRadians(geopos_b[0] - geopos_a[0]);

		final double y = Math.sin(dLon) * Math.cos(lat2);
		final double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		final double brng = Math.atan2(y, x);

		return (Math.toDegrees(brng) + 360) % 360;
	}

	/**
	 * This method checks whether the difference between two angles does not
	 * exceed a maximal difference
	 * 
	 * @author Markus Kerper
	 * 
	 * @param first
	 *            first angle
	 * @param second
	 *            second angle
	 * @param maxDiff
	 *            maximal difference
	 * @return true, if the difference between the two angles does not exceed a
	 *         maximal difference. false, otherwise.
	 */
	public static boolean checkDirections(final double first,
			final double second, final double maxDiff) {

		final double v1 = Math.toRadians(first);
		final double v2 = Math.toRadians(second);
		final double cos = Math.cos(v1) * Math.cos(v2);
		final double sin = Math.sin(v1) * Math.sin(v2);
		final double result = Math.toDegrees(Math.acos(cos + sin));
		return result <= maxDiff + 0.0001;
	}

	/**
	 * This method translates a point in geodetic format into a cartesian
	 * description with respect to a geodetic reference position
	 * 
	 * @param trajectory
	 * @return
	 */
	private static ArrayList<Vertex> projectToCartesian(
			final ArrayList<Vertex> trajectory) {
		final ArrayList<Vertex> cartesianTrajectory = new ArrayList<Vertex>();

		if (trajectory.isEmpty()) {
			return cartesianTrajectory;
		}

		final double reference[] = trajectory.get(0).getCoordinates();
		for (final Vertex v : trajectory) {
			final double orig_coordinates[] = v.getCoordinates();
			final double coordinates[] = Geo.geo2grid(reference,
					orig_coordinates);
			cartesianTrajectory.add(new Vertex(v.getId(), coordinates));
		}

		return cartesianTrajectory;
	}

}
