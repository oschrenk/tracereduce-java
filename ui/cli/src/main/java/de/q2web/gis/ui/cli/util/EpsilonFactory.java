package de.q2web.gis.ui.cli.util;

import de.q2web.gis.trajectory.core.api.Precision;

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
	 * @param precision
	 *            the precision
	 * @return the number
	 */
	public static Number build(final Number epsilon, final Precision precision) {

		switch (precision) {
		case DOUBLE:
			return epsilon.doubleValue();

		case FLOAT:
			return epsilon.floatValue();
		}

		// can never happen
		return null;
	}

}
