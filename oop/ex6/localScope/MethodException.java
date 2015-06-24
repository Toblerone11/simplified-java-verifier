package oop.ex6.localScope;

/**
 * super class of the exceptions which can be thrown by MethodReader instances. 
 * @author Omer and Ron
 *
 */
public class MethodException extends LocalScopeException {

	/* data members */
	private static final long serialVersionUID = 1L;
	
	public MethodException() {
		super("not valid method declaration");
	}
	
	public MethodException(String messege) {
		super(messege);
	}
}
