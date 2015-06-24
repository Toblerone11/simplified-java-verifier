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
 * 
 * @author Omer and Ron
 *
 */
public class ConditionBlock extends LocalScope {

	
	/* constants */
	private static final String INT = "int", DOUBLE = "double", BOOLEAN = "boolean";
	private static final Pattern CONDITION_BLOCK_PATTERN = Pattern.compile(Constants.COND_DECLARE + Constants.OPT_SPACE);
	private static final Pattern NUMBER_COND = Pattern.compile("\\s*" + Constants.NUMBER + "\\s*");
	private static final Pattern VAR_COND = Pattern.compile("\\s*(" + Constants.LEGAL_NAME + "|" + Constants.NUMBER + ")\\s*");
	private static final Pattern COMPLEX_COND = Pattern.compile(Constants.CONDITION);
	
	public ConditionBlock() {
		super();
	}
	
	public ConditionBlock(HashMap<String, Method> methodsTable) {
		this.methodsTable = methodsTable;
	}

	@Override
	public BufferedReader checkScope(String currentLine, BufferedReader bReader, int lineNum)
			throws IOException, ReaderUnknownRowException, LocalScopeException, VariableException {
		
		/* checks the declaration of the scope */	
		readDeclarationLine(currentLine, lineNum, varReader);
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
			String varName = condition.substring(matcher.start(), matcher.end());
			if ((!isExplicitLegalVar(varName)) && (!isLegalBoolean(varName, varReader.getGlobalTable())))
				throw new LocalScopeException("not legal boolean value in line " + lineNum +": " + varName);
		}
	}
	
//	/**
//	 * checks if the condition in the declaration line is valid
//	 * @param declaration the declaration line.
//	 * @return true if the declaration is valid, false otherwise.
//	 */
//	private boolean validateDeclareLine(String declaration, VarReader varReader) {
//		Matcher matcher = CONDITION_BLOCK_PATTERN.matcher(declaration);
//		
//		matcher.find();
//		String condition = matcher.group(1);
//		
//		matcher = VAR_COND.matcher(condition);
//		while (matcher.find()) {
//			String varName = condition.substring(matcher.start(), matcher.end());
//		
//			if ((!isExplicitLegalVar(varName)) && (!isLegalBoolean(varName, varReader.getGlobalTable())))
//				throw new LocalScopeException("not legal boolean value in line " + lineNum +": " + varName);
//		}
//
//		else if (COMPLEX_COND.matcher(condition).matches()) {
//			matcher = VAR_COND.matcher(condition);
//			while (matcher.find()) {
//				String varName = condition.substring(matcher.start(), matcher.end());
//				if (!isVarLegalBoolean(varName, varReader.getGlobalTable()))
//					return false;
//				
//	}
			
	/**
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
	public BufferedReader readConditionScope(String currentLine, BufferedReader bReader, int lineNum, HashMap<String, Variable> varsTable) 
			throws IOException, LocalScopeException, ReaderUnknownRowException, VariableException {
		VarReader varReader = new VarReader(varsTable);
		readDeclarationLine(currentLine, lineNum, varReader);
		
		return super.readScope(currentLine, bReader, lineNum, varReader);
		
	}
}

