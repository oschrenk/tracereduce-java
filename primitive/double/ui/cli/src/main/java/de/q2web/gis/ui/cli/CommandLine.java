/*
 *******************************************************************************
 * CommandLine.java
 * $Id: $
 *
 *******************************************************************************
 *
 * Copyright:   Q2WEB GmbH
 *              quality to the web
 *
 *              Tel  : +49 (0) 211 / 159694-00	Kronprinzenstr. 82-84
 *              Fax  : +49 (0) 211 / 159694-09	40217 Düsseldorf
 *              eMail: info@q2web.de						http://www.q2web.de
 *
 *
 * Author:      University
 *
 * Created:     10.04.2012
 *
 * Copyright (c) 2009 Q2WEB GmbH.
 * All rights reserved.
 *
 *******************************************************************************
 */
package de.q2web.gis.ui.cli;

import java.io.File;

import com.beust.jcommander.JCommander;

import de.q2web.gis.algorithms.core.Algorithms;
import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.io.core.TraceReaders;
import de.q2web.gis.io.core.TraceWriters;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.ui.cli.util.EpsilonFactory;
import de.q2web.util.timer.WorkUnitException;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CommandLine {

	public static void main(final String[] args) {

		// 1. read startup arguments
		final StartupArguments startupArguments = new StartupArguments();
		final JCommander jCommander = new JCommander(startupArguments);
		jCommander.parse(args);

		// 2. build object tree from arguments
		final File input = startupArguments.getInput();
		final double epsilon = EpsilonFactory.build(startupArguments
				.getEpsilon());
		final AlgorithmTemplate algorithmTemplate = startupArguments
				.getAlgorithmTemplate();
		final int dimensions = startupArguments.getDimensions();
		final TraceReader traceReader = TraceReaders.build(dimensions);
		final TraceWriter traceWriter = TraceWriters.build(dimensions,
				startupArguments.getWriter());
		final Algorithm algorithm = Algorithms.build(algorithmTemplate);

		// 3. run the simplification process
		try {
			new TrajectorySimplification(traceReader, algorithm,
					startupArguments.isTimed(), traceWriter)
			.run(input, epsilon);
		} catch (final WorkUnitException e) {
			System.err.println(e);
		}

	}
}
