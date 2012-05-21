/*
 *******************************************************************************
 * AlgorithmConverterTest.java
 * $Id: $
 *
 *******************************************************************************
 *
 * Copyright:   Q2WEB GmbH
 *              quality to the web
 *
 *              Tel  : +49 (0) 211 / 159694-00	Kronprinzenstr. 82-84
 *              Fax  : +49 (0) 211 / 159694-09	40217 Düsseldorf
 *              eMail: info@q2web.de						http://www.q2web.de
 *
 *
 * Author:      University
 *
 * Created:     10.04.2012
 *
 * Copyright (c) 2009 Q2WEB GmbH.
 * All rights reserved.
 *
 *******************************************************************************
 */
package de.q2web.gis.ui.cli.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Type;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Variant;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de
 */
public class AlgorithmFactoryTest {

	private static final AlgorithmTemplate DEFAULT_DOUGLAS_PEUCKER = new AlgorithmTemplate(
			Type.DOUGLAS_PEUCKER);

	private static final AlgorithmTemplate DEFAULT_LINEAR_APPROXIMATION = new AlgorithmTemplate(
			Type.LINEAR_APPROXIMATION);
	private static final AlgorithmTemplate JGRAPHT_LINEAR_APPROXIMATION = new AlgorithmTemplate(
			Type.LINEAR_APPROXIMATION, Variant.JGRAPHT);
	private static final AlgorithmTemplate JUNG_LINEAR_APPROXIMATION = new AlgorithmTemplate(
			Type.LINEAR_APPROXIMATION, Variant.JUNG);

	private static final AlgorithmTemplate DEFAULT_CUBIC_SPLINES = new AlgorithmTemplate(
			Type.CUBIC_SPLINES);

	private AlgorithmTemplateConverter algorithmConverter;

	@Before
	public void setUp() {
		algorithmConverter = new AlgorithmTemplateConverter();
	}

	@Test
	public void testDefaultDouglasPeucker() {
		assertEquals(DEFAULT_DOUGLAS_PEUCKER, algorithmConverter.convert("dp"));
		assertEquals(DEFAULT_DOUGLAS_PEUCKER, algorithmConverter.convert("DP"));
		assertEquals(DEFAULT_DOUGLAS_PEUCKER,
				algorithmConverter.convert("douglas-peucker"));
	}

	@Test
	public void testDefaultLinearApproximation() {
		assertEquals(DEFAULT_LINEAR_APPROXIMATION,
				algorithmConverter.convert("la"));
		assertEquals(DEFAULT_LINEAR_APPROXIMATION,
				algorithmConverter.convert("LA"));
		assertEquals(DEFAULT_LINEAR_APPROXIMATION,
				algorithmConverter.convert("linear-approximation"));
	}

	public void testVariantLinearApproximation() {
		assertEquals(JGRAPHT_LINEAR_APPROXIMATION,
				algorithmConverter.convert("la#jgrapht"));
		assertEquals(JUNG_LINEAR_APPROXIMATION,
				algorithmConverter.convert("la#jung"));
	}

	@Test
	public void testDefaultCubicSplines() {
		assertEquals(DEFAULT_CUBIC_SPLINES, algorithmConverter.convert("cs"));
		assertEquals(DEFAULT_CUBIC_SPLINES, algorithmConverter.convert("CS"));
		assertEquals(DEFAULT_CUBIC_SPLINES,
				algorithmConverter.convert("cubic-splines"));
	}

}
