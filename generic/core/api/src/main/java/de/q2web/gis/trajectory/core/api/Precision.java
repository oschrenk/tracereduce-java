/*
 *******************************************************************************
 * Precision.java
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
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de
 */
public enum Precision {

	DOUBLE("double"), FLOAT("float");

	private final String id;

	private Precision(final String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 * @category getter
	 */
	public String getId() {
		return id;
	}

	public static Precision get(final String value) {
		final String lowerCase = value.toLowerCase();

		if (DOUBLE.getId().equals(lowerCase)) {
			return DOUBLE;
		}
		if (FLOAT.getId().equals(lowerCase)) {
			return FLOAT;
		}

		return null;
	}

}