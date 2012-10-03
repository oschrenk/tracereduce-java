package de.q2web.gis.ui.cli.util;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DistanceValidator implements IParameterValidator {

	protected static final String HAVERSINE = "haversine";
	protected static final String SPHERICAL = "spherical";
	protected static final String EUCLIDEAN = "euclidean";

	private static final List<String> validDistances = new ArrayList<String>();

	static {
		validDistances.add(EUCLIDEAN);
		validDistances.add(SPHERICAL);
		validDistances.add(HAVERSINE);
	}

	/*
	 * @see com.beust.jcommander.IParameterValidator#validate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void validate(final String name, final String value)
			throws ParameterException {
		if (value == null) {
			throw new ParameterException("No valid distance given.");
		}

		final String distance = value.trim().toLowerCase();

		if (!validDistances.contains(distance)) {
			throw new ParameterException("No valid distance given.");
		}
	}
}
