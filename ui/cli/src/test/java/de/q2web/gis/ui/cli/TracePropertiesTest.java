package de.q2web.gis.ui.cli;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.q2web.gis.core.api.Distance;
import de.q2web.gis.core.api.Point;
import de.q2web.gis.geom.HaversineDistance;
import de.q2web.gis.io.api.TraceReader;
import de.q2web.gis.io.csv.CsvTraceReader;

public class TracePropertiesTest {

	private static final int DIM = 2;
	private static final String TRACES_CURRENT = System
			.getProperty("user.home")
			+ File.separator
			+ "data"
			+ File.separator + "urban_64kb+redux";
	private static final File TEST_DIRECTORY_CURRENT = new File(TRACES_CURRENT);

	@Test
	public void test() {
		testDirectory(TEST_DIRECTORY_CURRENT);
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
		double minLength = Double.POSITIVE_INFINITY;
		double maxLength = Double.NEGATIVE_INFINITY;
		double sumLength = 0;
		long files = 0;

		Distance distance = new HaversineDistance();

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

				double length = distance.distance(trace.get(0),
						trace.get(size - 1));
				sumLength += length;

				if (length < minLength) {
					minLength = length;
				}

				if (length > maxLength) {
					maxLength = length;
					System.out.println(csvFile);
				}

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

		System.out.println("Avg Length " + sumLength / files);
		System.out.println("Min Length " + minLength);
		System.out.println("Max Length  " + maxLength);

		System.out.println("MinLon " + minLongitude);
		System.out.println("MaxLon " + maxLongitude);
		System.out.println("MinLat " + minLatitude);
		System.out.println("MaxLat " + maxLatitude);

		System.out.println("Avg Lon Delta " + sumDeltaLongitude / files);
		System.out.println("Avg Lat Delta " + sumDeltaLatitude / files);

	}
}
