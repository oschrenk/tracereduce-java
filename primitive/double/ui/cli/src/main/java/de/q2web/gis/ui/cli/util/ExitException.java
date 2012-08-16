package de.q2web.gis.ui.cli.util;

import com.google.common.annotations.VisibleForTesting;

/**
 * The Class ExitException.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 * @version $Id: $
 */
public class ExitException extends SecurityException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The status. */
	private final int status;

	/**
	 * Instantiates a new exit exception.
	 * 
	 * @param message
	 *            the message
	 * @param e
	 *            the e
	 * @param status
	 *            the status
	 */
	public ExitException(final String message, final Throwable e,
			final int status) {
		super(message, e);
		this.status = status;
	}

	/**
	 * Instantiates a new exit exception.
	 * 
	 * @param status
	 *            the status
	 */
	@VisibleForTesting
	protected ExitException(final int status) {
		this.status = status;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 * @category getter
	 */
	public int getExitCode() {
		return status;
	}

}
