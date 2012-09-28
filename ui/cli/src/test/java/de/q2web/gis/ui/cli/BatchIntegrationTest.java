package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import de.q2web.gis.ui.cli.util.Duration;

public class BatchIntegrationTest {

	private static final boolean BATCH_MODE = true;

	private static final String DEV_NULL = "/dev/null";

	private static final String TWO_DIM = "2";

	private static final String DOUGLAS_PEUCKER_REFERENCE = "douglas-peucker#reference";
	private static final String DOUGLAS_PEUCKER_OPENCL = "douglas-peucker#opencl";

	private static final String IMAI_REFERENCE = "imai#reference";
	private static final String IMAI_OPENCL = "imai#opencl";

	private static final String SPLINE_REFERENCE = "spline#reference";
	private static final String SPLINE_OPENCL = "spline#opencl";

	private static final String DEFAULT_EPSILON = "10";

	private static final String TRACES_HIGHWAY = "/osmTrace_hwy_1.00_100plus";
	private static final String TRACES_URBAN = "/osmTrace_urb_1.00_100plus";

	private static final File[] DIRECTORIES = new File[2];

	@Before
	public void setUp() throws Exception {

		DIRECTORIES[0] = new File(this.getClass().getResource(TRACES_HIGHWAY)
				.getFile());
		DIRECTORIES[1] = new File(this.getClass().getResource(TRACES_URBAN)
				.getFile());
	}

	@Test
	public void testDouglasPeuckerReference() throws IOException {
		testAlgorithm(DOUGLAS_PEUCKER_REFERENCE);
	}

	@Test
	public void testDouglasPeuckerOpenCL() throws IOException {
		testAlgorithm(DOUGLAS_PEUCKER_OPENCL);
	}

	@Test
	public void testImaiReference() throws IOException {
		testAlgorithm(IMAI_REFERENCE);
	}

	@Test
	public void testImaiOpenCL() throws IOException {
		testAlgorithm(IMAI_OPENCL);
	}

	private void testAlgorithm(final String algorithm) throws IOException {

		int filesTested = 0;
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		for (File dir : DIRECTORIES) {
			File[] csvFiles = dir.listFiles();
			for (File csvFile : csvFiles) {
				final String[] args = new String[] { "-i",
						csvFile.getCanonicalPath(), "-e", DEFAULT_EPSILON,
						"-a", DOUGLAS_PEUCKER_OPENCL, "-d", TWO_DIM, "-o",
						DEV_NULL };
				CommandLine.main(args, BATCH_MODE);
				filesTested++;
			}
		}
		stopwatch.stop();
		long elapsedTime = stopwatch.elapsedTime(TimeUnit.NANOSECONDS);
		System.out.println(String.format("Tested %s files in %s", filesTested,
				Duration.of(elapsedTime)));
	}
}
