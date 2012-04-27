package de.q2web.gis.alg.dp;

import java.util.ArrayList;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class DouglasPeuckerAlgorithm.
 * 
 * @param <P>
 *            the generic type
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class ReferenceDouglasPeucker<P extends Number> implements Algorithm<P> {

	/** The epsilon. */
	private P epsilon;

	/** The geometry. */
	private final Geometry<P> geometry;

	/**
	 * Instantiates a new douglas peucker algorithm.
	 * 
	 * @param geometry
	 *            the geometry
	 */
	public ReferenceDouglasPeucker(final Geometry<P> geometry) {
		super();
		this.geometry = geometry;
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * java.lang.Number)
	 */
	@Override
	public List<Point<P>> run(final List<Point<P>> trace, final P epsilon) {
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
	public List<Point<P>> run(final List<Point<P>> trace) {
		final int traceLength = trace.size();

		// determine the node at which the trace will be seperated
		P maximumDistance = null;
		int maxIndex = -1;

		for (int i = 1; i < traceLength - 1; i++) {
			final P distance = geometry.distance(trace.get(i), trace.get(0),
					trace.get(traceLength - 1));
			if (geometry.compare(distance, maximumDistance) > 0) {
				maximumDistance = distance;
				maxIndex = i;
			}
		}

		final List<Point<P>> resultList;

		// if no seperation needed, just return start and end
		if (maxIndex == -1 || geometry.compare(maximumDistance, epsilon) <= 0) {
			resultList = new ArrayList<Point<P>>(2);
			resultList.add(trace.get(0));
			resultList.add(trace.get(traceLength - 1));
		} else {
			// otherwise continue:
			// minimize lower part
			final List<Point<P>> lowerTrace = new ArrayList<Point<P>>(maxIndex);
			for (int i = 0; i <= maxIndex; i++) {
				lowerTrace.add(trace.get(i));
			}
			final List<Point<P>> lowerTraceResults = run(lowerTrace);
			// remove last element, cause this is equal to the first element in
			// the upper part result list
			lowerTraceResults.remove(lowerTraceResults.size() - 1);

			// minimize upper part
			final List<Point<P>> upperTrace = new ArrayList<Point<P>>(
					traceLength - maxIndex);
			for (int i = maxIndex; i < traceLength; i++) {
				upperTrace.add(trace.get(i));
			}
			final List<Point<P>> upperTraceResults = run(upperTrace);

			resultList = new ArrayList<Point<P>>(lowerTraceResults.size()
					+ upperTraceResults.size());

			// assemble the result
			resultList.addAll(lowerTraceResults);
			resultList.addAll(upperTraceResults);
		}

		return resultList;

	}
}
