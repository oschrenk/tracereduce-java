package de.q2web.gis.alg.cs.opencl;

import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueBarrier;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clSetKernelArg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;
import org.jocl.utils.CommandQueues;
import org.jocl.utils.Contexts;
import org.jocl.utils.Devices;
import org.jocl.utils.Kernels;
import org.jocl.utils.Platforms;
import org.jocl.utils.Programs;

import de.q2web.gis.core.api.Algorithm;
import de.q2web.gis.core.api.Point;
import de.q2web.jocl.util.Integers;
import de.q2web.jocl.util.Resources;

/**
 * The Class ReferenceCubicSplinesAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CubicSplinesOpenClAlgorithm implements Algorithm {

	private static final long[] DEFAULT_LOCAL_WORKSIZE = new long[] { 1 };

	static final String SOURCE = Resources
			.convertStreamToString(CubicSplinesOpenClAlgorithmTest.class
					.getResourceAsStream("cubicSplines.cl"));

	public static final String KERNEL_CUBIC_SPLINE = "cubicSpline";
	public static final String KERNEL_MINIMUM_WITH_POSITION = "minimumWithPositionFloat";

	private float[] longitudeXCoordinates;
	private float[] latitudeYCoordinates;

	private cl_mem memObject[];
	private cl_command_queue queue;
	private cl_kernel splineKernel;
	private cl_kernel minimumKernel;

	/*
	 * @see de.q2web.gis.core.api.Algorithm#run(java.util.List, double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		// transform to arrays
		final int length = trace.size();

		longitudeXCoordinates = new float[length];
		latitudeYCoordinates = new float[length];
		float[] distance = new float[length - 2];
		int[] currentExcludes = new int[length];

		for (int i = 0; i < trace.size(); i++) {
			final Point point = trace.get(i);
			longitudeXCoordinates[i] = (float) point.get(0);
			latitudeYCoordinates[i] = (float) point.get(1);
		}

		// OpenCl Preamble
		final cl_platform_id platformId = Platforms.getPlatforms().get(0);
		final cl_device_id deviceId = Devices.getDevices(platformId,
				CL_DEVICE_TYPE_GPU).get(0);
		final cl_context context = Contexts.create(platformId, deviceId);
		queue = CommandQueues.create(context, deviceId);
		final cl_program program = Programs.createFromSource(context, SOURCE);

		// Create Kernels
		splineKernel = Kernels.create(program, KERNEL_CUBIC_SPLINE);
		minimumKernel = Kernels.create(program, KERNEL_MINIMUM_WITH_POSITION);

		Pointer pointerLongitudeX = Pointer.to(longitudeXCoordinates);
		Pointer pointerLatitudeY = Pointer.to(latitudeYCoordinates);
		Pointer pointerDistance = Pointer.to(distance);
		Pointer pointerCurrentExcludes = Pointer.to(currentExcludes);

		memObject = new cl_mem[4];
		// longitude / x array
		memObject[0] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				pointerLongitudeX, null);
		// longitude / y array
		memObject[1] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				pointerLatitudeY, null);
		// distance array, results will be written here
		memObject[2] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_float * length, null, null);
		// currentExcludes
		memObject[3] = clCreateBuffer(context, CL_MEM_READ_WRITE
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * length,
				pointerCurrentExcludes, null);

		float minimum = Float.MAX_VALUE;

		do {

			// Set default arguments for the kernels
			// x
			clSetKernelArg(splineKernel, 0, Sizeof.cl_mem,
					Pointer.to(memObject[0]));
			// y
			clSetKernelArg(splineKernel, 1, Sizeof.cl_mem,
					Pointer.to(memObject[1]));
			// distance
			clSetKernelArg(splineKernel, 2, Sizeof.cl_mem,
					Pointer.to(memObject[2]));
			// excludes
			clSetKernelArg(splineKernel, 3, Sizeof.cl_mem,
					Pointer.to(memObject[3]));
			clSetKernelArg(splineKernel, 4, Sizeof.cl_uint,
					Pointer.to(new int[] { length }));

			final long[] globalWorkSize = new long[] { length };

			// Execute the kernel
			clEnqueueNDRangeKernel(queue, splineKernel, 1, null,
					globalWorkSize, DEFAULT_LOCAL_WORKSIZE, 0, null, null);

			// Read the output data
			clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, 0,
					distance.length * Sizeof.cl_float, pointerDistance, 0,
					null, null);

			// minimumKernel

			// 2. get minimum distance
			clSetKernelArg(minimumKernel, 0, Sizeof.cl_mem,
					Pointer.to(memObject[2]));
			clSetKernelArg(minimumKernel, 1, Sizeof.cl_uint,
					Pointer.to(new int[] { length }));

			long minimumDistanceGlobalWorkSize = Integers.nextBinary(length);
			for (int pass = 0; minimumDistanceGlobalWorkSize > 1; pass++) {
				clSetKernelArg(minimumKernel, 2, Sizeof.cl_uint,
						Pointer.to(new int[] { pass }));
				clEnqueueNDRangeKernel(queue, minimumKernel, 1, null,
						new long[] { minimumDistanceGlobalWorkSize >>= 1 },
						DEFAULT_LOCAL_WORKSIZE, 0, null, null);
				clEnqueueBarrier(queue);
			}

			// Read the distance data
			clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, 0,
					distance.length * Sizeof.cl_float, pointerDistance, 0,
					null, null);
			System.out.println(Arrays.toString(distance));

			final float[] values = new float[2];
			clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, 0,
					Sizeof.cl_float * 2, Pointer.to(values), 0, null, null);

			System.out.println(Arrays.toString(values));

			minimum = values[0];
			final int positionOfMinimum = (int) values[1];

			if (minimum < epsilon) {
				currentExcludes[positionOfMinimum] = 1;
			}

		} while (minimum < epsilon);

		List<Point> simplifiedTrace = new LinkedList<Point>();

		for (int i = 0; i < currentExcludes.length; i++) {
			if (currentExcludes[i] == 0) {
				simplifiedTrace.add(trace.get(i));
			}
		}

		return simplifiedTrace;
	}

}
