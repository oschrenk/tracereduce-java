/*
 *******************************************************************************
 * ReaderTimedWorkUnit.java
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
package de.q2web.gis.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.util.timer.TimedWorkUnit;
import de.q2web.util.timer.WorkUnitException;

/**
 * The Class ReaderTimedWorkUnit.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceReaderTimedWorkUnit extends TimedWorkUnit<File, List<Point>> {

	/** The point rader. */
	private final TraceReader traceRader;

	/**
	 * Instantiates a new reader timed work unit.
	 * 
	 * @param traceRader
	 *            the trace rader
	 */
	public TraceReaderTimedWorkUnit(final TraceReader traceRader) {
		this.traceRader = traceRader;
	}

	/*
	 * @see de.q2web.util.timer.TimedWorkUnit#work(java.lang.Object)
	 */
	@Override
	protected List<Point> work(final File file) throws WorkUnitException {
		List<Point> points;
		try {
			points = traceRader.read(file);
		} catch (final IOException e) {
			throw new WorkUnitException(e);
		}
		return points;
	}
}
