package de.q2web.gis.alg.la.jgrapht;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.traverse.TopologicalOrderIterator;

public class LongestPathIterator<V, E> extends TopologicalOrderIterator<V, E> {

	private final Map<V, Integer> lengthsData = new HashMap<V, Integer>();

	public LongestPathIterator(DirectedGraph<V, E> graph) {
		super(graph);
		for (V vertex : graph.vertexSet()) {
			if (graph.inDegreeOf(vertex) == 0)
				lengthsData.put(vertex, 0);
		}
	}

	private int calculatePathLength(V vertex, E edge) {
		if (edge == null)
			return 0;
		V fromVertex = Graphs.getOppositeVertex(getGraph(), edge, vertex);
		int fromVertexData = lengthsData.get(fromVertex);
		return fromVertexData + ((IntegerWeightEdge) edge).getWeight();
	}

	@Override
	protected void encounterVertex(V vertex, E edge) {
		super.encounterVertex(vertex, edge);
		lengthsData.put(vertex, calculatePathLength(vertex, edge));
	}

	@Override
	protected void encounterVertexAgain(V vertex, E edge) {
		super.encounterVertexAgain(vertex, edge);
		int vertexData = lengthsData.get(vertex);
		int newPathLength = calculatePathLength(vertex, edge);
		if (vertexData < newPathLength)
			lengthsData.put(vertex, newPathLength);
	}

	public double getLongestPathLength(V vertex) {
		return lengthsData.get(vertex).doubleValue();
	}
}