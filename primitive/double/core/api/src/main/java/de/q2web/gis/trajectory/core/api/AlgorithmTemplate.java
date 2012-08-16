/*
 *******************************************************************************
 * Algorithm.java
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
 * Created:     09.04.2012
 *
 * Copyright (c) 2009 Q2WEB GmbH.
 * All rights reserved.
 *
 *******************************************************************************
 */
package de.q2web.gis.trajectory.core.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class Algorithm.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de
 */
public class AlgorithmTemplate {

	/** The type. */
	private final Type type;

	/** The variant. */
	private final Variant variant;

	public AlgorithmTemplate(final Type type) {
		this(type, Variant.DEFAULT);
	}

	/**
	 * Instantiates a new algorithm.
	 * 
	 * @param type
	 *            the type
	 * @param variant
	 *            the variant
	 */
	public AlgorithmTemplate(final Type type, final Variant variant) {
		super();
		this.type = type;
		this.variant = variant;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * @category getter
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the variant.
	 * 
	 * @return the variant
	 * @category getter
	 */
	public Variant getVariant() {
		return variant;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((variant == null) ? 0 : variant.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AlgorithmTemplate other = (AlgorithmTemplate) obj;
		if (type != other.type) {
			return false;
		}
		if (variant != other.variant) {
			return false;
		}
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Algorithm [type=" + type + ", variant=" + variant + "]";
	}

	/**
	 * The Enum Type.
	 */
	public static enum Type {

		DOUGLAS_PEUCKER("dp", "douglas-peucker"),

		LINEAR_APPROXIMATION("la", "linear-approximation"),

		CUBIC_SPLINES("cs", "cubic-splines");

		private final Set<String> ids;

		private Type(final String... ids) {
			this.ids = new HashSet<String>(Arrays.asList(ids));
		}

		public Set<String> getIds() {
			return ids;
		}

		public static Type get(final String value) {
			final String lowerCase = value.toLowerCase();

			if (DOUGLAS_PEUCKER.getIds().contains(lowerCase)) {
				return DOUGLAS_PEUCKER;
			}
			if (LINEAR_APPROXIMATION.getIds().contains(lowerCase)) {
				return LINEAR_APPROXIMATION;
			}
			if (CUBIC_SPLINES.getIds().contains(lowerCase)) {
				return CUBIC_SPLINES;
			}

			return null;
		}
	}

	/**
	 * The Enum Variant.
	 */
	public static enum Variant {

		DEFAULT("default"), REFERENCE("reference"), JGRAPHT("jgrapht"), JUNG("jung");
		private final String id;

		private Variant(final String id) {
			this.id = id;
		}

		/**
		 * @return the id
		 * @category getter
		 */
		public String getId() {
			return id;
		}

		public static Variant get(final String value) {
			final String lowerCase = value.toLowerCase();

			if (DEFAULT.getId().equals(lowerCase)) {
				return DEFAULT;
			}
			if (REFERENCE.getId().equals(lowerCase)) {
				return REFERENCE;
			}
			if (JGRAPHT.getId().equals(lowerCase)) {
				return JGRAPHT;
			}
			if (JUNG.getId().equals(lowerCase)) {
				return JUNG;
			}

			return null;
		}
	}

}
