package de.q2web.gis.ui.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.q2web.gis.ui.cli.util.ExitException;
import de.q2web.gis.ui.cli.util.NoExitSecurityManager;

/**
 * The Class CommandLineExitTest.
 *
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class CommandLineExitIntegrationTest {

	/** The original security manager. */
	private SecurityManager originalSecurityManager;

	/**
	 * Replace the security manager with a mock version that captures the exit
	 * code
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		originalSecurityManager = System.getSecurityManager();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	/**
	 * Test for fail exit code.
	 */
	@Test
	public void testForFailExitCode() {
		try {
			CommandLine.main(new String[0]);
		} catch (final ExitException e) {
			assert (e.getExitCode() > 0);
		}
	}

	/**
	 * We have to restore the original Security Manager
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		System.setSecurityManager(originalSecurityManager);
	}

}
