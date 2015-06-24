package oop.ex6.main;

/**
 * Exception class for 'Invalid usage' - no arguments given.
 * @author Omer and Ron
 *
 */
public class SjavacInvalidUsageException extends SjavacException {
	private static final long serialVersionUID = 1L;
	
	public SjavacInvalidUsageException() {
		System.err.println("Usage: java oop.ex6.main.Sjavac file.sjava");
	}
}
