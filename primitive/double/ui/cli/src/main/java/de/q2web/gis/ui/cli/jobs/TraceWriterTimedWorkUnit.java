package de.q2web.gis.ui.cli.jobs;

import java.io.IOException;
import java.util.List;

import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.util.timer.TimedWorkUnit;
import de.q2web.util.timer.WorkUnitException;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceWriterTimedWorkUnit extends TimedWorkUnit<List<Point>, Void> {

	/** The trace writer. */
	private final TraceWriter traceWriter;

	/**
	 * Instantiates a new trace writer timed work unit.
	 * 
	 * @param traceWriter
	 *            the trace writer
	 */
	public TraceWriterTimedWorkUnit(final TraceWriter traceWriter) {
		this.traceWriter = traceWriter;
	}

	/*
	 * @see de.q2web.util.timer.TimedWorkUnit#work(java.lang.Object)
	 */
	@Override
	protected Void work(final List<Point> points) throws WorkUnitException {
		try {
			traceWriter.write(points);
		} catch (final IOException e) {
			throw new WorkUnitException(e);
		}

		return (null);
	}

}
