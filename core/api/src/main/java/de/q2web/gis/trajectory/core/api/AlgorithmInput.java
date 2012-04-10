package de.q2web.gis.trajectory.core.api;

import java.util.List;

/**
 * The Class AlgorithmInput.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmInput {

	/** The trace. */
	@SuppressWarnings("rawtypes")
	private final List<Point> trace;

	/** The epsilon. */
	private final Number epsilon;

	/**
	 * Instantiates a new algorithm input.
	 * 
	 * @param trace
	 *            the trace
	 * @param epsilon
	 *            the epsilon
	 */
	@SuppressWarnings("rawtypes")
	public AlgorithmInput(final List<Point> trace, final Number epsilon) {
		super();
		this.trace = trace;
		this.epsilon = epsilon;
	}

	/**
	 * Gets the trace.
	 * 
	 * @return the trace
	 * @category getter
	 */
	@SuppressWarnings("rawtypes")
	public List<Point> getTrace() {
		return trace;
	}

	/**
	 * Gets the epsilon.
	 * 
	 * @return the epsilon
	 * @category getter
	 */
	public Number getEpsilon() {
		return epsilon;
	}

}
