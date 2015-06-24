package oop.ex6.variableReader;

/**
 * Thrown if variable was not declared before.
 * @author Omer and Ron
 *
 */
public class VariableNotExistException  extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableNotExistException(int givenRowNumber) {
		System.err.println("Variable was not decalred before. Line: " + givenRowNumber );
		
	}
}
