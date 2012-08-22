package de.q2web.gis.ui.cli.jobs;

import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmInput;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.util.timer.TimedWorkUnit;
import de.q2web.util.timer.WorkUnitException;

/**
 * The Class AlgorithmTimedWorkUnit.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmTimedWorkUnit extends TimedWorkUnit<AlgorithmInput, List<Point>> {

	/** The algorithm. */
	private final Algorithm algorithm;

	/**
	 * Instantiates a new algorithm timed work unit.
	 * 
	 * @param algorithm
	 *            the algorithm
	 */
	public AlgorithmTimedWorkUnit(final Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	/*
	 * @see de.q2web.util.timer.TimedWorkUnit#work(java.lang.Object)
	 */
	@Override
	protected List<Point> work(final AlgorithmInput algorithmInput)
			throws WorkUnitException {

		return algorithm.run(algorithmInput.getTrace(),
				algorithmInput.getEpsilon());
	}
}
