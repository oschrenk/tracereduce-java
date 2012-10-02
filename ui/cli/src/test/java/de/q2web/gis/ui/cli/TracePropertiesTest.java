package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.csv.CsvTraceReader;
import de.q2web.gis.trajectory.core.api.Point;

public class TracePropertiesTest {

	private static final int DIM = 2;
	private static final String TRACES_HIGHWAY = System
			.getProperty("user.home")
			+ File.separator
			+ "data"
			+ File.separator + "highway";
	private static final String TRACES_URBAN = System.getProperty("user.home")
			+ File.separator + "data" + File.separator + "urban";

	private static final File DIR_HIGHWAY = new File(TRACES_HIGHWAY);
	private static final File DIR_URBAN = new File(TRACES_URBAN);

	@Ignore
	@Test
	public void highwayTraces() {
		testDirectory(DIR_HIGHWAY);
	}

	@Ignore
	@Test
	public void urbanTraces() {
		testDirectory(DIR_URBAN);
	}

	@Test
	public void testAll() {
		testDirectory(DIR_HIGHWAY);
		testDirectory(DIR_URBAN);
	}

	public void testDirectory(final File dir) {

		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.toLowerCase().endsWith(".csv");
			}
		};

		TraceReader reader = new CsvTraceReader(DIM);
		int fails = 0;

		long minPoints = Integer.MAX_VALUE;
		long maxPoints = Integer.MIN_VALUE;

		// east west
		double minLongitude = Double.MAX_VALUE;
		double maxLongitude = Double.MIN_VALUE;

		double sumDeltaLongitude = 0;

		// south north
		double minLatitude = Double.MAX_VALUE;
		double maxLatitude = Double.MIN_VALUE;

		double sumDeltaLatitude = 0;

		long sumPoints = 0;
		long files = 0;

		File[] csvFiles = dir.listFiles(fileFilter);
		for (File csvFile : csvFiles) {
			List<Point> trace;
			try {

				trace = reader.read(csvFile);
				int size = trace.size();

				double traceMinLongitude = Double.MAX_VALUE;
				double traceMaxLongitude = Double.MIN_VALUE;
				double traceMinLatitude = Double.MAX_VALUE;
				double traceMaxLatitude = Double.MIN_VALUE;
				double traceDeltaLongitude = 0;
				double traceDeltaLatitude = 0;

				for (Point point : trace) {

					// all files
					if (point.get(0) < minLongitude) {
						minLongitude = point.get(0);
					}
					if (point.get(0) > maxLongitude) {
						maxLongitude = point.get(0);
					}
					if (point.get(1) < minLatitude) {
						minLatitude = point.get(0);
					}
					if (point.get(1) > maxLatitude) {
						maxLatitude = point.get(0);
					}

					// only trace
					if (point.get(0) < traceMinLongitude) {
						traceMinLongitude = point.get(0);
					}
					if (point.get(0) > traceMaxLongitude) {
						traceMaxLongitude = point.get(0);
					}
					if (point.get(1) < traceMinLatitude) {
						traceMinLatitude = point.get(0);
					}
					if (point.get(1) > traceMaxLatitude) {
						traceMaxLatitude = point.get(0);
					}

					traceDeltaLongitude = Math.abs(traceMaxLongitude
							- traceMinLongitude);
					traceDeltaLatitude = Math.abs(traceMaxLatitude
							- traceMinLatitude);

					// add to sum delta
					sumDeltaLongitude = sumDeltaLongitude + traceDeltaLongitude;
					sumDeltaLatitude = sumDeltaLatitude + traceDeltaLatitude;

				}

				if (size < minPoints) {
					minPoints = size;
				}
				if (size > maxPoints) {
					maxPoints = size;
				}

				files++;
				sumPoints = sumPoints + size;
			} catch (IOException e) {
				System.err.println(csvFile);
				fails = fails + 1;
			} catch (NumberFormatException e) {
				System.err.println(csvFile);
				fails++;
			}
			trace = null;

		}

		System.out.println("-----");
		System.out.println("Min " + minPoints);
		System.out.println("Max " + maxPoints);
		System.out.println("Cnt " + files);
		System.out.println("Avg " + sumPoints / files);

		System.out.println("MinLon " + minLongitude);
		System.out.println("MaxLon " + maxLongitude);
		System.out.println("MinLat " + minLatitude);
		System.out.println("MaxLat " + maxLatitude);

		System.out.println("Avg Lon Delta " + sumDeltaLongitude / files);
		System.out.println("Avg Lat Delta " + sumDeltaLatitude / files);

	}
}
