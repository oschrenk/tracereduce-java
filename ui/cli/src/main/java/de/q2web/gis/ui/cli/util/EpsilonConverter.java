package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IStringConverter;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class EpsilonConverter implements IStringConverter<Number> {

	/*
	 * @see com.beust.jcommander.IStringConverter#convert(java.lang.String)
	 */
	@Override
	public Number convert(final String value) {

		if (value == null) {
			throw new IllegalArgumentException("Invalid epsilon");
		}

		String trimmedValue = value.trim();

		if (trimmedValue.isEmpty()) {
			throw new IllegalArgumentException("Invalid epsilon");
		}

		return Double.parseDouble(value);
	}

}
