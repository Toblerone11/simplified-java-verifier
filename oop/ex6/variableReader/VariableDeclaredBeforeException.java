package oop.ex6.variableReader;

/**
 * Same variables can't be declared twice. <br>
 * Example: <br>
 * String a = "Omer"; <br>
 * ... <br>
 * String a = "Ron";
 * 
 * @author 
 *
 */
public class VariableDeclaredBeforeException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public VariableDeclaredBeforeException(int givenRowNumber) {
		System.err.println("Global variable was declared before. Line: " + givenRowNumber );
		
	}
}
