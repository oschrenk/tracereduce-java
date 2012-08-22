package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class SphericalGeometry.
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class SphericalGeometry extends AbstractGeometry {

	/** The Constant EARTH_VOLUMETRIC_MEAN_RADIUS. */
	public static final int EARTH_VOLUMETRIC_MEAN_RADIUS = 6371000;

	/** The radius. */
	private final double radius;

	/**
	 * Instantiates a new spherical geometry with EARTH_VOLUMETRIC_MEAN_RADIUS
	 */
	public SphericalGeometry() {
		this(EARTH_VOLUMETRIC_MEAN_RADIUS);
	}

	/**
	 * Instantiates a new spherical geometry.
	 *
	 * @param radius
	 *            the radius
	 */
	public SphericalGeometry(final double radius) {
		this.radius = radius;
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point)
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
		return getDistance2d(radius, from.get(0), from.get(1), to.get(0),
				to.get(1));
	}

	/**
	 * Gets the distance2d.
	 *
	 * @param radius
	 *            the radius
	 * @param latitudeFrom
	 *            the latitude from
	 * @param longitudeFrom
	 *            the longitude from
	 * @param latitudeTo
	 *            the latitude to
	 * @param longitudeTo
	 *            the longitude to
	 * @return the distance2d
	 */
	protected static final double getDistance2d(final double radius,
			final double latitudeFrom, final double longitudeFrom,
			final double latitudeTo, final double longitudeTo) {
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

		return radius * c;
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public double distance(final Point point, final Point lineStart,
			final Point lineEnd) {
		return distance2d(radius, point, lineStart, lineEnd);
	}

	/**
	 * Calculates the distance from the point X to the geodesic line AB
	 *
	 * Using longitude ($\theta$) and latitude ($\phi$), let $A=(\theta_A,
	 * \phi_A)$, $B=(\theta_B, \phi_B)$, and $X=(\theta_X, \phi_X)$. The
	 * direction vectors for these points are $$\hat A = (\cos \phi_A \cos
	 * \theta_A, \cos \phi_A \sin \theta_A, \sin \phi_A),$$ $$ \hat B = (\cos
	 * \phi_B \cos \theta_B, \cos \phi_B \sin \theta_B, \sin \phi_B), $$ $$\hat
	 * X = (\cos \phi_X \cos \theta_X, \cos \phi_X \sin \theta_X, \sin
	 * \phi_X).$$
	 *
	 * Let $\Phi$ be the distance on the unit sphere between $\hat X$ and the
	 * geodesic line passing through $\hat A$ and $\hat B$. Imagine the plane
	 * $\mathcal{P}$ passing through $\hat A$, $\hat B$, and the origin, which
	 * cuts the unit sphere in half. Then the Euclidean distance of $\hat X$
	 * from plane $\mathcal{P}$ is $\sin \Phi$. Now let $\hat n$ be a unit
	 * normal vector for $\mathcal{P}$, and we have
	 *
	 * $$\hat n = \hat A \times \hat B$$ $$\sin \Phi = | \hat n \cdot \hat X |$$
	 *
	 * So, if the radius of the original sphere is $R$, then the surface
	 * distance from the point $X$ to the geodesic line
	 * $\overleftrightarrow{AB}$ is $R \Phi$.
	 *
	 * @param radius
	 *            the radius
	 * @param point
	 *            the point
	 * @param lineStart
	 *            the line start
	 * @param lineEnd
	 *            the line end
	 * @return the double
	 * @see <a href="http://math.stackexchange.com/posts/23612/">How to find the
	 *      distance between a point and line joining two points on a
	 *      sphere?</a>
	 */
	private static final double distance2d(final double radius,
			final Point point, final Point lineStart, final Point lineEnd) {
		final Point aPrime = SphericalGeometry.toCartesian3d(lineStart);
		final Point bPrime = SphericalGeometry.toCartesian3d(lineEnd);
		final Point pPrime = SphericalGeometry.toCartesian3d(point);
		final Point n = Vector.cross(aPrime, bPrime);
		final double sinPhi = Math.abs(Vector.dot(n, pPrime));
		final double phi = Math.asin(sinPhi);
		return radius * phi;
	}

	/**
	 * * Lat-Lng[-Height] in spherical coordinates.
	 *
	 * @param p
	 *            the p
	 * @return the point
	 */

	private static final Point toCartesian3d(final Point p) {
		final double[] c = {
				Math.cos(Math.toRadians(p.get(0)))
						* Math.cos(Math.toRadians(p.get(1))),
				Math.cos(Math.toRadians(p.get(0)))
						* Math.sin(Math.toRadians(p.get(1))),
				Math.sin(Math.toRadians(p.get(0))) };
		return new Point(p.getTime(), c);
	}

}
