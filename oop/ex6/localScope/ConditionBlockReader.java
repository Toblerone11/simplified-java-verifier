package oop.ex6.localScope;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.*;

import oop.ex6.globalReader.Constants;
import oop.ex6.globalReader.ReaderUnknownRowException;
import oop.ex6.variableReader.VarReader;
import oop.ex6.variableReader.Variable;
import oop.ex6.variableReader.VariableException;


/**
 * this class represents a reader to handle a scope of condition - 'if' or 'while'. this reader has unique
 * methods to handle the condition line itself.
 * In addition, this class inherits from LocalScope all the other methods to handle other lines
 * which can appear in all local scopes (if/while scope and method scope for now);
 * 
 * @author Omer and Ron
 *
 */
public class ConditionBlockReader extends LocalScope {
	
	/* constants */
	private static final String INT = "int", DOUBLE = "double", BOOLEAN = "boolean";
	private static final Pattern CONDITION_BLOCK_PATTERN = Pattern.compile(Constants.COND_DECLARE + Constants.OPT_SPACE);
	private static final Pattern NUMBER_COND = Pattern.compile("\\s*" + Constants.NUMBER + "\\s*");
	private static final Pattern VAR_COND = Pattern.compile("\\s*(" + Constants.LEGAL_NAME 
			+ "|" + Constants.NUMBER + ")\\s*");
	
	public ConditionBlockReader() {
		super();
	}
	
	/**
	 * C'tor. build a new Instance for some inner scope in the code, initialized with the methods
	 * that was declared in the global scope.
	 * @param methodsTable hash map with the names of methods matched to Method's instances.
	 */
	public ConditionBlockReader(HashMap<String, Method> methodsTable) {
		this.methodsTable = methodsTable;
	}

	@Override
	public BufferedReader checkScope(String currentLine, BufferedReader bReader, int lineNum)
			throws IOException, ReaderUnknownRowException, LocalScopeException, VariableException {
		
		/* checks the declaration of the scope */
		return super.checkScope(currentLine, bReader, lineNum);
	}
	
	@Override
	public void readDeclarationLine(String scopeDeclaration, int lineNum, VarReader varReader)
			throws LocalScopeException, VariableException {
		
		Matcher matcher = CONDITION_BLOCK_PATTERN.matcher(scopeDeclaration);
		matcher.find();
		String condition = matcher.group(1);
		if (EMPTY.matcher(condition).matches())
			throw new LocalScopeException("condition was not given in line " + lineNum);
		
		matcher = VAR_COND.matcher(condition);	
		while (matcher.find()) {
			String varName = matcher.group(1);
			if ((!isExplicitLegalVar(varName)) && (!isLegalBoolean(varName, varReader.getGlobalTable())))
				throw new LocalScopeException("not legal boolean value in line " + lineNum +": " + varName);
		}
	}
			
	/*
	 * used to check if some variable already been declared
	 * and if its type is of boolean, int or double.
	 * @param varName the name of the variable to check
	 * @return true if the var exists as int, double or boolean, false otherwise.
	 */
	private boolean isLegalBoolean(String varName, HashMap<String, Variable> varsTable) {
		if (varsTable.containsKey(varName)) {
			Variable booleanVar = varsTable.get(varName);
			String type = booleanVar.getType();
			if ((type.equals(INT)) || (type.equals(DOUBLE)) || (type.equals(BOOLEAN))) {
				if (!(booleanVar.getValue() == null))
					return true;
			}
		}
		return false;
	}
	
	/*
	 * check if the given argument is not a variable - but explicit value:
	 * number of type int or double, or boolean value (true or false)
	 * @param varName the given variable to check
	 * @return true if legal boolean value, false otherwise.
	 */
	private boolean isExplicitLegalVar(String valName) {

		//check if the given argument is not a variable - but explicit value;
		if (NUMBER_COND.matcher(valName).matches()) {
			return true;
		}
		else if ((valName.contains("true")) || (valName.contains("false")))
			return true;
		
		return false;
	}

	@Override
	public BufferedReader enterScope(String currentLine, BufferedReader bReader, int lineNum,
			HashMap<String, Variable> varsTable) 
			throws IOException, LocalScopeException, ReaderUnknownRowException, VariableException {
		VarReader varReader = new VarReader(varsTable);
		readDeclarationLine(currentLine, lineNum, varReader);
		
		return super.readScope(currentLine, bReader, lineNum, varReader);
	}
}

