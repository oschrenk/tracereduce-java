/**
 *
 */
__kernel void euclidean2dPointLineDistance(
	__global const float *x,
	__global const float *y,
	__global float *distance,
	const float fromX,
	const float fromY,
	const float toX,
	const float toY
) {
	int gid = get_global_id(0);

	//  double normalLength = Math.sqrt((to.getX() - from.getX())
	//			* (to.getX() - from.getX()) + (to.getY() - from.getY())
	//			* (to.getY() - from.getY()));
	// return Math.abs(
	//			(point.getX() - from.getX()) * (to.getY() - from.getY())
	// 			-
	//			(point.getY() - from.getY())* (to.getX() - from.getX()))
	//			/ normalLength;

	float normalLength = sqrt( (toX - fromX) * (toX - fromX) + (toY - fromY) * (toY - fromY) );
	distance[gid] =

			  (x[gid] - fromX) * (toY - fromY)
			- (y[gid] - fromX) * (toY - fromY)
		/ normalLength;
}