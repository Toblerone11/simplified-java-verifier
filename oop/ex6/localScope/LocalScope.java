package oop.ex6.localScope;

import java.util.HashMap;
import java .util.regex.*;
import java.io.BufferedReader;
import java.io.IOException;

import oop.ex6.globalReader.Constants;
import oop.ex6.globalReader.ReaderUnknownRowException;
import oop.ex6.globalReader.Tools;
import oop.ex6.variableReader.VarReader;
import oop.ex6.variableReader.Variable;
import oop.ex6.variableReader.VariableException;


/**
 * this class used as super class in order to handle methods and if/while loop in s-java files.
 * in general, this class related to the parts of the code in which there is local variables that
 * should be undefined outside their scopes.
 * @author Omer and Ron
 *
 */
public class LocalScope {

	
	/* constants */
	protected static final int METHOD = 1, CONDITION = 2;
	protected static final Pattern BLOCK_CLOSER = Pattern.compile(Constants.CLOSE_SCOPE);
	protected static final Pattern METHOD_CALL = Pattern.compile(Constants.METHOD_CALL);
	protected static final Pattern RETURN = Pattern.compile(Constants.RETURN);
	protected static final Pattern VAR_PATTERN = Pattern.compile(Constants.LEGAL_VAR);
	protected static final Pattern EMPTY = Pattern.compile(Constants.OPT_SPACE);
	protected static final Pattern ARG_PATTERN = Pattern.compile(Constants.METHOD_CALL_ARG);
	protected static final Pattern EXPLICIT_ARG = Pattern.compile(Constants.NUMBER);
	protected static final String COMMENT = "//";
	
	/* data members */
	protected BufferedReader reader;
	protected Pattern pattern;
	protected Matcher matcher;
	protected HashMap<String, Method> methodsTable;
	private int lineNumber;
	protected VarReader varReader;
	
	public LocalScope() {
		this.reader = null;
		this.methodsTable = new HashMap<String, Method>();
	}
	
	/**
	 * reads the whole block until its closing curly brackets (block closer).
	 * checks the types of whole lines, if they match to some known type - it pass
	 * to the next line. in the case of a if/while statement, the function enters the inner scope
	 * in recursion.
	 * @param currentLine the declaration line of the scope to check.
	 * @param reader the buffered reader of the file.
	 * @param lineNum the current number of line.
	 * @return updated buffered reader
	 * @throws IOException in case of issues with the buffered reader
	 * @throws ReaderUnknownRowException in case of unknown type of some line
	 * @throws LocalScopeException in case there is illegal method declaration in the scope.
	 * @throws VariableException 
	 */
	public BufferedReader checkScope(String currentLine, BufferedReader bReader, int lineNum)
			throws IOException, ReaderUnknownRowException, LocalScopeException, VariableException {
		/* checks the declaration of the scope */	
		
		readDeclarationLine(currentLine, lineNum, varReader);
		this.reader = bReader;
		String line = reader.readLine();
		lineNum++;
		while (!BLOCK_CLOSER.matcher(line).matches()) {
			//System.out.println(lineNum + line);
			/* checks the line type */
			int lineType = Tools.checkLine(line, lineNum);
			switch (lineType) {
			case (Tools.METHOD_DECLARTION):
				throw new LocalScopeException("invalid method declaration in a local scope. line : " + lineNum);
			case (Tools.IF_OR_WHILE):
				checkScope(currentLine, reader, lineNum);
				break;
			case (Tools.EMPTY_LINE):
				break;
			case (Tools.COMMENT):
				break;
			case (Tools.VARIABLE):
				break;
			case (Tools.RETURN):
				break;
			case (Tools.METHOD_CALL):
				break;
			default:
				return null;
			}
			line = reader.readLine();
			lineNum++;
		}
		lineNumber = lineNum;
		return reader;
	}

