package de.q2web.gis.alg.la.ref.dijkstra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class DijkstraAlgorithmTest {

	private Graph<String> graph;
	private Vertex<String> o;
	private Vertex<String> t;
	private List<Vertex<String>> expectedPath;

	/**
	 * Example from
	 * http://optlab-server.sce.carleton.ca/POAnimations2007/DijkstrasAlgo.html
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		o = new Vertex<String>(0, "O");
		final Vertex<String> a = new Vertex<String>(1, "A");
		final Vertex<String> b = new Vertex<String>(2, "B");
		final Vertex<String> c = new Vertex<String>(3, "C");
		final Vertex<String> d = new Vertex<String>(4, "D");
		final Vertex<String> e = new Vertex<String>(5, "E");
		final Vertex<String> f = new Vertex<String>(6, "F");
		t = new Vertex<String>(7, "T");

		final List<Vertex<String>> vertexes = Lists.newArrayList(o, a, b, c, d,
				e, f, t);
		expectedPath = Lists.newArrayList(o, a, b, d, t);

		final Edge<String> oa = new Edge<String>(0, o, a, 2);
		final Edge<String> ob = new Edge<String>(1, o, b, 5);
		final Edge<String> oc = new Edge<String>(2, o, c, 4);

		final Edge<String> ab = new Edge<String>(3, a, b, 2);
		final Edge<String> af = new Edge<String>(4, a, f, 12);

		final Edge<String> bc = new Edge<String>(5, b, c, 1);
		final Edge<String> bd = new Edge<String>(6, b, d, 4);
		final Edge<String> be = new Edge<String>(7, b, e, 3);

		final Edge<String> ce = new Edge<String>(8, c, e, 4);

		final Edge<String> de = new Edge<String>(9, d, e, 1);
		final Edge<String> dt = new Edge<String>(10, d, t, 5);

		final Edge<String> et = new Edge<String>(11, e, t, 7);

		final Edge<String> ft = new Edge<String>(12, f, t, 3);

		final List<Edge<String>> edges = Lists.newArrayList(oa, ob, oc, ab, af,
				bc, bd, be, ce, de, dt, et, ft);

		graph = new Graph<String>(vertexes, edges);
	}

	@Test
	public void test() {
		final DijkstraAlgorithm<String> dijkstraAlgorithm = new DijkstraAlgorithm<String>(
				graph);

		dijkstraAlgorithm.execute(o);
		final LinkedList<Vertex<String>> path = dijkstraAlgorithm.getPath(t);

		assertTrue(path.size() == 5);
		assertEquals(path, expectedPath);

	}
}
