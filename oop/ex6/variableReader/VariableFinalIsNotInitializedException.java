package oop.ex6.variableReader;

/**
 * Thrown if final variable is declared and not given a value. 
 * @author Omer and Ron
 *
 */
public class VariableFinalIsNotInitializedException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableFinalIsNotInitializedException(int givenRowNumber) {
		System.err.println("Final variable is not initialized on declaration. Line: " + givenRowNumber );
	}
}
