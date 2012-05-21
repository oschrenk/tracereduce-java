package de.q2web.gis.alg.la.jung;

import java.util.Collections;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class JungLinearAppoximationAlgorithm implements Algorithm {

	private final Geometry geometry;

	private Graph<Point, IntegerWeightEdge> graph;

	public JungLinearAppoximationAlgorithm(final Geometry geometry) {
		super();
		this.geometry = geometry;
	}

	@Override
	public List<Point> run(final List<Point> points, final double epsilon) {
		if (points.size() < 3) {
			return points;
		}

		graph = getVerticesOnlyGraph(points);
		addPossibleEdges(points, epsilon);

		final Point start = points.get(0);
		final Point end = points.get(points.size());

		return getLongestPath(graph, start, end);
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

		graph.addEdge(new IntegerWeightEdge(j - i - 1), iPoint, jPoint);
	}

	private Graph<Point, IntegerWeightEdge> getVerticesOnlyGraph(
			final List<Point> points) {
		final Graph<Point, IntegerWeightEdge> graph = new DirectedSparseGraph<Point, IntegerWeightEdge>();

		for (final Point point : points) {
			graph.addVertex(point);
		}

		return graph;
	}

	@SuppressWarnings("unused")
	private List<Point> getLongestPath(
			final Graph<Point, IntegerWeightEdge> graph, final Point start,
			final Point end) {
		// TODO getLongestPath
		return Collections.emptyList();
	}
}
