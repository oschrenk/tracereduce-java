package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.ParameterException;

/**
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CommandLineIntegrationTest {

	/** The Constant PATH. */
	private static final String PATH = "/trace.csv";

	/** The trace file. */
	private File traceFile;

	@Before
	public void setUp() throws Exception {
		traceFile = new File(this.getClass().getResource(PATH).getFile());
	}

	@Test
	public void test() throws IOException {
		final String[] args = new String[] { "-i",
				traceFile.getCanonicalPath(), "-e", "10", "-a",
				"douglas-peucker#reference", "-d", "3" };
		CommandLine.main(args);
	}

	@Test(expected = ParameterException.class)
	public void testWrongGeometry() throws IOException {
		final String[] args = new String[] { "-i",
				traceFile.getCanonicalPath(), "-e", "10", "-a",
				"douglas-peucker#reference", "-g", "FOO", "-d", "3" };
		CommandLine.main(args);
	}

	@Test
	public void testEuclideanGeometry() throws IOException {
		final String[] args = new String[] { "-i",
				traceFile.getCanonicalPath(), "-e", "10", "-a",
				"douglas-peucker#reference", "-g", "euclidean", "-d", "3" };
		CommandLine.main(args);
	}
}
