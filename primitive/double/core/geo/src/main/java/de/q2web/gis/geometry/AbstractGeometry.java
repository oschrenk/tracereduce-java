package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Geometry;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public abstract class AbstractGeometry implements Geometry {

	/*
	 * @see de.q2web.gis.trajectory.core.api.Geometry#compare(double, double)
	 */
	@Override
	public int compare(final double a, final double b) {
		if (a < b) {
			return -1; // Neither val is NaN, thisVal is smaller
		}
		if (a > b) {
			return 1; // Neither val is NaN, thisVal is larger
		}

		// Cannot use doubleToRawLongBits because of possibility of NaNs.
		final long thisBits = Double.doubleToLongBits(a);
		final long anotherBits = Double.doubleToLongBits(b);

		return (thisBits == anotherBits ? 0 : // Values are equal
				(thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
						1)); // (0.0, -0.0) or (NaN, !NaN)
	}

}
