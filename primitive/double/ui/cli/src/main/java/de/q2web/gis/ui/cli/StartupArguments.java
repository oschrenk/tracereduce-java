package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import de.q2web.gis.algorithms.core.AlgorithmTemplates;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateConverter;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateValidator;
import de.q2web.gis.ui.cli.util.EpsilonConverter;
import de.q2web.gis.ui.cli.util.InputValidator;
import de.q2web.gis.ui.cli.util.TraceWriterConverter;

// TODO: Auto-generated Javadoc
/**
 * The Class StartupArguments.
 */
public class StartupArguments {

	/** The Constant DOUGLAS_PEUCKER. */
	private static final String DOUGLAS_PEUCKER = "dp";

	/** The Constant DEFAULT_ALGORITHM. */
	private static final String DEFAULT_ALGORITHM = DOUGLAS_PEUCKER;
	// TODO epxplain unit
	/** The Constant DEFAULT_EPSILON. */
	private static final double DEFAULT_EPSILON = 10;

	/** The Constant DEFAULT_DIMENSIONS. */
	private static final int DEFAULT_DIMENSIONS = 2;

	/** The DEFAUL t_ timed. */
	private static boolean DEFAULT_TIMED = true;

	/** The DEFAUL t_ outpu t_ writer. */
	private static Writer DEFAULT_OUTPUT_WRITER = new OutputStreamWriter(
			System.out);

	/** The input. */
	@Parameter(names = { "-i", "--input" }, description = "Path to input file", converter = FileConverter.class, validateWith = InputValidator.class, required = true)
	private File input;

	/** The epsilon. */
	@Parameter(names = { "-e", "--epsilon" }, description = "Error tolerance level", converter = EpsilonConverter.class)
	private final double epsilon = DEFAULT_EPSILON;

	/** The output writer. */
	@Parameter(names = { "-o", "--output-file" }, description = "Path to optional output file", converter = TraceWriterConverter.class)
	private final Writer outputWriter = DEFAULT_OUTPUT_WRITER;

	/** The timed. */
	@Parameter(names = { "-t", "--timed" }, description = "Print timings flag")
	private final boolean timed = DEFAULT_TIMED;

	/** The algorithm. */
	@Parameter(names = { "-a", "--algorithm" }, description = "Algorithm for minimizing trajectory", validateValueWith = AlgorithmTemplateValidator.class, converter = AlgorithmTemplateConverter.class)
	private final AlgorithmTemplate algorithm = AlgorithmTemplates
			.build(DEFAULT_ALGORITHM);

	/** The dimensions. */
	@Parameter(names = { "-d", "--dimensions" }, description = "Number of dimensions")
	private final Integer dimensions = DEFAULT_DIMENSIONS;

	/**
	 * Gets the input.
	 * 
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	/**
	 * Gets the epsilon.
	 * 
	 * @return the epsilon
	 */
	public Number getEpsilon() {
		return epsilon;
	}

	/**
	 * Gets the algorithm template.
	 * 
	 * @return the algorithm
	 */
	public AlgorithmTemplate getAlgorithmTemplate() {
		return algorithm;
	}

	/**
	 * Gets the dimensions.
	 * 
	 * @return the dimensions
	 */
	public int getDimensions() {
		return dimensions;
	}

	/**
	 * Checks if is timed.
	 * 
	 * @return true, if is timed
	 */
	public boolean isTimed() {
		return timed;
	}

	/**
	 * Gets the output.
	 * 
	 * @return the output
	 */
	public Writer getWriter() {
		return outputWriter;
	}
}
