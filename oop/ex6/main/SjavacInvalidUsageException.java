package oop.ex6.main;

/**
 * Invalid usage - no arguments were given.
 * @author Omer & Ron
 *
 */
public class SjavacInvalidUsageException extends SjavacException {
	private static final long serialVersionUID = 1L;
	
	public SjavacInvalidUsageException() {
		System.err.println("Usage: java oop.ex6.main.Sjavac file.sjava");
		
	}
}
