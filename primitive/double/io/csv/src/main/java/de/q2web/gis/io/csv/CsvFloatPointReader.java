package de.q2web.gis.io.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.trajectory.core.api.DoublePoint;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class CsvFloatReader.
 */
public class CsvFloatPointReader implements TraceReader {

	/** The dimensions. */
	private final int dimensions;

	/** The points. */
	private final List<Point> points;

	/** The quote character. */
	private final char quoteCharacter;

	/** The separator. */
	private final char separator;

	/** The number of header lines. */
	private final int numberOfHeaderLines;

	private final int ignorableColumns;

	public CsvFloatPointReader(final int dimensions) {
		this(dimensions, new ArrayList<Point>());
	}

	/**
	 * Instantiates a new csv float reader.
	 * 
	 * @param dimensions
	 *            the dimensions
	 * @param points
	 *            the points
	 */
	public CsvFloatPointReader(final int dimensions, final List<Point> points) {
		this(dimensions, points, Defaults.QUOTE_CHARACTER, Defaults.SEPARATOR,
				Defaults.NUMBER_OF_HEADER_LINES,
				Defaults.NUMBER_IGNORABLE_COLUMNS);
	}

	/**
	 * Instantiates a new csv float reader.
	 * 
	 * @param dimensions
	 *            the dimensions
	 * @param points
	 *            the points
	 * @param quoteCharacter
	 *            the quote character
	 * @param separator
	 *            the separator
	 * @param numberOfHeaderLines
	 *            the number of header lines
	 * @param ignorableColumns
	 *            the ignorable columns
	 */
	public CsvFloatPointReader(final int dimensions, final List<Point> points,
			final char quoteCharacter, final char separator,
			final int numberOfHeaderLines, final int ignorableColumns) {
		this.dimensions = dimensions;
		this.points = points;
		this.quoteCharacter = quoteCharacter;
		this.separator = separator;
		this.numberOfHeaderLines = numberOfHeaderLines;
		this.ignorableColumns = ignorableColumns;
	}

	/*
	 * @see de.q2web.gis.io.api.Reader#read()
	 */
	@Override
	public List<Point> read(final File path) throws IOException {
		final CSVReader reader = new CSVReader(new FileReader(path), separator,
				quoteCharacter, numberOfHeaderLines);
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			final double[] point = new double[dimensions];
			for (int i = 0; i < dimensions; i++) {
				point[i] = Double.parseDouble(nextLine[i + ignorableColumns]);
			}
			points.add(new DoublePoint(point));
		}
		return points;
	}

}
