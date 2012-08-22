package de.q2web.gis.ui.cli.util;

import java.security.Permission;

/**
 * The Class NoExitSecurityManager.
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class NoExitSecurityManager extends SecurityManager {

	/*
	 * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
	 */
	@Override
	public void checkPermission(final Permission perm) {
		// allow anything.
	}

	/*
	 * @see java.lang.SecurityManager#checkPermission(java.security.Permission,
	 * java.lang.Object)
	 */
	@Override
	public void checkPermission(final Permission perm, final Object context) {
		// allow anything.
	}

	/*
	 * @see java.lang.SecurityManager#checkExit(int)
	 */
	@Override
	public void checkExit(final int status) {
		super.checkExit(status);
		throw new ExitException(status);
	}
}
