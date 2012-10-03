/**
 *
 * d=|v^^·r|= (|(toX-fromX)(fromY-pointY)-(fromX-pointX)(toY-fromY)|)
 *            /(sqrt((toX-fromX)^2+(toY-fromY)^2))
 */
__kernel void euclidean2dPointLineDistance(
	__global const float *x,
	__global const float *y,
	__global float *distance,
	const uint offset,
	const float fromX,
	const float fromY,
	const float toX,
	const float toY
) {
	int tid = get_global_id(0) + offset;

	float nom = (toX-fromX)*(fromY-y[tid])-(fromX-x[tid])*(toY-fromY);
	float denom = (toX-fromX)*(toX-fromX)+(toY-fromY)*(toY-fromY);
	
	distance[tid] = fabs(nom)/ sqrt(denom);
}

__kernel void spherical2dPointLineDistance(
	__global const float *x,
	__global const float *y,
	__global float *distance,
	const uint offset,
	const float fromX,
	const float fromY,
	const float toX,
	const float toY
) {
	// earth volumetric mean radius in meter
	float radius = 6371000;

	int tid = get_global_id(0) + offset;

	float radX = radians(x[tid]);
	float radY = radians(y[tid]);

	float radFromX = radians(fromX);
	float radFromY = radians(fromY);
	float radToX = radians(toX);
	float radToY = radians(toY);

	// final Point aPrime = SphericalGeometry.toCartesian3d(lineStart);
	// final Point bPrime = SphericalGeometry.toCartesian3d(lineEnd);
	// final Point pPrime = SphericalGeometry.toCartesian3d(point);

	// toCartesian3d
	// final double[] c = {
	// 		Math.cos(Math.toRadians(p.get(0)))
	// 				* Math.cos(Math.toRadians(p.get(1))),
	// 		Math.cos(Math.toRadians(p.get(0)))
	// 				* Math.sin(Math.toRadians(p.get(1))),
	// 		Math.sin(Math.toRadians(p.get(0))) };
	// return new Point(p.getTime(), c);

	float4 aPrime = (float4)(
		radFromX * cos(radFromY),
		radFromX * sin(radFromY),
		sin(radFromX),
		0);

	float4 bPrime = (float4)(
		radToX * cos(radToY),
		radToX * sin(radToY),
		sin(radToX),
		0);

	float4 pPrime = (float4)(
		radX * cos(radY),
		radX * sin(radY),
		sin(radX),
		0);

	// distance2d
	// final Point n = Vector.cross(aPrime, bPrime);
	// final double sinPhi = Math.abs(Vector.dot(n, pPrime));
	// final double phi = Math.asin(sinPhi);
	// return radius * phi;

	// opencl naturally supports vector operations
	// when using cross 4th result component is defined as 0
	float4 n = cross(aPrime, bPrime);
	
	float sinPhi = fabs(dot(n, pPrime));
	float phi = asin(sinPhi);

	distance[tid] = fabs(radius * phi);
}

/**
 * Compute the maximum float in an array and find the position of the
 * minimum in the original array. The maximum can be found in
 * <code>io[leftOffset]</code> and the position at
 * <code>io[leftOffset+1]</code>
 *
 * <p><b>Warning</b>Overwrites the input array!
 *
 * @param io
 *			read/write float array
 * @param leftOffset
 *			left offset of where to start the search in the array
  * @param rightOffset
 *			right offset of where to end the search in the array
 * @param pass
 *			counts the passes through the outer loop in the host
 */
__kernel void maximumWithPositionAndOffsetFloat(
	__global float* io,
	const uint leftOffset,
	const uint rightOffset,
	const uint pass
) {
	uint left = (1 << (pass + 1)) * get_global_id(0) + leftOffset; 
	uint right = left + (1 << pass);
	uint length = rightOffset - leftOffset + 1;
	
	if (right < rightOffset && left < rightOffset) {
		if ( ((__global float*)io)[right] > ((__global float*)io)[left] ) {
			((__global float*)io)[left] = ((__global float*)io)[right];
			((__global float*)io)[left+1] = (pass == 0 || right == length - 1) ? right : io[right+1];
		} else {
			((__global float*)io)[left+1] = (pass == 0) ? left : io[left+1];
		}
	}
}


