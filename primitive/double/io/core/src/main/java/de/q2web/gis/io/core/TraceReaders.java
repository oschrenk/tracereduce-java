package de.q2web.gis.io.core;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.csv.CsvFloatPointReader;

/**
 * The Class TraceReaders.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceReaders {

	/**
	 * Builds the.
	 * 
	 * 
	 * @param dimensions
	 *            the dimensions
	 * @return the trace reader
	 */
	public static TraceReader build(final int dimensions) {
		return new CsvFloatPointReader(dimensions);
	}
}
