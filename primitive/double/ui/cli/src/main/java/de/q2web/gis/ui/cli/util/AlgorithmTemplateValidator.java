/*
 *******************************************************************************
 * AlgorithmValidator.java
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

import java.util.Collection;
import java.util.Set;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.internal.Maps;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import de.q2web.gis.algorithms.core.AlgorithmTemplates;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Type;
import de.q2web.gis.trajectory.core.api.AlgorithmTemplate.Variant;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class AlgorithmTemplateValidator implements IParameterValidator {

	private static final SetMultimap<Type, Variant> allowedVariantsMap = Multimaps
			.newSetMultimap(Maps.<Type, Collection<Variant>> newHashMap(),
					new Supplier<Set<Variant>>() {
				@Override
				public Set<Variant> get() {
					return Sets.newHashSet();
				}
			});

	static {
		allowedVariantsMap.put(Type.DOUGLAS_PEUCKER, Variant.DEFAULT);
		allowedVariantsMap.put(Type.LINEAR_APPROXIMATION, Variant.DEFAULT);
		allowedVariantsMap.put(Type.CUBIC_SPLINES, Variant.DEFAULT);
	}

	/*
	 * @see com.beust.jcommander.IParameterValidator#validate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void validate(final String name, final String value)
			throws ParameterException {
		final AlgorithmTemplate algorithm = AlgorithmTemplates.build(value);
		final Type type = algorithm.getType();
		final Variant variant = algorithm.getVariant();

		if (!allowedVariantsMap.get(type).contains(variant)) {
			throw new ParameterException(String.format(
					"Variant %s not allowed for algorithm %s.", variant, type));
		}

	}

}
