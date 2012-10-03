package de.q2web.gis.core.api;

import java.util.List;

/**
 * The Interface Algorithm.
 *
 */
public interface Algorithm {

	/**
	 * Run.
	 *
	 * @param trace
	 *            the trace
	 * @param epsilon
	 *            the epsilon
	 * @return the list
	 */
	List<Point> run(List<Point> trace, double epsilon);

}
