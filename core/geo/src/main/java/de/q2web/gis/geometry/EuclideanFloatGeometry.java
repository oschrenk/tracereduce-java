/*
 *******************************************************************************
 * EuclideanGeometry.java
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
 * Created:     09.04.2012
 *
 * Copyright (c) 2009 Q2WEB GmbH.
 * All rights reserved.
 *
 *******************************************************************************
 */
package de.q2web.gis.geometry;

import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de
 */
public class EuclideanFloatGeometry implements Geometry<Float> {

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public Float distance(final Point<Float> from, final Point<Float> to) {
		// TODO Geometry<Float>.distance()
		return null;
	}

	/*
	 * @see
	 * de.q2web.gis.trajectory.core.api.Geometry#distance(de.q2web.gis.trajectory
	 * .core.api.Point, de.q2web.gis.trajectory.core.api.Point,
	 * de.q2web.gis.trajectory.core.api.Point)
	 */
	@Override
	public Float distance(final Point<Float> point,
			final Point<Float> lineStart, final Point<Float> lineEnd) {
		// TODO Geometry<Float>.distance()
		return null;
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Geometry#compare(java.lang.Number,
	 * java.lang.Number)
	 */
	@Override
	public int compare(final Float a, final Float b) {
		// TODO Geometry<Float>.compare()
		return 0;
	}

}
