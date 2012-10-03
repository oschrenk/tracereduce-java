package de.q2web.gis.alg.dp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Distance;
import de.q2web.gis.core.api.Point;

/**
 * The Class DouglasPeuckerAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DouglasPeuckerReferenceAlgorithm implements Algorithm {

	private static Logger LOGGER = LoggerFactory
			.getLogger(DouglasPeuckerReferenceAlgorithm.class);

	/** The epsilon. */
	private double epsilon;

	/** The distance. */
	private final Distance distance;

	/**
	 * Instantiates a new douglas peucker algorithm.
	 * 
	 * @param distance
	 *            the distance
	 */
	public DouglasPeuckerReferenceAlgorithm(final Distance distance) {
		super();
		this.distance = distance;
	}

	/*
	 * @see de.q2web.gis.core.api.Algorithm#run(java.util.List, double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {
		this.epsilon = epsilon;

		final List<Point> simplified = run(trace, 0);

		LOGGER.trace("Trace: {}", simplified);

		return simplified;
	}

	/**
	 * Run.
	 * 
	 * @param trace
	 *            the trace
	 * @return the list
	 */
	public List<Point> run(final List<Point> trace, final int depth) {
		final int traceLength = trace.size();

		// determine the node at which the trace will be separated
		double maximumDistance = Double.NEGATIVE_INFINITY;
		int maxIndex = -1;

		Point first = trace.get(0);
		Point last = trace.get(traceLength - 1);

		for (int i = 1; i < traceLength - 1; i++) {
			final double d = distance.distance(trace.get(i), first, last);
			if (d > maximumDistance) {
				maximumDistance = d;
				maxIndex = i;
			}
		}

		final List<Point> resultList;

		// if no separation needed, just return start and end
		if (maxIndex == -1 || maximumDistance <= epsilon) {
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
			final List<Point> lowerTraceResults = run(lowerTrace, depth + 1);
			// remove last element, cause this is equal to the first element in
			// the upper part result list
			lowerTraceResults.remove(lowerTraceResults.size() - 1);

			// minimize upper part
			final List<Point> upperTrace = new ArrayList<Point>(traceLength
					- maxIndex);
			for (int i = maxIndex; i < traceLength; i++) {
				upperTrace.add(trace.get(i));
			}
			final List<Point> upperTraceResults = run(upperTrace, depth + 1);

			resultList = new ArrayList<Point>(lowerTraceResults.size()
					+ upperTraceResults.size());

			// assemble the result
			resultList.addAll(lowerTraceResults);
			resultList.addAll(upperTraceResults);
		}

		return resultList;

	}
}
