package oop.ex6.variableReader;

/**
 * Thrown if we try to assign variable1 value of variable2 but variable2 was not initialized.
 * @author Omer and Ron
 *
 */
public class VariableUnableToIntializeException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableUnableToIntializeException(int givenRowNumber) {
		System.err.println("Can't intialize variable with another variable value \n"
				+ "if the first was not initalized. line: " + givenRowNumber + "\n" );
		
	}
}
