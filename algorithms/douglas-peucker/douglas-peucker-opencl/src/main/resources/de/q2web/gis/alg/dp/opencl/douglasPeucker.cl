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