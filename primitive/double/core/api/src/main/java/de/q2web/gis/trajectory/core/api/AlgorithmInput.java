package de.q2web.gis.trajectory.core.api;

import java.util.List;

/**
 * The Class AlgorithmInput.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmInput {

	/** The trace. */
	private final List<Point> trace;

	/** The epsilon. */
	private final double epsilon;

	/**
	 * Instantiates a new algorithm input.
	 * 
	 * @param trace
	 *            the trace
	 * @param epsilon
	 *            the epsilon
	 */
	public AlgorithmInput(final List<Point> trace, final double epsilon) {
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
	public List<Point> getTrace() {
		return trace;
	}

	/**
	 * Gets the epsilon.
	 * 
	 * @return the epsilon
	 * @category getter
	 */
	public double getEpsilon() {
		return epsilon;
	}

}
