package de.q2web.gis.ui.cli;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.Precision;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateConverter;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateValidator;
import de.q2web.gis.ui.cli.util.EpsilonConverter;
import de.q2web.gis.ui.cli.util.InputValidator;

public class StartupArguments {

	@Parameter(names = { "-i", "--input" }, description = "Path to input file", converter = FileConverter.class, validateWith = InputValidator.class, required = true)
	private File input;

	@Parameter(names = { "-e", "--epsilon" }, description = "Error tolerance level", converter = EpsilonConverter.class, required = true)
	private Number epsilon;

	@Parameter(names = { "-a", "--algorithm" }, description = "Algorithm for minimzing trajectory", validateWith = AlgorithmTemplateValidator.class, converter = AlgorithmTemplateConverter.class, required = true)
	private AlgorithmTemplate algorithm;

	@Parameter(names = { "-d", "--dimensions" }, description = "Number of dimensions")
	private final Integer dimensions = 2;

	@Parameter(names = { "-p", "--precision" }, description = "Precision for algorithm")
	private final Precision precision = Precision.DOUBLE;

	/**
	 * @return the input
	 * @category getter
	 */
	public File getInput() {
		return input;
	}

	/**
	 * @return the epsilon
	 * @category getter
	 */
	public Number getEpsilon() {
		return epsilon;
	}

	/**
	 * @return the algorithm
	 * @category getter
	 */
	public AlgorithmTemplate getAlgorithmTemplate() {
		return algorithm;
	}

	/**
	 * @return the dimensions
	 * @category getter
	 */
	public int getDimensions() {
		return dimensions;
	}

	/**
	 * @return the precision
	 * @category getter
	 */
	public Precision getPrecision() {
		return precision;
	}

}
