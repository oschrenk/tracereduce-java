package de.q2web.gis.io.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.q2web.gis.core.api.Point;

public interface TraceReader {

	List<Point> read(File input) throws IOException;

}
