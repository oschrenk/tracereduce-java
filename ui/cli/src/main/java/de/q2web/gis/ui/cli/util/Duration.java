package de.q2web.gis.ui.cli.util;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
// @formatter:off
public class Duration {

	public static String of(final long nanoseconds) {

		final long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoseconds);
		final long milliSeconds = TimeUnit.NANOSECONDS.toMillis(nanoseconds)
				- TimeUnit.SECONDS.toMillis(seconds);
		final long microSeconds = TimeUnit.NANOSECONDS.toMicros(nanoseconds)
				- TimeUnit.MILLISECONDS.toMicros(TimeUnit.NANOSECONDS
						.toMillis(nanoseconds));
		final long reducedNanoSeconds = nanoseconds
				- TimeUnit.MICROSECONDS.toNanos(TimeUnit.NANOSECONDS
						.toMicros(nanoseconds));

		if (reducedNanoSeconds == 0) {
			return String.format("%ds, %dms, %dus", seconds, milliSeconds,
					microSeconds);
		}

		return String.format("%ds, %dms, %dus, %dns", seconds, milliSeconds,
				microSeconds, reducedNanoSeconds);
	}
}
// @formatter:on