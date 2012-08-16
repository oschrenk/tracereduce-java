package de.q2web.gis.algorithms.core;

import static org.junit.Assert.*;

import org.junit.Test;

import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmsTemplatesTest {

	@Test
	public void test() {
		final AlgorithmTemplate template = AlgorithmTemplates.build("dp");
		assertEquals(AlgorithmTemplate.Type.DOUGLAS_PEUCKER, template.getType());
		assertEquals(AlgorithmTemplate.Variant.DEFAULT, template.getVariant());
	}

}
