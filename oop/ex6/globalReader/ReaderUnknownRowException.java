package oop.ex6.globalReader;


/**
 * The exception rises if row is NOT one of the follows: <br>
 * 1. Empty line, line with spaces and/or tabs. <br>
 * 2. starts with '//'. <br>
 * 3. Legal variable row. <br>
 * 4. Legal method row.
 * @author Omer & Ron
 *
 */
public class ReaderUnknownRowException extends ReaderException {
	private static final long serialVersionUID = 1L;
	
	public ReaderUnknownRowException(int givenRowNumber) {
		System.err.println("Illegal s-java error in line: " + givenRowNumber );
	}
}
