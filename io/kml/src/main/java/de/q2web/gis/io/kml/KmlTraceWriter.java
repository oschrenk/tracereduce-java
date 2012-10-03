package de.q2web.gis.io.kml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import de.q2web.gis.core.api.Point;
import de.q2web.gis.io.api.TraceWriter;

/**
 * The Class CsvDoublePointWriter.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class KmlTraceWriter implements TraceWriter {

	/** The dimensions. */
	private final int dimensions;

	/** The writer. */
	private final Writer writer;

	/**
	 * Instantiates a new csv double point writer.
	 * 
	 * @param dimensions
	 *            the dimensions
	 * @param writer
	 *            the writer
	 */
	public KmlTraceWriter(final int dimensions, final Writer writer) {
		this.dimensions = dimensions;
		this.writer = writer;

	}

	/*
	 * @see de.q2web.gis.io.api.TraceWriter#write(java.util.List)
	 */
	@Override
	public void write(final List<Point> trace) throws IOException {

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<kml xmlns=\"http://earth.google.com/kml/2.0\"> <Document>\n");

		writer.write("<Placemark>\n");
		writer.write(" <LineString>\n");
		writer.write("  <coordinates>\n");

		for (final Point point : trace) {
			final StringBuffer buffer = new StringBuffer();
			buffer.append(Double.toString(point.get(0)));
			buffer.append(", ");
			buffer.append(Double.toString(point.get(1)));

			if (dimensions == 3) {
				buffer.append(Double.toString(point.get(2)));
			} else {
				buffer.append(", 0.");
			}
			buffer.append("\n");
			writer.write(buffer.toString());
		}

		writer.write("  </coordinates>\n");
		writer.write(" </LineString>\n");
		writer.write("</Placemark>\n");
		writer.write("</Document> </kml>");

		if (writer != null) {
			writer.close();
		}
	}
}
