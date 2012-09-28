package de.q2web.gis.ui.cli.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class DurationTest {

	@Test
	public void test() {
		assertEquals("0s, 0ms, 1us, 1ns", Duration.of(1001));
		assertEquals("0s, 1ms, 1us, 1ns", Duration.of(1001001));
		assertEquals("1s, 1ms, 1us, 1ns", Duration.of(1001001001));
	}

}
