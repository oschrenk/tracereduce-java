/**
 *
 * d=|v^^·r|= (|(toX-fromX)(fromY-pointY)-(fromX-pointX)(toY-fromY)|)
 *            /(sqrt((toX-fromX)^2+(toY-fromY)^2))
 */
__kernel void euclidean2dPointLineDistance(
	__global const float *x,
	__global const float *y,
	__global float *distance,
	const uint leftOffset,
	const uint rightOffset,
	const float fromX,
	const float fromY,
	const float toX,
	const float toY
) {

	int tid = get_global_id(0);

	if (tid < leftOffset) {
		distance[tid] = -1;
		return;
	}
	
	if (tid > rightOffset) {
		distance[tid] = -1;
		return;
	}
	

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
 * Compute orthogonal bearing
 */
inline float orthodromeBearing(float lon1, float lat1, float lon2, float lat2) {
	float radLat1 = radians(lat1);
	float radLat2 = radians(lat2);

	float deltaLongitude = radians(lon2 - lon1);

	float y = sin(deltaLongitude) * cos(radLat2);
	float x = cos(radLat1) * sin(radLat2) - sin(radLat1)
			* cos(radLat2) * cos(deltaLongitude);

	return atan2(y, x);
}

inline float haversineDistance(float longitudeFrom, float latitudeFrom,
			float longitudeTo, float latitudeTo
) {
	float deltaLatitude = radians(latitudeFrom - latitudeTo);
	float deltaLongitude = radians((longitudeFrom - longitudeTo));

	float sinusHalfDeltaLatitude = sin(deltaLatitude / 2);
	float sinusHalfDeltaLongitude = sin(deltaLongitude / 2);

	float a = sinusHalfDeltaLatitude * sinusHalfDeltaLatitude
			+ cos(radians(latitudeFrom))
			* cos(radians(latitudeTo))
			* sinusHalfDeltaLongitude * sinusHalfDeltaLongitude;
	float c = 2 * atan2(sqrt(a), sqrt(1 - a));

	return 6371000 * c;
	}

__kernel void haversine2dPointLineDistance(
	__global const float *longitudeX,
	__global const float *latitudeY,
	__global float *distance,
	const uint leftOffset,
	const uint rightOffset,
	const float fromLongitudeX,
	const float fromLatitudeY,
	const float toLongitudeX,
	const float toLatitudeY
) {
	// earth volumetric mean radius in meter
	float radius = 6371000;

	int tid = get_global_id(0);

	if (tid < leftOffset) {
		distance[tid] = -1;
		return;
	}
	
	if (tid > rightOffset) {
		distance[tid] = -1;
		return;
	}
	

	
	float b12 = orthodromeBearing(fromLongitudeX, fromLatitudeY, toLongitudeX, toLatitudeY );
	float b13 = orthodromeBearing(fromLongitudeX, fromLatitudeY, longitudeX[tid], latitudeY[tid] );
	float d13 = haversineDistance(fromLongitudeX, fromLatitudeY, longitudeX[tid], latitudeY[tid] );
	
	float dt = asin( sin(d13 / radius) * sin(b13 - b12)  ) * radius;
	
	distance[tid] = fabs(dt);
}

/**
 * Compute the maximum float in an array and find the position of the
 * maximum in the original array. The maximum can be found in
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
	if (right <= rightOffset && left < rightOffset) {

		// HostCode
		// 	long globalWorkSize = Integers.nearestBinary(rightOffset - leftOffset +1 );
		// 	for (int pass = 0; globalWorkSize > 1; pass++) {
		// 		clSetKernelArg(kernel, 3, Sizeof.cl_uint,
		// 				Pointer.to(new int[] { pass }));
		// 		clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[] {globalWorkSize >>= 1},
		// 				localWorkSize, 0, null, null);
		// 		clEnqueueBarrier(queue);
		// 	}

		// Example:

		// Input:
		//	float max = 90; leftOffset = 2, rightOffset = 3
		//	float[] floats = { 70f, 60f, max, 40f, 50f, 80f };
		
		// pass = 0
		// globalWorkSize = 2
		// io = { 70, 60, 90, 40, 50, 80 }
		// pass = 0, globalWorkSize = 2
		//
		//	left  = (1 << (pass + 1)) * globalId + leftOfsset;
		//	              = (1 << (0 + 1)) * globalId;
		// 	              = 2 * globalId + 2 ; with globalId in {0..1}
		//	=> left is {2, 4}
		//	right = left + (1 << pass);
		//	              = {2, 4} + (1 << 0);
		//	              = {2, 4} + 1;
		//	=> right is {3, 5}
		//
		// 	left:2, right:3
		//			=> 90 > 40
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
		if ( ((__global float*)io)[right] > ((__global float*)io)[left] ) {
			((__global float*)io)[left] = ((__global float*)io)[right];
			((__global float*)io)[left+1] = (pass == 0 || right == rightOffset - leftOffset - 1) ? right : io[right+1];
		} else {
			((__global float*)io)[left+1] = (pass == 0) ? left : io[left+1];
		}

	}
}