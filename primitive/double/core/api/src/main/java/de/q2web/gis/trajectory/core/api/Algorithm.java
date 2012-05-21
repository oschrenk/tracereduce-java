package de.q2web.gis.trajectory.core.api;

import java.util.List;

/**
 * The Interface Algorithm.
 * 
 */
public interface Algorithm {

	/**
	 * Run.
	 * 
	 * @param points
	 *            the points
	 * @param epsilon
	 *            the epsilon
	 * @return the list
	 */
	List<Point> run(List<Point> points, double epsilon);

}
