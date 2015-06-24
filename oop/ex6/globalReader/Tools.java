package oop.ex6.globalReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class of function shared by all class relevant to globalReader.
 * each line read passes throught the main method of this class in order to be recognized.
 * @author Omer and Ron
 *
 */
public class Tools {

	/* Constants */
	//code-line types.
	public static final int EMPTY_LINE = 1;
	public static final int COMMENT = 2;
	public static final int VARIABLE = 3;
	public static final int METHOD_DECLARTION = 4;
	public static final int METHOD_CALL = 5;
	public static final int IF_OR_WHILE = 6;
	public static final int RETURN = 7;
	public static final int CLOSE_BRACE = 8;
	
	//Patterns of lines recognition by regex.
	public static final Pattern METHOD_RECOGNIZER = Pattern.compile(Constants.METHOD_RECOGNIZER);
	public static final Pattern LEGAL_METHOD_CALL = Pattern.compile(Constants.METHOD_CALL);
	public static final Pattern CONDITION= Pattern.compile(Constants.COND_DECLARE);
	public static final Pattern RETURN_PATT = Pattern.compile(Constants.RETURN);
	public static final Pattern CLOSE_BLOCK = Pattern.compile(Constants.CLOSE_SCOPE);
	public static final Pattern variablePattern = Pattern.compile(Constants.LEGAL_VARS);
	public static final Pattern EMPTY_LINE_PATTERN = Pattern.compile(Constants.OPT_SPACE);
	public static final String COMMENT_REGEX = Constants.COMMENT;
	
	public static Matcher variableMatcher = null;
	
	/**
	 * Checks given line and returns its type in sjava terms.
	 * @param givenLine the line to recognize
	 * @param lineNumber counter of number of lines
	 * @return int number to determine line type.
	 * @throws ReaderUnknownRowException in case of unrecognizable line.
	 */
	public static int checkLine(String givenLine, int lineNumber) throws ReaderUnknownRowException {
		if (checkForEmptyLine(givenLine)) {
			return EMPTY_LINE;
		} else if (checkForCommentLine(givenLine)) {
			return COMMENT;
		} else if (RETURN_PATT.matcher(givenLine).matches()) {
			return RETURN;
		} else if (checkForVariableLine(givenLine)) {
			return VARIABLE;
		} else if (METHOD_RECOGNIZER.matcher(givenLine).matches()){
			return METHOD_DECLARTION;
		} else if (LEGAL_METHOD_CALL.matcher(givenLine).matches()) {
			return METHOD_CALL;
		} else if (CONDITION.matcher(givenLine).matches()) {
			return IF_OR_WHILE;
		} else if (CLOSE_BLOCK.matcher(givenLine).matches()) {
			return CLOSE_BRACE;
		} else {
			throw new ReaderUnknownRowException(lineNumber);
		}
	}
	
	// CheckLine's function assistant functions - BEGINNING //
	
	/**
	 * Skip empty lines, lines with tabs or spaces.
	 * @param line
	 * @return true or false
	 */
	private static boolean checkForEmptyLine(String line) {
		return EMPTY_LINE_PATTERN.matcher(line).matches(); // update the constant here;
	}
	
	
	/**
	 * Check if line begins with '//'.
	 * @param line
	 * @return true or false;
	 */
	private static boolean checkForCommentLine(String line) {
		return line.startsWith(COMMENT_REGEX);
	}
	
	/**
	 * Checks if line is a 'variable' type line.
	 * @return true or false
	 */
	private static boolean checkForVariableLine(String currentLine) {
		variableMatcher = variablePattern.matcher(currentLine);
		if (variableMatcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 *  Getter for variable matcher.
	 * @return variableMatcher
	 */
	public static Matcher getVarMatcher() {
		return variableMatcher;
	}
		
	/**
	 *  Getter for variable pattern.
	 * @return variablePattern
	 */
	public static Pattern getVarPattern() {
		return variablePattern;
	}
	
	
	// CheckLine's function assistant functions - END //
	
}
