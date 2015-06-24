package oop.ex6.localScope;

/**
 * super class of the exceptions which can be thrown by MethodReader instances. 
 * @author Omer and Ron
 *
 */
public class LocalScopeException extends Exception {

	/* data members */
	private static final long serialVersionUID = 1L;
	
	public LocalScopeException() {
		super("ERROR with inner scope");
	}
	
	public LocalScopeException(String message) {
		System.err.println(message);
	}
}

