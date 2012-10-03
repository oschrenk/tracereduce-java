package de.q2web.gis.io.kml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.junit.Test;

import de.q2web.gis.core.api.Point;
import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.api.TraceWriter;
import de.q2web.gis.io.csv.CsvTraceReader;

public class KmlTraceWriterTest {

	/** The Constant DIMENSIONS. */
	private static final int DIMENSIONS = 2;

	/** The Constant PATH. */
	private static final String PATH_1 = "/osmTrace_hwy_1.00_100+_00001.csv";

	private static final String PATH_2 = "/osmTrace_urb_1.00_100+_02262.csv";

	/** The trace file. */
	private File traceFile;

	/** The trace reader. */
	private TraceReader traceReader;

	@Test
	public void test1() throws IOException {

		traceFile = new File(this.getClass().getResource(PATH_1).getFile());
		traceReader = new CsvTraceReader(DIMENSIONS);

		final List<Point> trace = traceReader.read(traceFile);

		assertTrue(!trace.isEmpty());

		StringWriter writer = new StringWriter();

		TraceWriter kmlWriter = new KmlTraceWriter(DIMENSIONS, writer);

		kmlWriter.write(trace);

		String string = writer.toString();

		System.out.println(string);

	}

	@Test
	public void test2() throws IOException {

		traceFile = new File(this.getClass().getResource(PATH_2).getFile());
		traceReader = new CsvTraceReader(DIMENSIONS);

		final List<Point> trace = traceReader.read(traceFile);

		assertTrue(!trace.isEmpty());

		StringWriter writer = new StringWriter();

		TraceWriter kmlWriter = new KmlTraceWriter(DIMENSIONS, writer);

		kmlWriter.write(trace);

		String string = writer.toString();

		System.out.println(string);

	}

}
