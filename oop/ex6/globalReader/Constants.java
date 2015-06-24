package oop.ex6.globalReader;

/**
 * Class of constants shared by all classes relevant to activity
 * of Reader.
 * @author Omer and Ron
 *
 */
public class Constants {

	/* constants */
	public static final String SPACE = "\\s+", OPT_SPACE = "\\s*"; //space and optinal space;
	public static final String LEGAL_NAME = "(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)";
	public static final String LEGAL_METHOD_NAME = "(?:[A-Za-z][A-Za-z0-9_]*)";
	public static final String LAZY = "?", POSSESS = "+";
	public static final String COMMENT = "//";
	public static final String NUMBER = "(?:-)?\\d+(?:\\.\\d+)?+";
	
	//types of variables
	public static final String INT_TYPE = "int";
	public static final String DOUBLE_TYPE = "double";
	public static final String STRING_TYPE = "String";
	public static final String BOOLEAN_TYPE = "boolean";
	public static final String CHAR_TYPE = "char";
	public static final String DIMC = "DIMC"; // DIMC = Defined In Method Call.
	
	//variables recognition regex
	public static final String INT_TYPE_REGEX = "-?\\d+";
	public static final String DOUBLE_TYPE_REGEX = NUMBER;
	public static final String STRING_BEGINNING_CHAR = "\"";
	public static final String STRING_ENDING_CHAR = "\"";
	public static final String CHAR_BEGINNING_CHAR = "\'";
	public static final String CHAR_ENDING_CHAR = "\'";
	public static final String STRING_TYPE_REGEX = STRING_BEGINNING_CHAR + "?" + ".*" + STRING_ENDING_CHAR;
	public static final String CHAR_TYPE_REGEX = CHAR_BEGINNING_CHAR + "." + CHAR_ENDING_CHAR;
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String BOOLEAN_TYPE_REGEX = "(?:" + TRUE  + "|" + FALSE +")";
	
	//scope regex
	public static final String RETURN = "\\s*return;\\s*";
	
	//var regex
	public static final String LEGAL_TYPE = "\\b(int|String|double|char|boolean)\\b?";
	public static final String VAR_DEFINITION = "(=(" + SPACE + ".*))";
	public static final String DEFINITION = "(?:=(?:\\s*(?:[^," + '"' + ";\\s]++|"
			+ "[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*,)*)" + OPT_SPACE + LEGAL_NAME + 
			OPT_SPACE + "(?:=(?:\\s*([^," + '"' + ";\\s]+|[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*);\\s*";
	public static final String FINAL = OPT_SPACE + "(final)";
	
	
	public static final String LEGAL_VARS = "(?:\\s*(final)\\s+)?\\s*\\b(int|String|double|char|boolean)?"
			+ "\\b((?:\\s*(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\\s*(?:=(?:\\s*(?:[^," + '"' + ";\\s]++|"
			+ "[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*,)*)\\s*(?:(_[A-Za-z0-9_]+|[A-Za-z]"
					+ "[A-Za-z0-9_]*)\\s*(?:=(?:\\s*([^," + '"' + ";\\s]+|[" + '"' + "][^" + '"' + "]++"
							+ "[" + '"' + "])+))?\\s*);\\s*";
					
	public static final String LEGAL_VAR =
			SPACE + POSSESS + LEGAL_TYPE + "{1}" + LAZY + SPACE + "(" + LEGAL_NAME + SPACE + VAR_DEFINITION 
			+ "?" + SPACE + "," + ")*" + SPACE + "(" + LEGAL_NAME + SPACE + VAR_DEFINITION + "?" + SPACE + ")" + ";";
	
	//method regex
	public static final String LEGAL_RETURN_TYPE = "void";
	public static final String METHOD_PATTERN =
							OPT_SPACE + "(" + LEGAL_RETURN_TYPE + ")" + 
							SPACE + "(" + LEGAL_METHOD_NAME + ")" + OPT_SPACE + 
							"\\((.*)\\)";
	public static final String METHOD_ARG = "(" + LEGAL_TYPE + SPACE + LEGAL_NAME + ")";
	public static final String LEGAL_DECLARATION = "\\(" + "("
							+ "(?:" + OPT_SPACE + METHOD_ARG + OPT_SPACE + "," + ")*" + OPT_SPACE
			 				+ "(?:" + METHOD_ARG + OPT_SPACE + ")?"
			 				+ ")?" + "\\)";
	
	public static final String OPEN_SCOPE = "\\s*[\\{]\\s*";
	public static final String CLOSE_SCOPE = "\\s*[\\}]\\s*";
	public static final String METHOD_EXPLICIT_ARG = "(?:" + 
			INT_TYPE_REGEX + "|" + DOUBLE_TYPE_REGEX + "|" + BOOLEAN_TYPE_REGEX + "|" +
			STRING_TYPE_REGEX + "|" + CHAR_TYPE_REGEX + ")";	
	
	public static final String METHOD_CALL_ARG = "(" + "(?:" + LEGAL_NAME + "|" + METHOD_EXPLICIT_ARG + ")" + ")";
	
	public static final String METHOD_CALL =
			OPT_SPACE + "(" + LEGAL_METHOD_NAME + ")\\s*" +
			"\\(" + "("  + "(?:" + OPT_SPACE + "(?:" + METHOD_CALL_ARG + ")" + OPT_SPACE + "," + ")*" +
			"(?:" + OPT_SPACE + "(?:" + METHOD_CALL_ARG + ")" + ")?" + ")?" + OPT_SPACE + "\\)" +
			OPT_SPACE + ";";

	
	public static final String METHOD_RECOGNIZER =
			OPT_SPACE + LEGAL_RETURN_TYPE + SPACE + LEGAL_METHOD_NAME + OPT_SPACE + LEGAL_DECLARATION
			+ OPT_SPACE+ "\\{" + OPT_SPACE;
	
	//if / while regex
	public static final String CONDITION = "((?:\\s*(?:" + LEGAL_NAME + "|" + NUMBER + ")+\\s*(?:\\|\\||&&))*\\s*"
			+ "(?:" + LEGAL_NAME + "|" + NUMBER + ")\\s*)";
	public static final String COND_DECLARE = "\\s*(?:if|while)?+\\s*\\(" + CONDITION + "\\)\\s*\\{" + OPT_SPACE;
}
	


