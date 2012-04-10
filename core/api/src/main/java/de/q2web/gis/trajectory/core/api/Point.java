package de.q2web.gis.trajectory.core.api;

public interface Point<T extends Number> {

	int getDimensions();

	T get(int dimension);

}
