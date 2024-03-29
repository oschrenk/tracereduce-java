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

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Distance;
import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.io.core.TraceReaders;
import de.q2web.gis.io.core.TraceWriters;
import de.q2web.gis.ui.cli.util.AlgorithmFactory;
import de.q2web.gis.ui.cli.util.EpsilonFactory;
import de.q2web.gis.ui.cli.util.ExitCodes;
import de.q2web.gis.ui.cli.util.ExitException;
import de.q2web.util.timer.WorkUnitException;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CommandLine {

	/** The Constant ZERO. Everything went fine. */
	private static final int ZERO = 0;

	private static boolean DEFAULT_BATCH = false;

	public static void main(final String[] args) {
		new CommandLine().run(args, DEFAULT_BATCH);
	}

	protected static void main(final String[] args, final boolean batchMode) {
		new CommandLine().run(args, batchMode);
	}

	protected void run(final String[] args, final boolean batchMode) {

		int exitCode = ZERO;
		File input = null;

		try {
			// 1. read startup arguments
			final StartupArguments startupArguments = new StartupArguments();
			final JCommander jCommander = new JCommander(startupArguments);
			jCommander.parse(args);

			// 2. build object tree from arguments
			input = startupArguments.getInput();
			final double epsilon = EpsilonFactory.build(startupArguments
					.getEpsilon());
			final String algorithmName = startupArguments.getAlgorithm();
			final Distance distance = startupArguments.getDistance();

			final int dimensions = startupArguments.getDimensions();
			final TraceReader traceReader = TraceReaders.build(dimensions);
			final TraceWriter traceWriter = TraceWriters.build(dimensions,
					startupArguments.getWriter());
			final Algorithm algorithm = AlgorithmFactory.build(algorithmName,
					distance);

			// 3. run the simplification process
			try {
				new TraceReduce(traceReader, algorithm,
						startupArguments.isTimed(), traceWriter).run(input,
						epsilon);
			} catch (final WorkUnitException e) {
				throw new ExitException("A work unit failed.", e,
						ExitCodes.EX_SOFTWARE);
			}
		} catch (final ExitException e) {
			e.printStackTrace(System.err);
			exitCode = e.getExitCode();
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			exitCode = ExitCodes.EX_SOFTWARE;
		} finally {
			if (!batchMode) {
				System.exit(exitCode);
			} else {
				if (exitCode != ZERO) {
					System.out.println(String.format("Error in file %s",
							input.getAbsolutePath()));
				}
			}
		}

	}
}
