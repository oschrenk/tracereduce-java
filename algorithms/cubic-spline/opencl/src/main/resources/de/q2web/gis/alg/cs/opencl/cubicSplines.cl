__kernel void cubicSpline(
	__global const float *x,
	__global const float *y,
	__global float *distance,
	__global const uint *excludes,
	const uint length
) {
	uint tid = get_global_id(0);

	if (tid <= 0 ) {
		return;
	}

	if (tid >= length-1 ) {
		return;
	}

	// initialize variables
	float x0 = 0, y0 = 0, xa, ya, xb, yb, xc = 0, yc = 0;
	float fPrimeLeft;
	float fPrimeRight;
	float fDoublePrimeLeft;
	float fDoublePrimeRight;

	int i = tid;
	int j = 1;
	int k = 2;
	int g = i - 2;
	int h = i - 1;

	// case iii)
	while (excludes[h] && h >= 1) {
		h = h - 1;
		g = g - 1;
	}
	// case iii, b)
	while (h > 0 && excludes[g] && g > 1) {
		g = g - 1;
	}

	// make sure that i (=tid) is excluded by adding
	j = i + 1;
	k = j + 1;

	// case ii) excludeId == xRight
	while (excludes[j] && k < (length - 1)) {
		j = j + 1;
		k = j + 1;
	}
	// because of case iii, we might have reached left border,
	// if check == false, left border is not reached , so we can set x0
	if (j > 1 && g > 0) {
			x0 = x[g];
			y0 = y[g];
	}
	xa = x[h];
	ya = y[h];
	xb = x[j];
	yb = y[j];

	// case ii,b)
	while (j < length - 1 && excludes[k] && k < length - 1) {
		k = k + 1;
	}
	// if evaluates to true than right border not reached => we can
	// access x[k], y[k]
	if (j < (length - 1)) {
		xc = x[k];
		yc = y[k];
	}
	fPrimeRight = 2 / ((xc - xb) / (yc - yb) + (xb - xa) / (yb - ya));

	// if check == true, left border is reached => change formula of
	// prime left
	fPrimeLeft = (j > 1 && h == 0) ? 3 / 2 * (yb - ya) / (xb - xa)
			- fPrimeRight / 2 : 2 / ((xb - xa) / (yb - ya) + (xa - x0)
			/ (ya - y0));

	// formula of prime right changes if right border is reached
	if (j == length) {
		fPrimeRight = 3 / 2 * ((yb - ya) / (xb - xa)) - fPrimeLeft
				/ 2;
	}

	fDoublePrimeLeft = -(2 * (fPrimeRight + 2 * fPrimeLeft))
			/ (xb - xa) + (6 * (yb - ya) / (xb - xa)) / (xb - xa);
	fDoublePrimeRight = 2 * (2 * fPrimeRight + fPrimeLeft) / (xb - xa)
			- 6 * (yb - ya) / (xb - xa) / (xb - xa);

	// it might be the case that instead of i, j and k is used
	// it is still ok to write the results to a,b,c,d as in the next
	// iteration the next i is tried, which in turn migth also be passed
	// on, because it is being excluded, that is ok because it will
	// store the same result
	float d = 1 / 6 * (fDoublePrimeRight - fDoublePrimeLeft) / (xb - xa);
	float c = 1 / 2 * (xb * fDoublePrimeLeft - xa * fDoublePrimeRight)
			/ (xb - xa);
	float b = ((yb - ya) - c * (xb * xb - xa * xa) - d
			* (xb * xb * xb - xa * xa * xa))
			/ (xb - xa);
	float a = ya - b * xa - c * xa * xa - d * xa * xa * xa;

	float yy = a + b * x[tid]
			+ c * x[tid] * x[tid]
			+ d * x[tid] * x[tid]
			* x[tid];
	distance[tid - 1] = fabs(y[tid] - yy);

}

/**
 * Compute the minimum float in an array and find the position of the
 * minimum in the original array. The minimum can be found in
 * <code>buffer[0]</code> and the position at
 * <code>buffer[ceil(log_2(buffer.length))]</code>
 *
 * <p><b>Warning</b>Overwrites the input array!
 *
 * @param io
 *			read/write float array
 * @param length
 *			length of input array
 * @param pass
 *			counts the passes through the outer loop in the host
 */
__kernel void minimumWithPositionFloat(
	__global float* io,
	const uint length,
	const uint pass
) {
	uint left = (1 << (pass + 1)) * get_global_id(0);
	uint right = left + (1 << pass);
	if (right < length && left < length) {

		// HostCode
		// 	long globalWorkSize = Integers.nearestBinary(length);
		// 	for (int pass = 0; globalWorkSize > 1; pass++) {
		// 		clSetKernelArg(kernel, 2, Sizeof.cl_uint,
		// 				Pointer.to(new int[] { pass }));
		// 		clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[] {globalWorkSize >>= 1},
		// 				localWorkSize, 0, null, null);
		// 		clEnqueueBarrier(queue);
		// 	}

		// Example:

		// Input:
		//	float min = 10;
		//	float[] floats = { 70f, 60f, min, 40f, 50f, 80f };

		// pass = 0
		// globalWorkSize = 4
		// io = { 70, 60, 10, 40, 50, 80 }, length = 6
		// pass = 0, globalWorkSize = 4
		//
		//	left  = (1 << (pass + 1)) * globalId;
		//	              = (1 << (0 + 1)) * globalId;
		// 	              = 2 * globalId; with globalId in {0..3}
		//	=> left is {0, 2, 4, 6}
		//	right = left + (1 << pass);
		//	              = {0, 2, 4, 6} + (1 << 0);
		//	              = {0, 2, 4, 6} + 1;
		//	=> right is {1, 3, 5, 7}
		//
		// 	left:0, right:1
		//			=> 70 > 60
		//			=> io[0] = io[1] = 60
		//			   io[1] = right = 1
		// 	left:2, right:3
		//			=> 10 < 40
		//			=> io[3] = left = 2
		// 	left:4, right:5
		//			=> 50 < 80
		//			=> io[5] = left = 4
		// 	left:6, right:7 is out of bound => ignore
		//
		// pass = 1
		// globalWorkSize = 2
		// io = { 60, 1, 10, 2, 50, 4 };
		//
		// left  = (1 << (pass + 1)) * globalId;
		//       = 4 * globalId
		//       = {0, 4}
		// right = left + (2);
		//       = 4 * globalId + 2
		//       = {2, 6}
		// 	left:0, right:2
		//			=> 60 > 10
		//			=> io[0] = 10
		//			=> io[1]

		// 	left:0, right:1

		//
		// pass = 2
		// globalWorkSize = 1
		// io = { 60, 1, 10, 2, 50, 4 };

		//
		if ( ((__global float*)io)[right] < ((__global float*)io)[left] ) {
			((__global float*)io)[left] = ((__global float*)io)[right];
			((__global float*)io)[left+1] = (pass == 0 || right == length - 1) ? right : io[right+1];
		} else {
			((__global float*)io)[left+1] = (pass == 0) ? left : io[left+1];
		}

	}
}