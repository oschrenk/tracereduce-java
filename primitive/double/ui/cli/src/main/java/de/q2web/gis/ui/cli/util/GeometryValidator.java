package de.q2web.gis.ui.cli.util;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class GeometryValidator implements IParameterValidator {

	protected static final String SPHEROIDAL = "spheroidal";
	protected static final String SPHERICAL = "spherical";
	protected static final String EUCLIDEAN = "euclidean";

	private static final List<String> validGeometries = new ArrayList<String>();

	static {
		validGeometries.add(EUCLIDEAN);
		validGeometries.add(SPHERICAL);
		validGeometries.add(SPHEROIDAL);
	}

	/*
	 * @see com.beust.jcommander.IParameterValidator#validate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void validate(final String name, final String value)
			throws ParameterException {
		if (value == null) {
			throw new ParameterException("No valid geometry given.");
		}

		final String geometry = value.trim().toLowerCase();

		if (!validGeometries.contains(geometry)) {
			throw new ParameterException("No valid geometry given.");
		}
	}
}
