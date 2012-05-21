package de.q2web.gis.alg.la.jgrapht;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.GraphIterator;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

public class JGraphTLinearAppoximationAlgorithm implements Algorithm {

	private final Geometry geometry;

	public JGraphTLinearAppoximationAlgorithm(final Geometry geometry) {
		super();
		this.geometry = geometry;
	}

	private DirectedGraph<Point, IntegerWeightEdge> graph;

	@Override
	public List<Point> run(final List<Point> points, final double epsilon) {
		if (points.size() < 3) {
			return points;
		}

		graph = getVerticesOnlyGraph(points);
		addPossibleEdges(points, epsilon);

		return getLongestPath(graph);
	}

	private void addPossibleEdges(final List<Point> points, final double epsilon) {
		final int numberOfPoints = points.size();

		for (int i = 0; i < numberOfPoints; i++) {
			for (int j = i + 1; j < numberOfPoints; j++) {
				addPossibleEdge(points, i, j, epsilon);
			}
		}
	}

	private void addPossibleEdge(final List<Point> points, final int i,
			final int j, final double epsilon) {
		final Point iPoint = points.get(i);
		final Point jPoint = points.get(i);

		for (int k = i; k < j; k++) {
			if (geometry.compare(
					geometry.distance(points.get(k), points.get(i),
							points.get(j)), epsilon) > 0) {
				return;
			}
		}

		graph.addEdge(iPoint, jPoint, new IntegerWeightEdge(j - i - 1));
	}

	private DirectedGraph<Point, IntegerWeightEdge> getVerticesOnlyGraph(
			final List<Point> points) {
		final DirectedGraph<Point, IntegerWeightEdge> graph = new SimpleDirectedWeightedGraph<Point, IntegerWeightEdge>(
				IntegerWeightEdge.class);

		for (final Point point : points) {
			graph.addVertex(point);
		}

		return graph;
	}

	private List<Point> getLongestPath(
			final DirectedGraph<Point, IntegerWeightEdge> directedGraph) {
		final GraphIterator<Point, IntegerWeightEdge> graphIterator = new LongestPathIterator<Point, IntegerWeightEdge>(
				directedGraph);
		final List<Point> points = new LinkedList<Point>();
		while (graphIterator.hasNext()) {
			points.add(graphIterator.next());
		}
		return points;
	}
}
