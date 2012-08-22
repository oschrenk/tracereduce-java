/*
 *******************************************************************************
 * WorkUnitException.java
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
package de.q2web.util.timer;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de
 */
public class WorkUnitException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public WorkUnitException(final Throwable e) {
		super(e);
	}

}
