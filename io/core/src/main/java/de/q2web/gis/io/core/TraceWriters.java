package de.q2web.gis.io.core;

import java.io.Writer;

import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.io.csv.CsvTraceWriter;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceWriters {

	public static TraceWriter build(final int dimensions, final Writer writer) {
		return new CsvTraceWriter(dimensions, writer);
	}

}
