package de.q2web.gis.io.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.q2web.gis.trajectory.core.api.Point;

public interface TraceReader<P extends Number> {

	List<Point<P>> read(File input) throws IOException;

}
