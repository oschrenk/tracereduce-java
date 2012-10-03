package de.q2web.util.timer;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * The Class TimedWorkUnit.
 * 
 * @param <I>
 *            input type
 * @param <O>
 *            output type
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public abstract class TimedWorkUnit<I, O> implements WorkUnit<I, O> {

	/** The stopwatch. */
	Stopwatch stopwatch = new Stopwatch();

	/**
	 * Work.
	 * 
	 * @param input
	 *            the input
	 * @return the o
	 * @throws WorkUnitException
	 *             the work unit exception
	 */
	protected abstract O work(final I input) throws WorkUnitException;

	/*
	 * @see de.q2web.util.timer.WorkUnit#run(java.lang.Object)
	 */
	@Override
	public O run(final I input) throws WorkUnitException {
		stopwatch.start();
		final O output = work(input);
		stopwatch.stop();
		return output;
	}

	/**
	 * Gets the elapsed nanoseconds.
	 * 
	 * @return the elapsed nanoseconds
	 */
	public long getElapsedNanos() {
		return stopwatch.elapsedTime(TimeUnit.NANOSECONDS);
	}

	/**
	 * Gets the elapsed microseconds.
	 * 
	 * @return the elapsed microseconds
	 */
	public long getElapsedMicros() {
		return stopwatch.elapsedTime(TimeUnit.MICROSECONDS);
	}

	/**
	 * Gets the elapsed milliseconds.
	 * 
	 * @return the elapsed milliseconds
	 */
	public long getElapsedMillis() {
		return stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
	}

}
