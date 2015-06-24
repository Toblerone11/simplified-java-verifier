package oop.ex6.variableReader;

/**
 * This exception occurs if one variable type gets illegal value type. <br>
 * g.e. <br>
 * 'String line = 10;' <br>
 * 'int number = "God help me!";
 * @author Omer and Ron
 *
 */
public class VariableTypeMismatchException extends VariableException {
	private static final long serialVersionUID = 1L;
		
	public VariableTypeMismatchException(int givenRowNumber) {
		System.err.println("Type mismatch error in line: " + givenRowNumber);
		
	}
}
