package util;

import java.util.ArrayList;

import de.q2web.gis.alg.la.ref.dijkstra.Vertex;

public class Geometry
{
	public static double distToInterpolated(final ArrayList<Vertex> trace, final int lineStartIndex, final int lineEndIndex, final int intermediateIndex)
	{
		// vector setup
		final double[] lineStart = trace.get(lineStartIndex).getCoordinates();
		final double[] lineEnd = trace.get(lineEndIndex).getCoordinates();
		final double[] intermPoint = trace.get(intermediateIndex).getCoordinates();

		//        System.out.println("from: (" + lineStartIndex + ") " + Arrays.toString(lineStart) + "\t inter (" + intermediateIndex + "): "+ Arrays.toString(intermPoint) + "\t (" + lineEndIndex + "): "+ Arrays.toString(lineEnd));

		// calculate interpolated point
		final double indexOffset = intermediateIndex - lineStartIndex;
		final double[] lineVec = { lineEnd[0] - lineStart[0], lineEnd[1] - lineStart[1] };
		final double relation = indexOffset / (lineEndIndex-lineStartIndex);
		lineVec[0] *= relation;
		lineVec[1] *= relation;
		final double[] interpolated = { lineStart[0] + lineVec[0],  lineStart[1] + lineVec[1]};
		final double[] distVector = {intermPoint[0] - interpolated[0], intermPoint[1] - interpolated[1]};

		// calculate the distance
		final double distance = Math.sqrt(distVector[0]*distVector[0] + distVector[1]*distVector[1]);

		return distance;
	}

	public double distToLine(final double[] lineStart, final double[] lineEnd, final double[] interPoint)
	{
		// vector setup
		final double[] lineVec = { lineEnd[0] - lineStart[0], lineEnd[1] - lineStart[1] };
		final double lineLength = Math.sqrt(lineVec[0] * lineVec[0] + lineVec[1] * lineVec[1]);
		lineVec[0] /= lineLength;
		lineVec[1] /= lineLength;
		final double[] orthVec = { -lineVec[1], lineVec[0] };

		final double[] pointDiff = { interPoint[0] - lineStart[0], interPoint[1] - lineStart[1] };

		// now solve the equation:
		// r*lineVec + s*orthVec = pointDiff

		// first adjust line indices
		final int top = (lineVec[0] != 0.0) ? 0 : 1;
		final int bot = (top + 1) % 2;

		// solve the linear equation system:
		// r*lineVec + s*orthVec = pointDiff
		final double c = -lineVec[bot] / lineVec[top];
		// s will be the distance from the point to the edge
		final double s = (pointDiff[bot] + c * pointDiff[top]) / (orthVec[bot] + c * orthVec[top]);
		// // r will indicate, whether or not the projection lies between the node points
		final double r = (pointDiff[top] - orthVec[top] * s) / lineVec[top];

		return s;
	}

}
