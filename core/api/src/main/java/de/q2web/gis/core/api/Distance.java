package de.q2web.gis.core.api;

public interface Distance {

	double distance(Point from, Point to);

	double distance(Point point, Point lineStart, Point lineEnd);

}
