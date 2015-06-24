package oop.ex6.variableReader;

public class VariableFinalIsNotInitializedException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableFinalIsNotInitializedException(int givenRowNumber) {
		System.err.println("Final variable is not initialized on declaration. Line: " + givenRowNumber );
	}
}
