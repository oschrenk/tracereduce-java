package de.q2web.gis.trajectory.core.api;

public interface Geometry<P extends Number> {

	P distance(Point<P> from, Point<P> to);

	P distance(Point<P> point, Point<P> lineStart, Point<P> lineEnd);

	int compare(P a, P b);

}
