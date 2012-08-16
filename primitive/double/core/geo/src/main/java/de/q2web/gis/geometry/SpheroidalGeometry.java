package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SpheroidalGeometry extends AbstractGeometry {

	private final double semiMinorAxis;
	private final double semiMajorAxis;

	/**
	 * @param semiMajorAxis
	 * @param semiMinorAxis
	 * 
	 */
	public SpheroidalGeometry(final double semiMajorAxis,
			final double semiMinorAxis) {
		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMinorAxis;
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public double distance(final Point from, final Point to) {
		// TODO Geometry.distance()
		return 0;
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public double distance(final Point point, final Point lineStart,
			final Point lineEnd) {
		return distance2d(point, lineStart, lineEnd);
	}

	/**
	 * lat lng height.
	 * 
	 * @param p
	 *            the p
	 * @return the point
	 */
	private static final Point toCartesian3d(final Point p) {
		final double[] newP = new double[3];
		newP[0] = Math.cos(Math.toRadians(p.get(0)))
				* Math.cos(Math.toRadians(p.get(1)));
		newP[1] = Math.cos(Math.toRadians(p.get(0)))
				* Math.sin(Math.toRadians(p.get(1)));
		newP[2] = Math.sin(Math.toRadians(p.get(0)));
		return new DoublePoint(newP);
	}

	private final double distance2d(final Point point, final Point lineStart,
			final Point lineEnd) {
		final Point aPrime = SpheroidalGeometry.toCartesian3d(lineStart);
		final Point bPrime = SpheroidalGeometry.toCartesian3d(lineEnd);
		final Point pPrime = SpheroidalGeometry.toCartesian3d(point);
		final Point n = Vector.cross(aPrime, bPrime);

		final double sinPhi = Math.abs(Vector.dot(n, pPrime));
		final double phi = Math.asin(sinPhi);

		// TODO doing weird science here; I don't believe length of radius can
		// be applied here as in spherical geometry
		final double height = point.getDimensions() == 3 ? point.get(2) : 0;
		final double sinLat = Math.sin(Math.toRadians(point.get(0)));
		final double sinSquaredLat = sinLat * sinLat;
		final double eccentricitySquared = eccentricitySquared(semiMajorAxis,
				semiMinorAxis);
		final double normalDistance = semiMajorAxis
				/ (Math.sqrt(1 - eccentricitySquared * sinSquaredLat));
		final double[] newP = new double[3];
		newP[0] = (normalDistance + height);
		newP[1] = (normalDistance + height);
		newP[2] = (normalDistance * (1 - eccentricitySquared) + height);
		final Point radius = new DoublePoint(newP);

		return Vector.length(radius) * phi;
	}

	public static double eccentricitySquared(final double semiMajorAxis,
			final double semiMinorAxis) {
		return 1 - semiMinorAxis * semiMinorAxis / semiMajorAxis
				/ semiMajorAxis;
	}

}
