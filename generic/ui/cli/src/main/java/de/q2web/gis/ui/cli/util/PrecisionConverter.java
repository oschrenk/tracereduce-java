package de.q2web.gis.ui.cli.util;

import com.beust.jcommander.IStringConverter;

import de.q2web.gis.trajectory.core.api.Precision;

public class PrecisionConverter implements IStringConverter<Precision> {
	@Override
	public Precision convert(final String value) {
		return Precision.get(value);
	}
}
