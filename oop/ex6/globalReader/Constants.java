package oop.ex6.globalReader;

public class Constants {

	/* constants */
	public static final String SPACE = "\\s+", OPT_SPACE = "\\s*"; //space and optinal space;
	public static final String LEGAL_NAME = "(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)";
	public static final String LAZY = "?", POSSESS = "+";
	public static final String COMMENT = "//";
	public static final String NUMBER = "(?:-)?\\d+(?:\\.\\d+)?+";
	
	//scope regex
	public static final String RETURN = "\\s*return;\\s*";
	
	//var regex
	public static final String LEGAL_TYPE = "(?:int|String|double|char|boolean)";
	public static final String VAR_DEFINITION = "(=(" + SPACE + ".*))";
	public static final String LEGAL_VAR =
			SPACE + POSSESS + LEGAL_TYPE + "{1}" + LAZY + SPACE + "(" + LEGAL_NAME + SPACE + VAR_DEFINITION + "?" + SPACE + "," + ")*" + SPACE
			+ "(" + LEGAL_NAME + SPACE + VAR_DEFINITION + "?" + SPACE + ")" + ";";
	
	//method regex
	public static final String LEGAL_RETURN_TYPE = "void";
	public static final String METHOD_PATTERN =
							OPT_SPACE + "(" + LEGAL_RETURN_TYPE + ")" + 
							SPACE + "(" + LEGAL_NAME + ")" + 
							"\\((.*)\\)";
	public static final String METHOD_ARG = "(" + LEGAL_TYPE + SPACE + LEGAL_NAME + ")";
	public static final String LEGAL_DECLARATION = "\\(" + "("
							+ "(?:" + OPT_SPACE + METHOD_ARG + OPT_SPACE + "," + ")*" + OPT_SPACE
			 				+ "(?:" + METHOD_ARG + OPT_SPACE + ")?"
			 				+ ")?" + "\\)";
	
	public static final String OPEN_SCOPE = "\\s*[\\{]\\s*";
	public static final String CLOSE_SCOPE = "\\s*[\\}]\\s*";
	public static final String METHOD_CALL =
			OPT_SPACE + "(" + LEGAL_NAME + ")\\s*" +
			"\\(" + "("  + "(?:" + OPT_SPACE + "(?:" + LEGAL_NAME + "|" + NUMBER + ")" + OPT_SPACE + "," + ")*" +
			"(?:" + OPT_SPACE + "(?:" + LEGAL_NAME + "|" + NUMBER + ")" + ")?" + ")?" + OPT_SPACE + "\\)" +
			OPT_SPACE + ";";
	
	public static final String METHOD_CALL_ARG = "(" + "(?:" + LEGAL_NAME + "|" + NUMBER + ")" + ")";
	
	public static final String METHOD_RECOGNIZER =
			OPT_SPACE + LEGAL_RETURN_TYPE + SPACE + LEGAL_NAME + LEGAL_DECLARATION + OPT_SPACE + "\\{";
	
	//if / while regex
	public static final String CONDITION = "((?:\\s*(?:" + LEGAL_NAME + "|" + NUMBER + ")+\\s*(?:\\|\\||&&))*\\s*(?:" + LEGAL_NAME + "|" + NUMBER + ")\\s*)";
	public static final String COND_DECLARE = "\\s*(?:if|while)?+\\s*\\(" + CONDITION + "\\)\\s*\\{";
	
	
	public String b = "\\s*(?:(final)\\s+)\\b(int|String|double|char|boolean){1}?\\b((?:\\s*(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\\s*(?:=(?:\\s*(?:[^," + '"' + ";\\s]++|[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*,)*)\\s*(?:(_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\\s*(?:=(?:\\s*([^," + '"' + ";\\s]+|[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*);";
//	public String c = "\s*\b(int|String|double|char|boolean){1}?\b((?:\s*(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\s*(?:=(?:\s*(?:[^,";\s]++|["][^"]++["])+))?\s*,)*)\s*(?:(_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\s*(?:=(?:\s*([^,";\s]+|["][^"]++["])+))?\s*);"
	
	public String c = "(?:\\s*(final)\\s+)?\\b(int|String|double|char|boolean){1}?\\b((?:\\s*(?:_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\\s*(?:=(?:\\s*(?:[^," + '"' + ";\\s]++|[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*,)*)\\s*(?:(_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)\\s*(?:=(?:\\s*([^," + '"' + ";\\s]+|[" + '"' + "][^" + '"' + "]++[" + '"' + "])+))?\\s*);";
	
	public String d = "(?:(_[A-Za-z0-9_]+|[A-Za-z][A-Za-z0-9_]*)+\\s*,)*";
}

