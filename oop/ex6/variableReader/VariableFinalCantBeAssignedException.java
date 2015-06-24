package oop.ex6.variableReader;

/**
 * Thrown if there is a try to change the value of a final variable.
 * @author Omer and Ron
 *
 */
public class VariableFinalCantBeAssignedException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableFinalCantBeAssignedException(int givenRowNumber) {
		System.err.println("Final variable cant be assigned. Line: " + givenRowNumber );
		
	}
}
