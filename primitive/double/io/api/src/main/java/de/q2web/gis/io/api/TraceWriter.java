package de.q2web.gis.io.api;

import java.io.IOException;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Point;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public interface TraceWriter {

	void write(List<Point> trace) throws IOException;

}
