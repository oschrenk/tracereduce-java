package de.q2web.gis.alg.lo.opencl;

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

import java.util.Arrays;
import java.util.Collections;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Ints;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Point;
import de.q2web.jocl.util.Integers;
import de.q2web.jocl.util.Resources;

public class LinearOptimumOpenClAlgorithm implements Algorithm {

	private static final int DEFAULT_SOURCE_ID = 0;

	private static Logger LOGGER = LoggerFactory
			.getLogger(LinearOptimumOpenClAlgorithm.class);

	private static final long[] DEFAULT_LOCAL_WORKSIZE = new long[] { 1 };

	static final String SOURCE = Resources
			.convertStreamToString(LinearOptimumOpenClAlgorithm.class
					.getResourceAsStream("linearOptimum.cl"));

	public static final String KERNEL_CROSSTRACK_EUCLIDEAN = "euclidean2dPointLineDistance";
	public static final String KERNEL_CROSSTRACK_SPHERICAL = "spherical2dPointLineDistance";

	private static final String KERNEL_MAXIMUM = "maximumWithPositionAndOffsetFloat";
	private static final String DIJKSTRA_INITIALIZE = "dijkstra_initialize";
	private static final String DIJKSTRA_SSSP1 = "dijkstra_sssp1";
	private static final String DIJKSTRA_SSSP2 = "dijkstra_sssp2";

	private cl_mem memObject[];
	private cl_command_queue queue;
	private cl_kernel distanceKernel;
	private cl_kernel maximumKernel;
	private cl_kernel dijkstraInitializationKernel;
	private cl_kernel sssp1Kernel = null;
	private cl_kernel sssp2Kernel = null;

	private float[] xCoordinates;
	private float[] yCoordinates;
	private int[] vertexArray;

	private double epsilon;

	private final String crossTrackMetric;

	private List<Point> trace;

	public LinearOptimumOpenClAlgorithm(final String crossTrackMetric) {
		this.crossTrackMetric = crossTrackMetric;

		if (!crossTrackMetric.equals(KERNEL_CROSSTRACK_EUCLIDEAN)) {
			if (!crossTrackMetric.equals(KERNEL_CROSSTRACK_SPHERICAL)) {
				throw new IllegalArgumentException("No valid distance kernel");
			}
		}
	}

	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		this.trace = trace;
		this.epsilon = epsilon;

		// init arrays
		final int length = trace.size();
		xCoordinates = new float[length];
		yCoordinates = new float[length];
		vertexArray = new int[length];

		for (int i = 0; i < trace.size(); i++) {
			final Point point = trace.get(i);
			xCoordinates[i] = (float) point.get(0);
			yCoordinates[i] = (float) point.get(1);
		}

