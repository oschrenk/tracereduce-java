/*
 *******************************************************************************
 * PrecisisonValidator.java
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
package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import de.q2web.gis.trajectory.core.api.Precision;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class PrecisisonValidator implements IParameterValidator {

	/*
	 * @see com.beust.jcommander.IParameterValidator#validate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void validate(final String name, final String value)
			throws ParameterException {
		final String lowerCase = value.toLowerCase();

		if (Precision.get(lowerCase) == null) {
			throw new ParameterException(String.format(
					"Value \"%s\" is not a valid precision", value));
		}

	}
}
