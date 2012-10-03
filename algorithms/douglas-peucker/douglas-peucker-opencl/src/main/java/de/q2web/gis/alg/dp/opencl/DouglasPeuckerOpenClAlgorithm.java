package de.q2web.gis.alg.dp.opencl;

import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueBarrier;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.util.ArrayList;
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

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.jocl.util.Integers;
import de.q2web.jocl.util.Resources;

/**
 * The Class DouglasPeuckerAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DouglasPeuckerOpenClAlgorithm implements Algorithm {

	private static final long[] DEFAULT_LOCAL_WORKSIZE = new long[] { 1 };

	private static final String SOURCE = Resources
			.convertStreamToString(DouglasPeuckerOpenClAlgorithm.class
					.getResourceAsStream("douglasPeucker.cl"));

	public static final String KERNEL_CROSSTRACK_EUCLIDEAN = "euclidean2dPointLineDistance";
	public static final String KERNEL_CROSSTRACK_SPHERICAL = "spherical2dPointLineDistance";

	private static final String KERNEL_MAXIMUM = "maximumWithPositionAndOffsetFloat";

	private cl_mem memObject[];
	private cl_command_queue queue;
	private cl_kernel distanceKernel;
	private cl_kernel maximumKernel;

	private float[] xCoordinates;
	private float[] yCoordinates;
	/** The epsilon. */
	private double epsilon;

	private final List<Integer> knots = new LinkedList<Integer>();

	private final String crossTrackMetric;

	/**
	 * Instantiates a new douglas peucker algorithm.
	 * 
	 */
	public DouglasPeuckerOpenClAlgorithm(final String crossTrackMetric) {
		this.crossTrackMetric = crossTrackMetric;

		if (!crossTrackMetric.equals(KERNEL_CROSSTRACK_EUCLIDEAN)) {
			if (!crossTrackMetric.equals(KERNEL_CROSSTRACK_SPHERICAL)) {
				throw new IllegalArgumentException("No valid distance kernel");
			}
		}
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		// transform to arrays
		final int length = trace.size();
		xCoordinates = new float[length];
		yCoordinates = new float[length];

		for (int i = 0; i < trace.size(); i++) {
			final Point point = trace.get(i);
			xCoordinates[i] = (float) point.get(0);
			yCoordinates[i] = (float) point.get(1);
		}

		// run and fill global knots array
		run();

		// map index to original trace
		final List<Point> result = new ArrayList<Point>();
		for (final Integer index : knots) {
			result.add(trace.get(index));
		}

		return result;
	}

	private void run() {

		if (xCoordinates.length != yCoordinates.length) {
			throw new IllegalArgumentException(
					"Source arrays must be same length");
		}

		final int length = xCoordinates.length;

		// OpenCl Preamble
		final cl_platform_id platformId = Platforms.getPlatforms().get(0);
		final cl_device_id deviceId = Devices.getDevices(platformId,
				CL_DEVICE_TYPE_GPU).get(0);
		final cl_context context = Contexts.create(platformId, deviceId);
		queue = CommandQueues.create(context, deviceId);
		final cl_program program = Programs.createFromSource(context, SOURCE);
		distanceKernel = Kernels.create(program, crossTrackMetric);
		maximumKernel = Kernels.create(program, KERNEL_MAXIMUM);

		final Pointer xCoordinatesPointer = Pointer.to(xCoordinates);
		final Pointer yCoordinatesPointer = Pointer.to(yCoordinates);

		memObject = new cl_mem[3];
		memObject[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				xCoordinatesPointer, null);
		memObject[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				yCoordinatesPointer, null);
		memObject[2] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_float * length, null, null);

		// Set default arguments for the kernels
		// distanceKernel
		clSetKernelArg(distanceKernel, 0, Sizeof.cl_mem,
				Pointer.to(memObject[0]));
		clSetKernelArg(distanceKernel, 1, Sizeof.cl_mem,
				Pointer.to(memObject[1]));
		clSetKernelArg(distanceKernel, 2, Sizeof.cl_mem,
				Pointer.to(memObject[2]));
		// maximumKernel
		clSetKernelArg(maximumKernel, 0, Sizeof.cl_mem,
				Pointer.to(memObject[2]));

		// always use first knot
		knots.add(0);

		// init start and end
		final int leftOffset = 1;
		final int rightOffset = length - 2;
		final float fromX = xCoordinates[0];
		final float fromY = yCoordinates[0];
		final float toX = xCoordinates[length - 1];
		final float toY = yCoordinates[length - 1];

		// run recursive
		run(leftOffset, rightOffset, fromX, fromY, toX, toY);

		// always use last knot
		knots.add(length - 1);

		// Release kernel, program, and memory objects
		clReleaseMemObject(memObject[0]);
		clReleaseMemObject(memObject[1]);
		clReleaseMemObject(memObject[2]);
		clReleaseKernel(distanceKernel);
		clReleaseKernel(maximumKernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(queue);
		clReleaseContext(context);
	}

	private void run(final int leftOffset, final int rightOffset,
			final float fromX, final float fromY, final float toX,
			final float toY) {

		if (leftOffset <= rightOffset) {
			return;
		}

		// 1. compute distances
		clSetKernelArg(distanceKernel, 3, Sizeof.cl_uint,
				Pointer.to(new int[] { leftOffset }));
		clSetKernelArg(distanceKernel, 4, Sizeof.cl_float,
				Pointer.to(new float[] { fromX }));
		clSetKernelArg(distanceKernel, 5, Sizeof.cl_float,
				Pointer.to(new float[] { fromY }));
		clSetKernelArg(distanceKernel, 6, Sizeof.cl_float,
				Pointer.to(new float[] { toX }));
		clSetKernelArg(distanceKernel, 7, Sizeof.cl_float,
				Pointer.to(new float[] { toY }));

		final int length = rightOffset - leftOffset + 1;
		clEnqueueNDRangeKernel(queue, distanceKernel, 1, null,
				new long[] { length }, DEFAULT_LOCAL_WORKSIZE, 0, null, null);
		clEnqueueBarrier(queue);

		// 2. get maximum distance
		clSetKernelArg(maximumKernel, 1, Sizeof.cl_uint,
				Pointer.to(new int[] { leftOffset }));
		clSetKernelArg(maximumKernel, 2, Sizeof.cl_uint,
				Pointer.to(new int[] { rightOffset }));

		long globalWorkSize = Integers.nextBinary(length);
		for (int pass = 0; globalWorkSize > 1; pass++) {
			clSetKernelArg(maximumKernel, 3, Sizeof.cl_uint,
					Pointer.to(new int[] { pass }));
			clEnqueueNDRangeKernel(queue, maximumKernel, 1, null,
					new long[] { globalWorkSize >>= 1 },
					DEFAULT_LOCAL_WORKSIZE, 0, null, null);
			clEnqueueBarrier(queue);
		}

		final float[] values = new float[length];
		clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, Sizeof.cl_float
				* leftOffset, Sizeof.cl_float * 2, Pointer.to(values), 0, null,
				null);

		// make sure all read operations are done before rerunning
		clEnqueueBarrier(queue);

		if (values[0] > epsilon) {
			final int position = (int) values[1];
			knots.add(position);
			run(leftOffset + 1, position - 1, fromX, fromY,
					xCoordinates[position], yCoordinates[position]);
			run(position + 1, rightOffset - 1, xCoordinates[position],
					yCoordinates[position], toX, toY);
		}
	}
}