		// get results
		return run();
	}

	private List<Point> run() {

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

		// Create Kernels
		distanceKernel = Kernels.create(program, crossTrackMetric);
		maximumKernel = Kernels.create(program, KERNEL_MAXIMUM);
		dijkstraInitializationKernel = Kernels.create(program,
				DIJKSTRA_INITIALIZE);
		sssp1Kernel = Kernels.create(program, DIJKSTRA_SSSP1);
		sssp2Kernel = Kernels.create(program, DIJKSTRA_SSSP2);

		final Pointer xCoordinatesPointer = Pointer.to(xCoordinates);
		final Pointer yCoordinatesPointer = Pointer.to(yCoordinates);

		memObject = new cl_mem[3];
		// x coordinates
		memObject[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				xCoordinatesPointer, null);
		// y coordinates
		memObject[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_float * length,
				yCoordinatesPointer, null);
		// distance
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

		// setup edges
		// 'lineStartIndex' and 'lineEndIndex' create virtual edge
		// calculate distance from intermediatePointIndex to this edge
		List<Integer> edges = new LinkedList<Integer>();
		int edgeCount = 0;
		for (int lineStartIndex = 0; lineStartIndex < length - 2; lineStartIndex++) {
			vertexArray[lineStartIndex] = edgeCount;
			// add neighbor as edge
			edges.add(lineStartIndex + 1);
			edgeCount++;

			for (int lineEndIndex = lineStartIndex + 2; lineEndIndex < length; lineEndIndex++) {
				// for (int intermediatePointIndex = lineStartIndex + 1;
				// intermediatePointIndex < lineEndIndex;
				// intermediatePointIndex++) { //}
				final float fromX = xCoordinates[lineStartIndex];
				final float fromY = yCoordinates[lineStartIndex];
				final float toX = xCoordinates[lineEndIndex];
				final float toY = yCoordinates[lineEndIndex];

				final boolean addEdge = isValidEdge(lineStartIndex,
						lineEndIndex, fromX, fromY, toX, toY);

				if (addEdge) {
					edges.add(lineEndIndex);
					edgeCount++;
				}
			}
		}

		// connect the prior to last edge with last
		vertexArray[length - 2] = edgeCount;
		edges.add(length - 1);

		int[] edgeArray = Ints.toArray(edges);

		LOGGER.trace("VertexArray {}", Arrays.toString(vertexArray));
		LOGGER.trace("EdgeArray {}", Arrays.toString(edgeArray));

		// set last vertex array to 0,
		// it would be a cyclic but as the last vertex is always the target
		// there is no harm
		vertexArray[length - 1] = 0;

		// clean up after distance kernel
		clReleaseMemObject(memObject[0]);
		clReleaseMemObject(memObject[1]);
		clReleaseMemObject(memObject[2]);
		clReleaseKernel(distanceKernel);
		clReleaseKernel(maximumKernel);

		// *********************************
		// RUN DIJKSTRA
		// *********************************

		int vertexCount = vertexArray.length;
		final Pointer vertexArrayPointer = Pointer.to(vertexArray);
		final Pointer edgeArrayPointer = Pointer.to(edgeArray);

		memObject = new cl_mem[6];
		// __global uint *vertexArray
		memObject[0] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * vertexArray.length,
				vertexArrayPointer, null);
		// __global uint *edgeArray
		memObject[1] = clCreateBuffer(context, CL_MEM_READ_ONLY
				| CL_MEM_COPY_HOST_PTR, Sizeof.cl_uint * edgeCount,
				edgeArrayPointer, null);
		// __global uint *maskArray
		memObject[2] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_uint * vertexCount, null, null);
		// __global float *costArray,
		memObject[3] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_uint * vertexCount, null, null);
		// __global float *updatingCostArray,
		memObject[4] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_uint * vertexCount, null, null);
		// __global uint *parentVertexArray,
		memObject[5] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_uint * vertexCount, null, null);

		List<Point> simplifiedTrace = runDijkstra(vertexArray.length,
				edgeArray.length);

		// Release kernel, program, and memory objects
		clReleaseMemObject(memObject[0]);
		clReleaseMemObject(memObject[1]);
		clReleaseMemObject(memObject[2]);
		clReleaseMemObject(memObject[3]);
		clReleaseMemObject(memObject[4]);
		clReleaseMemObject(memObject[5]);

		clReleaseKernel(dijkstraInitializationKernel);
		clReleaseKernel(sssp1Kernel);
		clReleaseKernel(sssp2Kernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(queue);
		clReleaseContext(context);

		return simplifiedTrace;
	}

	private boolean isValidEdge(final int leftOffset, final int rightOffset,
			final float fromX, final float fromY, final float toX,
			final float toY) {

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

		// connect edge?
		return (values[0] <= epsilon);
	}

	private List<Point> runDijkstra(final int vertexCount, final int edgeCount) {

		// Initialization
		//
		// __global int *maskArray,
		clSetKernelArg(dijkstraInitializationKernel, 0, Sizeof.cl_mem,
				Pointer.to(memObject[2]));
		// __global float *costArray,
		clSetKernelArg(dijkstraInitializationKernel, 1, Sizeof.cl_mem,
				Pointer.to(memObject[3]));
		// __global float *updatingCostArray,
		clSetKernelArg(dijkstraInitializationKernel, 2, Sizeof.cl_mem,
				Pointer.to(memObject[4]));
		// __global uint *parentVertexArray,
		clSetKernelArg(dijkstraInitializationKernel, 3, Sizeof.cl_mem,
				Pointer.to(memObject[5]));
		// int sourceVertexId
		clSetKernelArg(dijkstraInitializationKernel, 4, Sizeof.cl_uint,
				Pointer.to(new int[] { DEFAULT_SOURCE_ID }));

		final long[] globalWorkSize = new long[] { vertexCount };
		clEnqueueNDRangeKernel(queue, dijkstraInitializationKernel, 1, null,
				globalWorkSize, DEFAULT_LOCAL_WORKSIZE, 0, null, null);

		final int[] maskArray = new int[vertexCount];
		final Pointer maskArrayPointer = Pointer.to(maskArray);
		do {
			// Enqueue Kernel SSSP 1
			//
			// __global int *vertexArray,
			clSetKernelArg(sssp1Kernel, 0, Sizeof.cl_mem,
					Pointer.to(memObject[0]));
			// __global float *edgeArray,
			clSetKernelArg(sssp1Kernel, 1, Sizeof.cl_mem,
					Pointer.to(memObject[1]));
			// __global int *maskArray,
			clSetKernelArg(sssp1Kernel, 2, Sizeof.cl_mem,
					Pointer.to(memObject[2]));
			// __global float *costArray,
			clSetKernelArg(sssp1Kernel, 3, Sizeof.cl_mem,
					Pointer.to(memObject[3]));
			// __global float *updatingCostArray,
			clSetKernelArg(sssp1Kernel, 4, Sizeof.cl_mem,
					Pointer.to(memObject[4]));
			// __global uint *parentVertexArray,
			clSetKernelArg(sssp1Kernel, 5, Sizeof.cl_mem,
					Pointer.to(memObject[5]));
			// int vertexCount
			clSetKernelArg(sssp1Kernel, 6, Sizeof.cl_uint,
					Pointer.to(new int[] { vertexCount }));
			// int edgeCount
			clSetKernelArg(sssp1Kernel, 7, Sizeof.cl_uint,
					Pointer.to(new int[] { edgeCount }));
			//
			clEnqueueNDRangeKernel(queue, sssp1Kernel, 1, null, globalWorkSize,
					DEFAULT_LOCAL_WORKSIZE, 0, null, null);

			// Enqueue Kernel SSSP 2
			// __global int *maskArray,
			clSetKernelArg(sssp2Kernel, 0, Sizeof.cl_mem,
					Pointer.to(memObject[2]));
			// __global float *costArray,
			clSetKernelArg(sssp2Kernel, 1, Sizeof.cl_mem,
					Pointer.to(memObject[3]));
			// __global float *updatingCostArray
			clSetKernelArg(sssp2Kernel, 2, Sizeof.cl_mem,
					Pointer.to(memObject[4]));

			clEnqueueNDRangeKernel(queue, sssp2Kernel, 1, null, globalWorkSize,
					DEFAULT_LOCAL_WORKSIZE, 0, null, null);

			// read mask array
			clEnqueueReadBuffer(queue, memObject[2], CL_TRUE, 0, Sizeof.cl_uint
					* vertexCount, maskArrayPointer, 0, null, null);
		} while (!isEmpty(maskArray));

		// read parent vertex array
		final int[] parentVertexArray = new int[vertexCount];
		final Pointer parentVertexArrayPointer = Pointer.to(parentVertexArray);
		clEnqueueReadBuffer(queue, memObject[5], CL_TRUE, 0, Sizeof.cl_uint
				* vertexCount, parentVertexArrayPointer, 0, null, null);

		return getPath(parentVertexArray);
	}

	private List<Point> getPath(final int[] parentVertexArray) {
		List<Point> path = new LinkedList<Point>();

		int currentParent = parentVertexArray.length - 1;

		// add target
		path.add(trace.get(currentParent));

		// add chain
		while ((currentParent = parentVertexArray[currentParent]) != 0) {
			path.add(trace.get(currentParent));
		}

		// add source
		path.add(trace.get(0));
		Collections.reverse(path);
		return path;
	}

	private boolean isEmpty(final int[] ints) {
		for (final int i : ints) {
			if (i != 0) {
				return false;
			}
		}
		return true;
	}
}