	/**
	 * reads the scope. checks the syntax of each line and handle all cases.
	 * if
	 * @param bReader the reader at its current position - at the start of the scope.
	 * @param lineNum the current line number.
	 * @param varsTable HashMap<String, Variable>, contains all the variables declared untill the former scope
	 * @return updated buffered reader - at the end of the scope.
	 * @throws IOException in case of issues with reading lines
	 * @throws LocalScopeException in case there is a syntax problem
	 * @throws ReaderUnknownRowException in case of unknown lines.
	 * @throws VariableException 
	 */
	public BufferedReader readScope(String currentLine, BufferedReader bReader, int lineNum, VarReader varReader) 
			throws IOException, LocalScopeException, ReaderUnknownRowException, VariableException {
		/* creating new VarReader instance to handle variables assignments */
		reader = bReader;
		String line = reader.readLine();
		lineNum++;
		
		while (!BLOCK_CLOSER.matcher(line).matches()) {
			int lineType = Tools.checkLine(line, lineNum);
			switch (lineType) {
			case (Tools.METHOD_DECLARTION):
				throw new LocalScopeException("invalid method declaration in a local scope. line : " + lineNum);
			case (Tools.IF_OR_WHILE):			
				ConditionBlock conditionBlock = new ConditionBlock();
				conditionBlock.readConditionScope(line, reader, lineNum, varReader.getGlobalTable());
				break;
			case (Tools.EMPTY_LINE):
				break;
			case (Tools.COMMENT):
				break;
			case (Tools.VARIABLE):
				varReader.analyzeLine(line, Tools.getVarMatcher() , lineNum);
				break;
			case (Tools.RETURN):
				handleReturn();
				return reader;
			case (Tools.METHOD_CALL):
				if(!handleMethodCall(line, varReader.getGlobalTable()))
					throw new LocalScopeException("methods hasn't called correctly. line: " + lineNum);
				break;
			default:
				return null;
			}
			line = reader.readLine();
			lineNum++;
			
			if (line == null)
				throw new LocalScopeException("block is not closed. line: " + lineNum);
		}
		lineNumber = lineNum;
		return reader;
	}
	
	/**
	 * reads the declaration line of a scope (method / if / while) verifies it and does necessery action
	 * depend on the type of scope.
	 * @param scopeDeclaration the string line represents the scope declaration.
	 * @throws LocalScopeException in case the scope isn't declared properly.
	 */
	public void readDeclarationLine(String scopeDeclaration, int lineNum, VarReader varReader)
			throws LocalScopeException, VariableException {};
	
	/**
	 * used when meeting a legal return line. validating that no operation
	 * is done after the return line in the same block.
	 * @throws LocalScopeException in case there is some assignment after the return line
	 * @throws IOException
	 */
	public void handleReturn() throws LocalScopeException, IOException {
		String line = reader.readLine();
		while (!BLOCK_CLOSER.matcher(line).matches()) {
			if (!((EMPTY.matcher(line).matches()) || (line.startsWith(COMMENT)))) {
				throw new LocalScopeException(
						"operations after return line in the same block are not allowed");
			}
		}
	}
	
	/**
	 * checking if the arguments in some function call are valid
	 * @param methodDeclaration
	 * @return
	 */
	public boolean handleMethodCall(String methodCall, HashMap<String, Variable> varsTable) {
		matcher = METHOD_CALL.matcher(methodCall);
		matcher.find();
		String methodName = matcher.group(1);
		String calledArgs = "";
		if (matcher.groupCount() > 1) {
			calledArgs = matcher.group(2); 
		}
		
		if (!methodsTable.containsKey(methodName))	//exisiting method check
			return false;
		String[] expectedArgs = methodsTable.get(methodName).getMethodArgTypes();
		Matcher argMatch = ARG_PATTERN.matcher(calledArgs);
		//System.out.println(ARG_PATTERN);
		//System.out.println(calledArgs);
		int numOfArgs = 0;
		while (argMatch.find()) {
			String argName = calledArgs.substring(argMatch.start(), argMatch.end());
			if (!varsTable.containsKey(argName)) {	//existing variable check
				if (EXPLICIT_ARG.matcher(argName).matches())
					return true;
				else
					return false;
			}
			String actualType = varsTable.get(argName).getType();
			if (!actualType.equals(expectedArgs[numOfArgs]))	//valid type check
				return false;
			
			numOfArgs++;
		}
		
		if (!(numOfArgs == expectedArgs.length)) //number of arguments check
			return false;

		return true;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

}
