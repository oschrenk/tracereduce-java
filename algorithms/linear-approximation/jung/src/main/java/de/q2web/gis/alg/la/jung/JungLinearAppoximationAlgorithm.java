package de.q2web.gis.alg.la.jung;

import java.util.Collections;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Algorithm;
import de.q2web.gis.trajectory.core.api.Geometry;
import de.q2web.gis.trajectory.core.api.Point;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class JungLinearAppoximationAlgorithm<P extends Number> implements
		Algorithm<P> {

	private final Geometry<P> geometry;

	private Graph<Point<P>, IntegerWeightEdge> graph;

	public JungLinearAppoximationAlgorithm(Geometry<P> geometry) {
		super();
		this.geometry = geometry;
	}

	@Override
	public List<Point<P>> run(List<Point<P>> points, P epsilon) {
		if (points.size() < 3) {
			return points;
		}

		graph = getVerticesOnlyGraph(points);
		addPossibleEdges(points, epsilon);

		final Point<P> start = points.get(0);
		final Point<P> end = points.get(points.size());

		return getLongestPath(graph, start, end);
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

		graph.addEdge(new IntegerWeightEdge(j - i - 1), iPoint, jPoint);
	}

	private Graph<Point<P>, IntegerWeightEdge> getVerticesOnlyGraph(
			List<Point<P>> points) {
		final Graph<Point<P>, IntegerWeightEdge> graph = new DirectedSparseGraph<Point<P>, IntegerWeightEdge>();

		for (Point<P> point : points) {
			graph.addVertex(point);
		}

		return graph;
	}

	private List<Point<P>> getLongestPath(
			Graph<Point<P>, IntegerWeightEdge> graph, Point<P> start,
			Point<P> end) {
		// TODO getLongestPath
		return Collections.emptyList();
	}
}
