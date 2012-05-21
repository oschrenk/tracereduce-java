package de.q2web.gis.alg.la.ref;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.q2web.gis.alg.la.ref.dijkstra.DijkstraAlgorithm;
import de.q2web.gis.alg.la.ref.dijkstra.Edge;
import de.q2web.gis.alg.la.ref.dijkstra.Graph;
import de.q2web.gis.alg.la.ref.dijkstra.Vertex;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

public class ReferenceLinearApproximation implements Algorithm {

	/** The geometry. */
	private final Geometry geometry;

	public ReferenceLinearApproximation(final Geometry geometry) {
		this.geometry = geometry;
	}

	/*
	 * @see de.q2web.gis.trajectory.core.api.Algorithm#run(java.util.List,
	 * java.lang.Number)
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinkedList<Vertex<Point>> getPolylineSimplification(
			final List<Point> trace, final double epsilon) {

		// create the nodes
		int vertexId = 1;
		final List<Vertex<Point>> vertices = new ArrayList<Vertex<Point>>(
				trace.size());
		for (final Point point : trace) {
			vertices.add(new Vertex<Point>(vertexId++, point));
		}

		// create the edges
		final List<Edge<Point>> edges = setupEdgesBruteForce(vertices, epsilon);

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
	 * @return
	 */
	private List<Edge<Point>> setupEdgesBruteForce(
			final List<Vertex<Point>> nodes, final double epsilon) {

		int edgeId = 0;
		final List<Edge<Point>> edgeList = new ArrayList<Edge<Point>>();
		final int numberOfNodes = nodes.size();

		// connect each two successive nodes by an edge:
		for (int from = 0; from < numberOfNodes - 1; from++) {
			final Vertex<Point> vertexFrom = nodes.get(from);
			final Vertex<Point> vertexTo = nodes.get(from + 1);
			edgeList.add(new Edge<Point>(edgeId++, vertexFrom, vertexTo));
		}

		// the pointer 'lineStartIndex' and 'lineEndIndex' create a virtual edge
		// and
		// the pointer 'intermediatePointIndex' checks for every point
		// interpolated
		// by this edge (i.e. inter \in ]from;to[ ), whether the
		// interpolation error bound (epsilon) is violated by the
		// interpolation distance (delta).
		for (int lineStartIndex = 0; lineStartIndex < numberOfNodes - 2; lineStartIndex++) {
			for (int lineEndIndex = lineStartIndex + 2; lineEndIndex < numberOfNodes - 1; lineEndIndex++) {
				double maxDelta = 0.0;
				for (int intermediatePointIndex = lineStartIndex + 1; intermediatePointIndex < lineEndIndex; intermediatePointIndex++) {
					final double delta = geometry.distance(
							nodes.get(intermediatePointIndex).getValue(), nodes
							.get(lineStartIndex).getValue(),
							nodes.get(lineEndIndex).getValue());

					if (geometry.compare(delta, maxDelta) > 0) {
						maxDelta = delta;
					}
				}
				if (geometry.compare(maxDelta, epsilon) <= 0) {
					edgeList.add(new Edge<Point>(edgeList.size(), nodes
							.get(lineStartIndex), nodes.get(lineEndIndex)));
				}
			}
		}

		return edgeList;
	}
}
