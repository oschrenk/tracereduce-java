package de.q2web.gis.alg.dp.opencl;

import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.util.ArrayList;
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
import de.q2web.jocl.util.Resources;

/**
 * The Class DouglasPeuckerAlgorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DouglasPeuckerRawOpenClAlgorithm implements Algorithm {

	private static final String OPENCL_EXTENSION = ".cl";

	private static final String KERNEL_DISTANCE_EUCLIDEAN_POINT_TO_LINE = "euclidean2dPointLineDistance";
	private static final String SOURCE = Resources
			.convertStreamToString(DouglasPeuckerRawOpenClAlgorithm.class
					.getResourceAsStream(KERNEL_DISTANCE_EUCLIDEAN_POINT_TO_LINE
							+ OPENCL_EXTENSION));

	/** The epsilon. */
	private double epsilon;

	private List<Integer> knots;
	private long global_work_size[];
	private long local_work_size[];
	private cl_mem memObject[];
	private float distances[];

	/**
	 * Instantiates a new douglas peucker algorithm.
	 * 
	 */
	public DouglasPeuckerRawOpenClAlgorithm() {
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		int length = trace.size();
		float[] srcArrayX = new float[length];
		float[] srcArrayY = new float[length];

		for (int i = 0; i < trace.size(); i++) {
			Point point = trace.get(i);
			srcArrayX[i] = (float) point.get(0);
			srcArrayY[i] = (float) point.get(1);
		}

		List<Integer> knots = run(srcArrayX, srcArrayY, (float) epsilon);

		List<Point> result = new ArrayList<Point>();

		for (Integer index : knots) {
			result.add(trace.get(index));
		}

		return result;
	}

	public List<Integer> run(float xCoordinates[], float yCoordinates[],
			float epsilon) {
		this.epsilon = epsilon;

		if (xCoordinates.length != yCoordinates.length) {
			throw new IllegalArgumentException(
					"Source arrays must be same length");
		}

		int length = xCoordinates.length;

		distances = new float[xCoordinates.length];

		Pointer xCoordinatesPointer = Pointer.to(xCoordinates);
		Pointer yCoordinatesPointer = Pointer.to(yCoordinates);
		Pointer distancesPointer = Pointer.to(distances);

		// OpenCl Preamble
		cl_platform_id platformId = Platforms.getPlatforms().get(0);
		cl_device_id deviceId = Devices.getDevices(platformId,
				CL_DEVICE_TYPE_GPU).get(0);
		cl_context context = Contexts.create(platformId, deviceId);
		cl_command_queue queue = CommandQueues.create(context, deviceId);
		cl_program program = Programs.createFromSource(context, SOURCE);
		cl_kernel kernel = Kernels.create(program,
				KERNEL_DISTANCE_EUCLIDEAN_POINT_TO_LINE);

		memObject = new cl_mem[3];
		memObject[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				xCoordinatesPointer, null);
		memObject[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				yCoordinatesPointer, null);
		memObject[2] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_float * length, null, null);

		// Set the arguments for the kernel
		clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObject[0]));
		clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObject[1]));
		clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memObject[2]));

		global_work_size = new long[] { length };
		local_work_size = new long[] { 1 };

		knots = new ArrayList<Integer>();
		// always use first knot
		knots.add(0);
		float fromX = xCoordinates[0];
		float fromY = yCoordinates[0];
		float toX = xCoordinates[length - 1];
		float toY = yCoordinates[length - 1];

		for (int i = 1; i <= length - 1; i++) {
			int knot = run(queue, kernel, xCoordinates, yCoordinates,
					distancesPointer, fromX, fromY, toX, toY);
			if (knot >= 1) {
				knots.add(knot);
				fromX = xCoordinates[knot];
				fromY = xCoordinates[knot];
			}
		}
		// FIXME split set of knots and rerun alg on both sets

		// always use last knot
		// knots.add(length - 1);

		// Release kernel, program, and memory objects
		clReleaseMemObject(memObject[0]);
		clReleaseMemObject(memObject[1]);
		clReleaseMemObject(memObject[2]);
		clReleaseKernel(kernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(queue);
		clReleaseContext(context);

		return knots;
	}

	private int run(cl_command_queue queue, cl_kernel kernel,
			float srcArrayX[], float srcArrayY[], Pointer dst, float fromX,
			float fromY, float toX, float toY) {

		// boundary points that form the line
		clSetKernelArg(kernel, 3, Sizeof.cl_float,
				Pointer.to(new float[] { (Float) fromX }));
		clSetKernelArg(kernel, 4, Sizeof.cl_float,
				Pointer.to(new float[] { (Float) fromY }));
		clSetKernelArg(kernel, 5, Sizeof.cl_float,
				Pointer.to(new float[] { (Float) toX }));
		clSetKernelArg(kernel, 6, Sizeof.cl_float,
				Pointer.to(new float[] { (Float) toY }));

		// execute the kernel
		clEnqueueNDRangeKernel(queue, kernel, 1, null, global_work_size,
				local_work_size, 0, null, null);

		// Read the output data
		clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, 0, srcArrayX.length
				* Sizeof.cl_float, dst, 0, null, null);
		return positionOfFirstKnotWithError(distances);
	}

	private int positionOfFirstKnotWithError(float[] dstArray) {

		for (int i = 0; i < dstArray.length; i++) {
			float f = dstArray[i];
			if (Math.abs(f) > epsilon) {
				return i;
			}
		}
		return -1;
	}

}
