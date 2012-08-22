package de.q2web.gis.io.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class CsvDoublePointWriter.
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CsvTraceWriter implements TraceWriter {

	/** The dimensions. */
	private final int dimensions;

	/** The writer. */
	private final Writer writer;

	/** The quote character. */
	private final char quoteCharacter;

	/** The separator. */
	private final char separator;

	/**
	 * Instantiates a new csv double point writer.
	 *
	 * @param dimensions
	 *            the dimensions
	 * @param writer
	 *            the writer
	 */
	public CsvTraceWriter(final int dimensions, final Writer writer) {
		this(dimensions, writer, Defaults.QUOTE_CHARACTER, Defaults.SEPARATOR);
	}

	/**
	 *
	 * @param dimensions
	 *            the dimensions
	 * @param writer
	 *            the writer
	 * @param quoteCharacter
	 *            the quote character
	 * @param separator
	 *            the separator
	 */
	public CsvTraceWriter(final int dimensions, final Writer writer,
			final char quoteCharacter, final char separator) {
		this.dimensions = dimensions;
		this.writer = writer;
		this.quoteCharacter = quoteCharacter;
		this.separator = separator;
	}

	/*
	 * @see de.q2web.gis.io.api.TraceWriter#write(java.util.List)
	 */
	@Override
	public void write(final List<Point> trace) throws IOException {
		final CSVWriter csvWriter = new CSVWriter(writer, separator,
				quoteCharacter);
		for (final Point point : trace) {
			final String[] line = new String[dimensions + 1];
			line[0] = Integer.toString(point.getTime());
			for (int i = 0; i < dimensions; i++) {
				line[i + 1] = Double.toString(point.get(i));
				csvWriter.writeNext(line);
			}
		}
		csvWriter.close();
		if (writer != null) {
			writer.close();
		}
	}

}
