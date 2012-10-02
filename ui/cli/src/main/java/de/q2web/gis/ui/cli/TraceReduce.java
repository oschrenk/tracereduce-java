package de.q2web.gis.ui.cli;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmInput;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.gis.ui.cli.jobs.AlgorithmTimedWorkUnit;
import de.q2web.gis.ui.cli.jobs.TraceReaderTimedWorkUnit;
import de.q2web.gis.ui.cli.jobs.TraceWriterTimedWorkUnit;
import de.q2web.gis.ui.cli.util.Duration;
import de.q2web.util.timer.TimedWorkUnit;
import de.q2web.util.timer.WorkUnitException;

/**
 * The Class Trajectory.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceReduce {

	private static Logger LOGGER = LoggerFactory.getLogger(TraceReduce.class);

	/** The trace reader. */
	private final TraceReader traceReader;

	/** The algorithm. */
	private final Algorithm algorithm;

	/** The is timed. */
	private final boolean isTimed;

	/** The trace writer. */
	private final TraceWriter traceWriter;

	/**
	 * Instantiates a new trajectory.
	 * 
	 * @param traceReader
	 *            the trace reader
	 * @param algorithm
	 *            the algorithm
	 * @param isTimed
	 *            the is timed
	 * @param traceWriter
	 *            the trace writer
	 */
	public TraceReduce(final TraceReader traceReader,
			final Algorithm algorithm, final boolean isTimed,
			final TraceWriter traceWriter) {
		this.traceReader = traceReader;
		this.algorithm = algorithm;
		this.isTimed = isTimed;
		this.traceWriter = traceWriter;
	}

	/**
	 * Run.
	 * 
	 * @param input
	 *            the input
	 * @param epsilon
	 *            the epsilon
	 * @throws WorkUnitException
	 *             the work unit exception
	 */
	public void run(final File input, final double epsilon)
			throws WorkUnitException {

		// read the file
		final TimedWorkUnit<File, List<Point>> traceReaderWorkUnit = new TraceReaderTimedWorkUnit(
				traceReader);
		final List<Point> trace = traceReaderWorkUnit.run(input);
		final long traceReaderDuration = traceReaderWorkUnit.getElapsedNanos();

		// do the logic
		final TimedWorkUnit<AlgorithmInput, List<Point>> algorithmWorkUnit = new AlgorithmTimedWorkUnit(
				algorithm);
		final List<Point> trajectory = algorithmWorkUnit
				.run(new AlgorithmInput(trace, epsilon));
		final long algorithmDuration = algorithmWorkUnit.getElapsedNanos();

		// write output
		final TimedWorkUnit<List<Point>, Void> traceWriterWorkUnit = new TraceWriterTimedWorkUnit(
				traceWriter);
		traceWriterWorkUnit.run(trajectory);
		final long traceWriterDuration = traceWriterWorkUnit.getElapsedNanos();

		if (isTimed) {
			System.out.println(String.format("%s with %s points",
					input.getAbsolutePath(), trace.size()));
			System.out.println(String.format("Input read in %s",
					Duration.of(traceReaderDuration)));

			System.out.println(String.format("Algorithm took %s",
					Duration.of(algorithmDuration)));

			LOGGER.info(Long.toString(algorithmDuration));

			System.out.println(String.format("Output written in %s",
					Duration.of(traceWriterDuration)));
		}

	}
}
