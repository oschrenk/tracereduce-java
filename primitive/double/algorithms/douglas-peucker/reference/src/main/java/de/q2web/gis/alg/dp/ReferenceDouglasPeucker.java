package de.q2web.gis.alg.dp;

import java.util.ArrayList;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class DouglasPeuckerAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class ReferenceDouglasPeucker implements Algorithm {

	/** The epsilon. */
	private double epsilon;

	/** The geometry. */
	private final Geometry geometry;

	/**
	 * Instantiates a new douglas peucker algorithm.
	 * 
	 * @param geometry
	 *            the geometry
	 */
	public ReferenceDouglasPeucker(final Geometry geometry) {
		super();
		this.geometry = geometry;
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {
		this.epsilon = epsilon;
		return run(trace);
	}

	/**
	 * Run.
	 * 
	 * @param trace
	 *            the trace
	 * @return the list
	 */
	public List<Point> run(final List<Point> trace) {
		final int traceLength = trace.size();

		// determine the node at which the trace will be seperated
		double maximumDistance = Double.NaN;
		int maxIndex = -1;

		for (int i = 1; i < traceLength - 1; i++) {
			final double distance = geometry.distance(trace.get(i),
					trace.get(0), trace.get(traceLength - 1));
			if (geometry.compare(distance, maximumDistance) > 0) {
				maximumDistance = distance;
				maxIndex = i;
			}
		}

		final List<Point> resultList;

		// if no seperation needed, just return start and end
		if (maxIndex == -1 || geometry.compare(maximumDistance, epsilon) <= 0) {
			resultList = new ArrayList<Point>(2);
			resultList.add(trace.get(0));
			resultList.add(trace.get(traceLength - 1));
		} else {
			// otherwise continue:
			// minimize lower part
			final List<Point> lowerTrace = new ArrayList<Point>(maxIndex);
			for (int i = 0; i <= maxIndex; i++) {
				lowerTrace.add(trace.get(i));
			}
			final List<Point> lowerTraceResults = run(lowerTrace);
			// remove last element, cause this is equal to the first element in
			// the upper part result list
			lowerTraceResults.remove(lowerTraceResults.size() - 1);

			// minimize upper part
			final List<Point> upperTrace = new ArrayList<Point>(traceLength
					- maxIndex);
			for (int i = maxIndex; i < traceLength; i++) {
				upperTrace.add(trace.get(i));
			}
			final List<Point> upperTraceResults = run(upperTrace);

			resultList = new ArrayList<Point>(lowerTraceResults.size()
					+ upperTraceResults.size());

			// assemble the result
			resultList.addAll(lowerTraceResults);
			resultList.addAll(upperTraceResults);
		}

		return resultList;

	}
}
