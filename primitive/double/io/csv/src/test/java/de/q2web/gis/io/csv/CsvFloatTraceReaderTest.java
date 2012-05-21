package de.q2web.gis.io.csv;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class CsvFloatTraceReaderTest.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CsvFloatTraceReaderTest {

	/** The Constant DIMENSIONS. */
	private static final int DIMENSIONS = 3;

	/** The Constant PATH. */
	private static final String PATH = "/trace.csv";

	/** The trace file. */
	private File traceFile;

	/** The trace reader. */
	private TraceReader traceReader;

	@Before
	public void setUp() throws Exception {
		traceFile = new File(this.getClass().getResource(PATH).getFile());
		traceReader = new CsvFloatPointReader(DIMENSIONS);
	}

	@Test
	public void test() throws IOException {

		final List<Point> trace = traceReader.read(traceFile);

		assertTrue(!trace.isEmpty());

	}
}
