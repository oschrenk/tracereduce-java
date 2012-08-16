package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IStringConverter;

import de.q2web.gis.algorithms.core.AlgorithmTemplates;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;

/**
 * Accepts the following values
 * 
 * - `dp` or `douglas-peucker`. Creates default Douglas Peucker Algorithm - `la`
 * or `linear-approximation`. Creates default Line Approximation Algorithm -
 * `cs` or `cubic-splines`. Creates default Cubic Splines Algorithm
 * 
 * You can supply a variant by appending `#variant`. Examples
 * 
 * - `la#jgrapht`
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmTemplateConverter implements
		IStringConverter<AlgorithmTemplate> {
	@Override
	public AlgorithmTemplate convert(final String value) {
		return AlgorithmTemplates.build(value);
	}
}
