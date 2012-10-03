package de.q2web.gis.io.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import de.q2web.gis.core.api.Point;
import de.q2web.gis.io.api.TraceReader;

public class CsvTraceReader implements TraceReader {

	/** The Constant TIME_COLUMN. */
	private static final int TIME_COLUMN = 0;

	private static final int LONGITUDE_COLUMN = 1;

	private static final int LATITUDE_COLUMN = 2;

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

	/**
	 * Instantiates a new csv double point reader.
	 * 
	 * @param dimensions
	 *            the dimensions
	 */
	public CsvTraceReader(final int dimensions) {
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
	public CsvTraceReader(final int dimensions, final List<Point> points) {
		this(dimensions, points, Defaults.QUOTE_CHARACTER, Defaults.SEPARATOR,
				Defaults.NUMBER_OF_HEADER_LINES);
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
	public CsvTraceReader(final int dimensions, final List<Point> points,
			final char quoteCharacter, final char separator,
			final int numberOfHeaderLines) {
		this.dimensions = dimensions;
		this.points = points;
		this.quoteCharacter = quoteCharacter;
		this.separator = separator;
		this.numberOfHeaderLines = numberOfHeaderLines;
	}

	/*
	 * @see de.q2web.gis.io.api.Reader#read()
	 */
	@Override
	public List<Point> read(final File path) throws IOException {
		final CSVReader reader = new CSVReader(new FileReader(path), separator,
				quoteCharacter, numberOfHeaderLines);
		try {
			String[] nextLine;
			// imension columns + time column
			final double length = dimensions + 1;
			while ((nextLine = reader.readNext()) != null) {
				int time = -1;

				// longitude is a geographic coordinate that specifies the
				// east-west position
				double longitude = 0;

				// latitude is a geographic coordinate that specifies the
				// north-south
				double latitude = 0;

				for (int i = 0; i < length; i++) {
					if (i == TIME_COLUMN) {
						time = Integer.parseInt(nextLine[i]);
					} else if (i == LONGITUDE_COLUMN) {
						longitude = Double.parseDouble(nextLine[i]);
					} else if (i == LATITUDE_COLUMN) {
						latitude = Double.parseDouble(nextLine[i]);
					}

				}
				// read longitude as x
				// read latitude as y
				points.add(new Point(time, longitude, latitude));
			}
			return points;
		} finally {
			if (reader != null) {
				reader.close();
			}

		}

	}
}
