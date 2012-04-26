package de.q2web.gis.alg.la.ref;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.Geometry;

import de.q2web.gis.alg.la.ref.dijkstra.DijkstraAlgorithm;
import de.q2web.gis.alg.la.ref.dijkstra.Edge;
import de.q2web.gis.alg.la.ref.dijkstra.Graph;
import de.q2web.gis.alg.la.ref.dijkstra.Vertex;
import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Point;

public class ReferenceLinearApproximation<P extends Number> implements
Algorithm<P> {

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
	public List<Point<P>> run(final List<Point<P>> points, final P epsilon) {

		// TODO Algorithm<P>.run()
		return null;
	}

	public LinkedList<Vertex> getPolylineSimplification() {
		// create the edges
		edges = setupEdgesBruteForce();

		// create shortest path through the trace
		final Graph graph = new Graph(nodes, edges);
		final DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(nodes.get(0));
		return dijkstra.getPath(nodes.get(nodes.size() - 1));
	}

	/**
	 * This method creates the edge list for a given list of nodes. In general,
	 * there exist edges between each two successive nodes. Additional edges are
	 * created between two nodes 'from' and 'to', if all nodes \in ]from; to[
	 * can be interpolated without an interpolation error exceeding epsilon
	 * 
	 * @return
	 */
	private ArrayList<Edge> setupEdgesBruteForce() {
		final ArrayList<Edge> edgeList = new ArrayList<Edge>();
		final int numberOfNodes = nodes.size();

		// connect each two successive nodes by an edge:
		for (int from = 0; from < numberOfNodes - 1; from++) {
			edgeList.add(new Edge("" + edgeList.size(), nodes.get(from), nodes
					.get(from + 1), 1));
		}

		// the pointer 'from' and 'to' create a virtual edge and
		// the pointer 'inter' checks for every point interpolated
		// by this edge (i.e. inter \in ]from;to[ ), whether the
		// interpolation error bound (epsilon) is violated by the
		// interpolation distance (delta).
		for (int from = 0; from < numberOfNodes - 2; from++) {
			for (int to = from + 2; to < numberOfNodes - 1; to++) {
				double maxDelta = 0.0;
				for (int inter = from + 1; inter < to; inter++) {
					final double delta = Geometry.distToInterpolated(nodes,
							from, to, inter);
					if (delta > maxDelta) {
						maxDelta = delta;
						// System.out.println("   inter: " +
						// this.nodes.get(inter) + "\t delta: " + delta);
					}
				}
				if (maxDelta <= epsilon) {
					edgeList.add(new Edge("" + edgeList.size(),
							nodes.get(from), nodes.get(to), 1));
					// System.out.println("from: " + this.nodes.get(from) +
					// "\t to: " + this.nodes.get(to) + "\t maxDelta: " +
					// maxDelta);
				}
			}
		}

		return edgeList;
	}
}
