package de.q2web.gis.alg.la.jgrapht;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.GraphIterator;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

;

public class JGraphTLinearAppoximationAlgorithm<P extends Number> implements
		Algorithm<P> {

	private final Geometry<P> geometry;

	public JGraphTLinearAppoximationAlgorithm(Geometry<P> geometry) {
		super();
		this.geometry = geometry;
	}

	private DirectedGraph<Point<P>, IntegerWeightEdge> graph;

	@Override
	public List<Point<P>> run(List<Point<P>> points, P epsilon) {
		if (points.size() < 3) {
			return points;
		}

		graph = getVerticesOnlyGraph(points);
		addPossibleEdges(points, epsilon);

		return getLongestPath(graph);
	}

	private void addPossibleEdges(List<Point<P>> points, P epsilon) {
		final int numberOfPoints = points.size();

		for (int i = 0; i < numberOfPoints; i++) {
			for (int j = i + 1; j < numberOfPoints; j++) {
				addPossibleEdge(points, i, j, epsilon);
			}
		}

	}

	private void addPossibleEdge(List<Point<P>> points, int i, int j, P epsilon) {
		final Point<P> iPoint = points.get(i);
		final Point<P> jPoint = points.get(i);

		for (int k = i; k < j; k++) {
			if (geometry.compare(
					geometry.distance(points.get(k), points.get(i),
							points.get(j)), epsilon) > 0) {
				return;
			}
		}

		graph.addEdge(iPoint, jPoint, new IntegerWeightEdge(j - i - 1));
	}

	private DirectedGraph<Point<P>, IntegerWeightEdge> getVerticesOnlyGraph(
			List<Point<P>> points) {
		final DirectedGraph<Point<P>, IntegerWeightEdge> graph = new SimpleDirectedWeightedGraph<Point<P>, IntegerWeightEdge>(
				IntegerWeightEdge.class);

		for (Point<P> point : points) {
			graph.addVertex(point);
		}

		return graph;
	}

	private List<Point<P>> getLongestPath(
			DirectedGraph<Point<P>, IntegerWeightEdge> directedGraph) {
		GraphIterator<Point<P>, IntegerWeightEdge> graphIterator = new LongestPathIterator<Point<P>, IntegerWeightEdge>(
				directedGraph);
		List<Point<P>> points = new LinkedList<Point<P>>();
		while (graphIterator.hasNext()) {
			points.add(graphIterator.next());
		}
		return points;
	}
}
