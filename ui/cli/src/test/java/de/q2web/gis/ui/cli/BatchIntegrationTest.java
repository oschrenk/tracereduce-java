package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import de.q2web.gis.ui.cli.util.Duration;

public class BatchIntegrationTest {

	private static Logger LOGGER = LoggerFactory
			.getLogger(BatchIntegrationTest.class);

	private static final boolean BATCH_MODE = true;

	private static final File OUTPUT_DIRECTORY = new File(
			System.getProperty("user.home") + File.separator + "data"
					+ File.separator + "output");

	private static final String TWO_DIM = "2";

	private static final String DOUGLAS_PEUCKER_REFERENCE = "douglas-peucker#reference";
	private static final String DOUGLAS_PEUCKER_OPENCL = "douglas-peucker#opencl";

	private static final String IMAI_REFERENCE = "imai#reference";
	private static final String IMAI_OPENCL = "imai#opencl";

	private static final String SPLINE_REFERENCE = "spline#reference";
	private static final String SPLINE_OPENCL = "spline#opencl";

	private static final float DEFAULT_EPSILON = 0.1f;

	private static final String TRACES_CURRENT = System
			.getProperty("user.home")
			+ File.separator
			+ "data"
			+ File.separator + "highway_64kb+redux";
	private static final File TEST_DIRECTORY_CURRENT = new File(TRACES_CURRENT);

	@Test
	public void testDouglasPeuckerReference() throws IOException {
		testAlgorithm(DOUGLAS_PEUCKER_REFERENCE, DEFAULT_EPSILON);
	}

	@Test
	public void testDouglasPeuckerOpenCL() throws IOException {
		testAlgorithm(DOUGLAS_PEUCKER_OPENCL, DEFAULT_EPSILON);
	}

	@Test
	public void testImaiReference() throws IOException {
		testAlgorithm(IMAI_REFERENCE, DEFAULT_EPSILON);
	}

	@Test
	public void testImaiOpenCL() throws IOException {
		testAlgorithm(IMAI_OPENCL, DEFAULT_EPSILON);
	}

	private void testAlgorithm(final String algorithm, final float epsilon)
			throws IOException {

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.toLowerCase().endsWith(".csv");
			}
		};

		int filesTested = 0;
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		LOGGER.info("#{}, {} with epsilon {}", TEST_DIRECTORY_CURRENT, algorithm, epsilon);

		File[] csvFiles = TEST_DIRECTORY_CURRENT.listFiles(filenameFilter);
		for (File csvFile : csvFiles) {

			String base = getNameWithoutExtension(csvFile.getName());
			String extension = ".kml";

			File outputFile = new File(OUTPUT_DIRECTORY, base + extension);

			final String[] args = new String[] { "-i",
					csvFile.getCanonicalPath(), "-e", Float.toString(epsilon),
					"-a", algorithm, "-d", TWO_DIM, "-o",
					outputFile.getAbsolutePath() };
			CommandLine.main(args, BATCH_MODE);
			filesTested++;
		}

		stopwatch.stop();
		long elapsedTime = stopwatch.elapsedTime(TimeUnit.NANOSECONDS);
		System.out.println(String.format("Tested %s files in %s", filesTested,
				Duration.of(elapsedTime)));
	}

	public static String getNameWithoutExtension(final String file) {
		String fileName = new File(file).getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
	}
}
