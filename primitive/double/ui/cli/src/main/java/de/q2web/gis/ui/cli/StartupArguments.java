package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateConverter;
import de.q2web.gis.ui.cli.util.AlgorithmTemplateValidator;
import de.q2web.gis.ui.cli.util.EpsilonConverter;
import de.q2web.gis.ui.cli.util.InputValidator;
import de.q2web.gis.ui.cli.util.TraceWriterConverter;

public class StartupArguments {

	@Parameter(names = { "-i", "--input" }, description = "Path to input file", converter = FileConverter.class, validateWith = InputValidator.class, required = true)
	private File input;

	@Parameter(names = { "-e", "--epsilon" }, description = "Error tolerance level", converter = EpsilonConverter.class, required = true)
	private double epsilon;

	@Parameter(names = { "-o", "--output-file" }, description = "Path to optional output file", converter = TraceWriterConverter.class)
	private final Writer outputWriter = new OutputStreamWriter(System.out);

	@Parameter(names = { "-t", "--timed" }, description = "Print timings flag")
	private final boolean timed = false;

	@Parameter(names = { "-a", "--algorithm" }, description = "Algorithm for minimizing trajectory", validateWith = AlgorithmTemplateValidator.class, converter = AlgorithmTemplateConverter.class, required = true)
	private AlgorithmTemplate algorithm;

	@Parameter(names = { "-d", "--dimensions" }, description = "Number of dimensions")
	private final Integer dimensions = 2;

	/**
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	/**
	 * @return the epsilon
	 */
	public Number getEpsilon() {
		return epsilon;
	}

	/**
	 * @return the algorithm
	 */
	public AlgorithmTemplate getAlgorithmTemplate() {
		return algorithm;
	}

	/**
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
