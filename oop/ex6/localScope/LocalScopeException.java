package oop.ex6.localScope;

/**
 * class of the exceptions which can be thrown by MethodReader instances.
 * for each illegal issue that being detected, the problem is written at the detection point
 * and passed to this class constructor.
 * @author Omer and Ron
 *
 */
public class LocalScopeException extends Exception {

	/* data members */
	private static final long serialVersionUID = 1L;
	
	/**
	 * default C'tor
	 */
	public LocalScopeException() {
		super("ERROR with inner scope");
	}
	
	/**
	 * C'tor. pass the message to the super class C'tor.
	 * @param message
	 */
	public LocalScopeException(String message) {
		System.err.println(message);
	}
}

