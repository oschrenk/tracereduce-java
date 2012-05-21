package de.q2web.gis.algorithms.core;

import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Type;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Variant;

/**
 * A factory for creating AlgorithmTemplate objects.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmTemplates {

	/**
	 * Builds the.
	 * 
	 * @param value
	 *            the value
	 * @return the algorithm template
	 */
	public static AlgorithmTemplate build(final String value) {
		final String trimmedValue = value.trim();
		final String[] splitValue = trimmedValue.split("#");

		final String alorithmValue = splitValue[0];
		final String variantValue = splitValue.length == 1 ? "default"
				: splitValue[1];

		final Type type = AlgorithmTemplate.Type.get(alorithmValue);
		final Variant variant = AlgorithmTemplate.Variant.get(variantValue);

		return new AlgorithmTemplate(type, variant);
	}

}
