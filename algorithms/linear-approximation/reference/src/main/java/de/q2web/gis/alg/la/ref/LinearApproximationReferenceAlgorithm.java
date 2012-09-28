package de.q2web.gis.alg.la.ref;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.q2web.gis.alg.la.ref.dijkstra.DijkstraAlgorithm;
import de.q2web.gis.alg.la.ref.dijkstra.Edge;
import de.q2web.gis.alg.la.ref.dijkstra.Graph;
import de.q2web.gis.alg.la.ref.dijkstra.Vertex;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

/**
 * The Class LinearApproximationReferenceAlgorithm.
 */
public class LinearApproximationReferenceAlgorithm implements Algorithm {

	private static Logger LOGGER = LoggerFactory
			.getLogger(LinearApproximationReferenceAlgorithm.class);

	private static final int DEFAULT_WEIGHT = 1;

	/** The geometry. */
	private final Geometry geometry;

	/**
	 * Instantiates a new linear approximation reference algorithm.
	 * 
	 * @param geometry
	 *            the geometry
	 */
	public LinearApproximationReferenceAlgorithm(final Geometry geometry) {
		this.geometry = geometry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * double)
	 */
	@Override
	public List<Point> run(final List<Point> trace, final double epsilon) {

		final LinkedList<Vertex<Point>> polylineSimplification = getPolylineSimplification(
				trace, epsilon);

		final List<Point> simplifiedTrace = new LinkedList<Point>();

		for (final Vertex<Point> vertex : polylineSimplification) {
			simplifiedTrace.add(vertex.getValue());
		}

		return simplifiedTrace;
	}

	/**
	 * Gets the polyline simplification.
	 * 
	 * @param trace
	 *            the trace
	 * @param epsilon
	 *            the epsilon
	 * @return the polyline simplification
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private LinkedList<Vertex<Point>> getPolylineSimplification(
			final List<Point> trace, final double epsilon) {

		// create the nodes
		int vertexId = 1;
		final List<Vertex<Point>> vertices = new ArrayList<Vertex<Point>>(
				trace.size());
		for (final Point point : trace) {
			vertices.add(new Vertex<Point>(vertexId++, point));
		}

		// create the edges
		final List<Edge<Point>> edges = setupEdges(vertices, epsilon);

		// create shortest path through the trace
		final Graph graph = new Graph(trace, edges);
		final DijkstraAlgorithm<Point> dijkstra = new DijkstraAlgorithm<Point>(
				graph);
		dijkstra.execute(vertices.get(0));

		return dijkstra.getPath(vertices.get(vertices.size() - 1));
	}

	/**
	 * This method creates the edge list for a given list of nodes. In general,
	 * there exist edges between each two successive nodes. Additional edges are
	 * created between two nodes 'from' and 'to', if all nodes \in ]from; to[
	 * can be interpolated without an interpolation error exceeding epsilon
	 * 
	 * @param nodes
	 *            the nodes
	 * @param epsilon
	 *            the epsilon
	 * @return the list
	 */
	private List<Edge<Point>> setupEdges(final List<Vertex<Point>> nodes,
			final double epsilon) {

		int edgeId = 0;
		final List<Edge<Point>> edgeList = new ArrayList<Edge<Point>>();
		final int numberOfNodes = nodes.size();

		// connect each two successive nodes by an edge:
		for (int from = 0; from < numberOfNodes - 1; from++) {
			final Vertex<Point> vertexFrom = nodes.get(from);
			final Vertex<Point> vertexTo = nodes.get(from + 1);
			edgeList.add(new Edge<Point>(edgeId++, vertexFrom, vertexTo,
					DEFAULT_WEIGHT));
		}

		// 'lineStartIndex' and 'lineEndIndex' create virtual edge
		// calculate distance from intermediatePointIndex to this edge
		for (int lineStartIndex = 0; lineStartIndex < numberOfNodes - 2; lineStartIndex++) {
			for (int lineEndIndex = lineStartIndex + 2; lineEndIndex < numberOfNodes; lineEndIndex++) {
				double maxDelta = 0.0;
				for (int intermediatePointIndex = lineStartIndex + 1; intermediatePointIndex < lineEndIndex; intermediatePointIndex++) {
					Point point = nodes.get(intermediatePointIndex).getValue();
					Point lineStart = nodes.get(lineStartIndex).getValue();
					Point lineEnd = nodes.get(lineEndIndex).getValue();

					final double delta = geometry.distance(point, lineStart,
							lineEnd);
					LOGGER.trace("Distance from {} to{},{}, is {}", point,
							lineStart, lineEnd, delta);
					if (geometry.compare(delta, maxDelta) > 0) {
						maxDelta = delta;
					}
				}
				LOGGER.trace("Found maximum delta of {}", maxDelta);
				if (geometry.compare(maxDelta, epsilon) <= 0) {
					// final int weight = lineEndIndex - lineStartIndex - 1;
					final int weight = DEFAULT_WEIGHT;
					final Vertex<Point> start = nodes.get(lineStartIndex);
					final Vertex<Point> end = nodes.get(lineEndIndex);

					LOGGER.trace("Connecting {} with {}, weight {}", start,
							end, weight);
					edgeList.add(new Edge<Point>(edgeList.size(), start, end,
							weight));
				}
			}
		}

		return edgeList;
	}
}
