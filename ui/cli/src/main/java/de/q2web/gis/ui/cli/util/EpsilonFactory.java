package de.q2web.gis.ui.cli.util;

/**
 * A factory for creating Epsilon objects.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EpsilonFactory {

	/**
	 * Builds the.
	 * 
	 * @param epsilon
	 *            the epsilon
	 * @return the number
	 */
	public static double build(final Number epsilon) {
		return epsilon.doubleValue();
	}

}
