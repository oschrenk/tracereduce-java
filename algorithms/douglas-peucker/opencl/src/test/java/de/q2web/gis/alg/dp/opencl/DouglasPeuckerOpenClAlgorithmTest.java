package de.q2web.gis.alg.dp.opencl;

import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jocl.CL;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;
import org.jocl.utils.Contexts;
import org.jocl.utils.Devices;
import org.jocl.utils.Platforms;
import org.jocl.utils.Programs;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.q2web.gis.alg.dp.DouglasPeuckerReferenceAlgorithm;
import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;
import de.q2web.gis.geom.EuclideanDistance;

public class DouglasPeuckerOpenClAlgorithmTest {

	private Point start;
	private Point end;
	private List<Point> trace;

	private double epsilon;

	@BeforeClass
	public static void setUp() {
		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);
	}

	@Test
	public void testBuildProgram() {
		final cl_platform_id platformId = Platforms.getPlatforms().get(0);
		final cl_device_id deviceId = Devices.getDevices(platformId,
				CL_DEVICE_TYPE_GPU).get(0);
		final cl_context context = Contexts.create(platformId, deviceId);

		final cl_program program = clCreateProgramWithSource(context, 1,
				new String[] { DouglasPeuckerOpenClAlgorithm.SOURCE }, null,
				null);
		final int returnCode = clBuildProgram(program, 0, null, null, null,
				null);

		if (returnCode != CL.CL_SUCCESS) {
			final String buildLogs = Programs.obtainBuildLogs(program);
			System.err.println(buildLogs);
			fail();
		}
	}

	@Before
	public void setup() {
		start = new Point(new double[] { 1d, 3d });
		final Point b = new Point(new double[] { 2d, 4d });
		final Point c = new Point(new double[] { 3d, 2d });
		final Point d = new Point(new double[] { 4d, 4d });
		final Point e = new Point(new double[] { 5d, 2d });
		final Point f = new Point(new double[] { 6d, 4d });
		end = new Point(new double[] { 7d, 3d });

		trace = new ArrayList<Point>();
		trace.add(start);
		trace.add(b);
		trace.add(c);
		trace.add(d);
		trace.add(e);
		trace.add(f);
		trace.add(end);

		epsilon = 1;
	}

	@Test
	public void crossTestWithReferennce() {

		final Algorithm reference = new DouglasPeuckerReferenceAlgorithm(
				new EuclideanDistance());

		final Algorithm algorithm = new DouglasPeuckerOpenClAlgorithm(
				DouglasPeuckerOpenClAlgorithm.KERNEL_CROSSTRACK_EUCLIDEAN);
		final List<Point> reducedTrace = algorithm.run(trace, epsilon);

		assertEquals(reference.run(trace, epsilon), reducedTrace);

	}

}
