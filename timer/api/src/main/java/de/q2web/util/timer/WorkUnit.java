package de.q2web.util.timer;

/**
 * The Interface WorkUnit.
 * 
 * @param <I>
 *            input type
 * @param <O>
 *            output type
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public interface WorkUnit<I, O> {

	/**
	 * Run.
	 * 
	 * @param input
	 *            the input
	 * @return the o
	 * @throws Exception
	 *             the exception
	 */
	O run(I input) throws Exception;

}
