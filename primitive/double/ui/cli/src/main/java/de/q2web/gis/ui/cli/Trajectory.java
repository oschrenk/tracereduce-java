package de.q2web.gis.ui.cli;

import java.io.File;
import java.util.List;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.jobs.AlgorithmTimedWorkUnit;
import de.q2web.gis.jobs.TraceReaderTimedWorkUnit;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmInput;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.gis.ui.cli.util.Duration;
import de.q2web.util.timer.TimedWorkUnit;
import de.q2web.util.timer.WorkUnitException;

/**
 * The Class Trajectory.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class Trajectory {

	/** The trace reader. */
	private final TraceReader traceReader;

	/** The algorithm. */
	private final Algorithm algorithm;

	/**
	 * Instantiates a new trajectory.
	 * 
	 * @param traceReader
	 *            the trace reader
	 * @param algorithm
	 *            the algorithm
	 */
	public Trajectory(final TraceReader traceReader, final Algorithm algorithm) {
		this.traceReader = traceReader;
		this.algorithm = algorithm;
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

		final TimedWorkUnit<File, List<Point>> traceReaderWorkUnit = new TraceReaderTimedWorkUnit(
				traceReader);
		final List<Point> trace = traceReaderWorkUnit.run(input);
		final long traceReaderDuration = traceReaderWorkUnit.getElapsedNanos();

		final TimedWorkUnit<AlgorithmInput, List<Point>> algorithmWorkUnit = new AlgorithmTimedWorkUnit(
				algorithm);
		final List<Point> trajectory = algorithmWorkUnit
				.run(new AlgorithmInput(trace, epsilon));
		final long algorithmDuration = algorithmWorkUnit.getElapsedNanos();

		System.out.println(String.format("Input read in %s",
				Duration.of(traceReaderDuration)));

		System.out.println(String.format("Algorithm took %s",
				Duration.of(algorithmDuration)));

		System.out.println(String.format("Resulting trace: %s", trajectory));

	}
}
