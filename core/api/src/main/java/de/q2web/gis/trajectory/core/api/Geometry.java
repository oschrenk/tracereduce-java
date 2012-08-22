package de.q2web.gis.trajectory.core.api;

public interface Geometry {

	double distance(Point from, Point to);

	double distance(Point point, Point lineStart, Point lineEnd);

	int compare(double a, double b);

}
