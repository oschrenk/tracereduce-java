package de.q2web.gis.trajectory.core.api;

import java.util.List;

/**
 * The Interface Algorithm.
 * 
 * @param <P>
 *            the generic type
 */
public interface Algorithm<P extends Number> {

	/**
	 * Run.
	 * 
	 * @param points
	 *            the points
	 * @param epsilon
	 *            the epsilon
	 * @return the list
	 */
	List<Point<P>> run(List<Point<P>> points, P epsilon);

}
