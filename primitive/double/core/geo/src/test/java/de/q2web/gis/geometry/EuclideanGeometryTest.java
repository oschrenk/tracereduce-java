package de.q2web.gis.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EuclideanGeometryTest {

	private Geometry geometry;

	@Before
	public void setUp() {
		geometry = new EuclideanGeometry();
	}

	@Test
	public void distanceOfPoints() {
		final Point from = new DoublePoint(new double[] { 0, 0 });
		final Point to = new DoublePoint(new double[] { 3, 4 });
		final double distance = geometry.distance(from, to);

		assertEquals(5d, distance, 0.0);
	}

	@Test
	public void distanceOfPointToLine3d() {
		final Point point = new DoublePoint(new double[] { 10, 5, 7 });
		final Point lineStart = new DoublePoint(new double[] { -2, 1, 7 });
		final Point lineEnd = new DoublePoint(new double[] { 2, 2, 4 });
		final double distance = EuclideanGeometry.distance3d(point, lineStart,
				lineEnd);

		assertEquals(Math.sqrt(56), distance, 0.0);
	}

	/**
	 * Distance of point to line2d.
	 */
	@Test
	public void distanceOfPointToLine2d() {
		final Point point = new DoublePoint(new double[] { 2, 0 });
		final Point lineStart = new DoublePoint(new double[] { 0, 0 });
		final Point lineEnd = new DoublePoint(new double[] { 2, 2 });
		final double distance = EuclideanGeometry.distance2d(point, lineStart,
				lineEnd);

		assertEquals(Math.sqrt(2), distance, 0.00000001);
	}

	@Test
	public void compare() {
		final double a = 10;
		final double b = 0;

		assertEquals(true, geometry.compare(a, b) > 0);
	}

	public static double earthRadius = 6378.137; /* Earth radius WGS84 */
	public static double eccentricity = 0.081819190842622;

	@Test
	public void testGeo2Grid() {
		double[] geopos_r = new double[] { 50, 50 };
		double[] geopos_i = new double[] { 50.0001, 50 };
		double[] geo2grid = geo2grid(geopos_r, geopos_i);

		for (double d : geo2grid) {
			System.out.println(d);
			System.out.println(Math.sqrt(d));
		}

	}

	public static double[] geo2grid(double[] geopos_r, double[] geopos_i) {
		double[] gridpos = new double[2];
		double r = earthRadiusEcc(geopos_r[1]);
		/*
		 * Differenz auf x-Achse:
		 * 
		 * Umfang des Breitenkreises in Metern u =
		 * Math.cos(deg2rad(geopos_r[1])) * r * 2 * PI
		 * 
		 * Abschnitt zwischen den Längengraden in Metern d = u * (geopos_i[0] -
		 * geopos_r[0])/360 = Math.cos(deg2rad(geopos_r[1])) * r * PI/180 *
		 * (geopos_i[0] - geopos_r[0])
		 */
		// gridpos[0] = deg2rad(r) * Math.cos(deg2rad(geopos_r[1])) *
		// (geopos_i[0] - geopos_r[0]);
		gridpos[0] = Math.cos(deg2rad(geopos_r[1])) * (r * Math.PI) / 180
				* (geopos_i[0] - geopos_r[0]);

		/*
		 * Differenz auf y-Achse:
		 * 
		 * Umfang des Längenkreises in Metern u = r * 2 * PI
		 * 
		 * Abschnitt zwischen den Breitengraden in Metern d = u * (geopos_i[1] -
		 * geopos_r[1])/360 = r * PI/180 * (geopos_i[1] - geopos_r[1])
		 */
		// gridpos[1] = deg2rad(r) * (geopos_i[1] - geopos_r[1]);
		gridpos[1] = (r * Math.PI) / 180 * (geopos_i[1] - geopos_r[1]);
		return gridpos;
	}

	public static double earthRadiusEcc(double lat) {
		/*
		 * the radius of curvature of an ellipsoidal Earth in the plane of the
		 * meridian is given by
		 * 
		 * R' = a * (1 - e^2) / (1 - e^2 * (sin(lat))^2)^(3/2)
		 * 
		 * 2 a * (1 - e ) R' = ----------------------- 2 2 3/2 (1 - e * sin(lat)
		 * )
		 * 
		 * where a is the equatorial radius,
		 * 
		 * b is the polar radius, and e is the eccentricity of the ellipsoid =
		 * sqrt(1 - b^2/a^2)
		 * 
		 * a = 6378.137 km (3963 mi) Equatorial radius (surface to center
		 * distance) b = 6356.752 km (3950 mi) Polar radius (surface to center
		 * distance) e = 0.081819190842622 Eccentricity
		 */
		double e_sq = Math.pow(eccentricity, 2);
		double numerator = earthRadius * (1.0 - e_sq);
		double denominator = Math.pow(
				1.0 - e_sq * Math.pow(Math.sin(deg2rad(lat)), 2), 1.5);
		return 1000 * numerator / denominator; /* result is in meters */
	}

	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

}
