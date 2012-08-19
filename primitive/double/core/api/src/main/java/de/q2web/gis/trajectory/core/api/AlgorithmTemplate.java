/*
 *******************************************************************************
 * Algorithm.java
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
package de.q2web.gis.trajectory.core.api;

/**
 * The Class Algorithm.
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmTemplate {

	private final String name;

	private final String geometry;

	public AlgorithmTemplate(final String name, final String geometry) {
		super();
		this.name = name;
		this.geometry = geometry;
	}

	public String getName() {
		return name;
	}

	public String getGeometry() {
		return geometry;
	}

}
