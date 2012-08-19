/*
 *******************************************************************************
 * InputValidator.java
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

import java.io.File;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class InputValidator implements IParameterValidator {

	/*
	 * @see com.beust.jcommander.IParameterValidator#validate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void validate(final String name, final String value)
			throws ParameterException {
		final File path = new File(value);
		if (!path.exists()) {
			throw new ParameterException("Input file does not exist.");
		}
		if (!path.isFile()) {
			throw new ParameterException("Input must be a file.");
		}
		if (!path.canRead()) {
			throw new ParameterException("Can't read input file.");
		}
	}

}
