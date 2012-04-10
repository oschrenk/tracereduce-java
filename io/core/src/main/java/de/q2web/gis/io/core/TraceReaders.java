package de.q2web.gis.io.core;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.csv.CsvDoublePointReader;
import de.q2web.gis.io.csv.CsvFloatPointReader;
import de.q2web.gis.trajectory.core.api.Precision;

/**
 * The Class TraceReaders.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceReaders {

	/**
	 * Builds the.
	 * 
	 * @param precision
	 *            the precision
	 * @param dimensions
	 *            the dimensions
	 * @return the trace reader
	 */
	@SuppressWarnings("rawtypes")
	public static TraceReader build(final Precision precision,
			final int dimensions) {

		if (precision.equals(Precision.DOUBLE)) {
			return new CsvDoublePointReader(dimensions);
		}

		if (precision.equals(Precision.FLOAT)) {
			return new CsvFloatPointReader(dimensions);
		}

		throw new IllegalArgumentException();
	}
}
