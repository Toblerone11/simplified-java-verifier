package oop.ex6.variableReader;

public class VariableUnableToIntializeException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableUnableToIntializeException(int givenRowNumber) {
		System.err.println("Can't intialize variable with another variable value \n"
				+ "if the first was not initalized. line: " + givenRowNumber + "\n" );
		
	}
}