/**
 * Implementation of Dijkstra's Single-Source Shortest Path (SSSP) algorithm on
 * the GPU. 
 *
 * <p>
 * The basis of this implementation in the paper "Accelerating large graph 
 * algorithms on the GPU using CUDA" by Parwan Harish and P.J. Narayanan
 *
 * <p>
 * A graph is composed of vertices and edges that connect these vertices. Each
 * edge has a number associated with, also called a weight. This weight is the
 * cost of travelling the edge (think of it as the physical distance).
 *
 * <p>
 * <b>Construction</b>
 * Normally you would represent a graph G (V, E)(with V being a set vertices, 
 * E being a set of edges between these vertices ) as an adjacency matrix. But
 * given the  fact that we operate on a very sparse matrix, we implement 
 * adjacency lists via two arrays, <code>vertexArray</code>, with 
 * <code>size(vertexArray)=O(V)</code> and <code>edgeArray</code>, with 
 * <code>size(edgeArray)=O(E)</code>. 
 * 
 * The <code>vertexArray</code> describes all vertices that have outgoing edges.
 * As not all vertices have outgoing edges another array on the host should 
 * If there are vertices that don't have outgoing edges, an array on the host 
 * should be created to map the vertex id to its <code>vertexArray</code> index.
 * The <code>edgeArray</code> contains indexes pointing to the vertex array. 
 *
 * Thus uni-directional edges are realized.
 *
 * @author Dan Ginsburg <daniel.ginsburg@childrens.harvard.edu>
 *
 * Modified to compute the path and not only the costs
 *
 * @author Oliver Schrenk <oliver.schrenk@gmail.com>
 *
 * @param vertexArray
 *			each entry contains an index that points to <code>edgeArray</code>.
 *			The entry is the edge for that vertex.
 * @param edgeArray
 * @param maskArray
 * @param costArray
 * @param updatingCostArray
 * @param parentVertexArray
 			Stores the parent
 * @param vertexCount
 * @param edgeCount
 */
__kernel  void dijkstra_sssp1(
	__global uint *vertexArray,
	__global uint *edgeArray,
	__global uint *maskArray,
	__global uint *costArray,
	__global uint *updatingCostArray,
	__global uint *parentVertexArray,
	const uint vertexCount,
	const uint edgeCount
) {
    // access thread id
    int tid = get_global_id(0);
	int defaultWeight = 1;

    if ( maskArray[tid] != 0 )
    {
        maskArray[tid] = 0;

        int edgeStart = vertexArray[tid];
        int edgeEnd;
        if (tid + 1 < (vertexCount)) {
            edgeEnd = vertexArray[tid + 1];
        } else {
            edgeEnd = edgeCount;
        }

		for(int edge = edgeStart; edge < edgeEnd; edge++) {
			int nid = edgeArray[edge];
		
			if (updatingCostArray[nid] > (costArray[tid] + defaultWeight)) {
			    updatingCostArray[nid] = (costArray[tid] + defaultWeight);
			    parentVertexArray[nid] = tid; 
			}
		}
    }
}

/**
 * Implementation of Dijkstra's Single-Source Shortest Path (SSSP) algorithm on
 * the GPU. 
 *
 * <p>
 * This is the second kernel
 *
 * @author Dan Ginsburg <daniel.ginsburg@childrens.harvard.edu>
 *
 * @param maskArray
 * @param costArray
 * @param updatingCostArray
 */
__kernel  void dijkstra_sssp2(
	__global uint *maskArray,
	__global uint *costArray,
	__global uint *updatingCostArray
) {
    // access thread id
    int tid = get_global_id(0);

    if (costArray[tid] > updatingCostArray[tid]) {
        costArray[tid] = updatingCostArray[tid];
        maskArray[tid] = 1;
    }

    updatingCostArray[tid] = costArray[tid];
}

/**
 * Implementation of Dijkstra's Single-Source Shortest Path (SSSP) algorithm on
 * the GPU. 
 *
 * <p>
 * Buffer initialization. This step saves time transferring data from host to
 * the device.
 *
 * <ul>
 *  <li>set <code>maskArray[]</code> to <code>0</code></li>
 *  <li>set <code>maskArray[sourceVertex]</code> to <code>1</code></li>
 *  <li>set <code>costArray[]</code> to <code>INT_MAX</code></li>
 *  <li>set <code>costArray[sourceVertex]</code> to <code>0</code></li>
 *  <li>set <code>updatingCostArray[]</code> to <code>INT_MAX</code></li>
 *  <li>set <code>updatingCostArray[sourceVertex]</code> to <code>1</code></li>
 * </ul>
 *
 * @author Dan Ginsburg <daniel.ginsburg@childrens.harvard.edu>
 *
 * @param maskArray
 * @param costArray
 * @param updatingCostArray
 * @param sourceVertex
 * @param vertexCount
 */
__kernel void dijkstra_initialize(
	__global uint *maskArray,
	__global uint *costArray,
	__global uint *updatingCostArray,
	__global uint *parentVertexArray,
	const uint sourceVertex
) {
    // access thread id
    int tid = get_global_id(0);

	if (sourceVertex == tid) {
	    maskArray[tid] = 1;
	    costArray[tid] = 0;
	    updatingCostArray[tid] = 0;
	    parentVertexArray[tid] = tid;
	} else {
	    maskArray[tid] = 0;
	    costArray[tid] = INT_MAX;
	    updatingCostArray[tid] = INT_MAX;
	    parentVertexArray[tid] = INT_MIN;
	}
}