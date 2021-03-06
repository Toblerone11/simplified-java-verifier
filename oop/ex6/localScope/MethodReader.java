package oop.ex6.localScope;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.HashMap;

import oop.ex6.globalReader.Constants;
import oop.ex6.globalReader.ReaderUnknownRowException;
import oop.ex6.globalReader.Tools;
import oop.ex6.variableReader.VarReader;
import oop.ex6.variableReader.Variable;
import oop.ex6.variableReader.VariableException;

/**
 * this class represents a reader to handle a scope of method definition. this reader has unique
 * methods to handle line which can occur in a method scope only.
 * In addition, this class inherits from LocalScope all the other methods to handle other lines
 * which can appear in all local scopes (if/while scope and method scope for now);
 * 
 *  * @author Omer and Ron
 * 
 */
public class MethodReader extends LocalScope {

	/* constants */
	private static final int TYPE = 0, NAME = 1, CAPTURED_NAME = 2;
	/* String to append to any variable which being declared in a function declaration
	 * in order to pass it to VarReader in a fix format for adding it to the local variables hash table */
	private static final String RE_FORMAT = " = DIMC;";
	
	/* data members */
	private Pattern argsPattern, legalType;
	private Pattern methodPattern;

	
	/* constructor */
	/**
	 * C'tor
	 * @param reader a buffer which read the document line by line
	 */
	public MethodReader() {
		super();
		//argsPattern = Pattern.compile(PARAMETER_PATTERN);
		methodPattern = Pattern.compile(Constants.METHOD_PATTERN);
		argsPattern = Pattern.compile(Constants.METHOD_ARG);
		legalType = Pattern.compile(Constants.LEGAL_TYPE);
		this.methodsTable = new HashMap<String, Method>();
	}

	@Override
	public void readDeclarationLine(String scopeDeclaration, int lineNum, VarReader varReader)
			throws LocalScopeException, VariableException {
//		System.out.println(lineNum + " " + scopeDeclaration);
		/* extract the return type and name of the method */
		matcher = methodPattern.matcher(scopeDeclaration);
		matcher.find();
		String type = matcher.group(1);
		String name = matcher.group(2);
		String args = matcher.group(3);
		
		/* checks arguments */
		matcher = argsPattern.matcher(args);
		ArrayList<String> argTypes = new ArrayList<String>();
		ArrayList<String> argNames = new ArrayList<String>();	
		while (matcher.find()) {
			String[] arg = matcher.group().split("\\s+");
			String argType = arg[TYPE];
			String argName = arg[NAME];
			if (!legalType.matcher(argType).matches())
				throw new MethodException("not valid argument type in line " + lineNum + ": " + arg[TYPE]);
			
			argTypes.add(argType);
			argNames.add(argName);
		}

		String[] methodArgTypes = argTypes.toArray(new String[argTypes.size()]);
		String[] methodArgNames = argNames.toArray(new String[argNames.size()]);
		methodsTable.put(name, new Method(type, name, methodArgTypes, methodArgNames));
	}
	
	@Override
	public BufferedReader checkScope(String currentLine, BufferedReader bReader, int lineNum)
			throws IOException, ReaderUnknownRowException, LocalScopeException, VariableException {
		
		/* checks the declaration of the scope */	
		readDeclarationLine(currentLine, lineNum, varReader);
		return super.checkScope(currentLine, bReader, lineNum);
	}
	
	@Override
	public BufferedReader enterScope(String currentLine, BufferedReader bReader, int lineNum,
			HashMap<String, Variable> varsTable) 
			throws IOException, LocalScopeException, ReaderUnknownRowException, VariableException {
		VarReader varReader = new VarReader(varsTable);
		matcher = methodPattern.matcher(currentLine);
		matcher.find();
		String name = matcher.group(CAPTURED_NAME);
		
		String[] types = this.methodsTable.get(name).getMethodArgTypes();
		String[] names = this.methodsTable.get(name).getMethodArgNames();
		
		addArgsToLocalTable(varReader, types, names, lineNum);
		return super.readScope(currentLine, bReader, lineNum, varReader);
		
	}
		
	/*
	 * re format the string of any argument which being declared in a function declaration
	 * in order to pass it to VarReader in a fix format for adding it to
	 * the hash table of the other variables which has been declared.
	 *  
	 * @param type the type of the argument.
	 * @param name the name of the argument as it had been declared for using it in the method scope
	 * @return a reformatted String of the argument.
	 */
	private String reFormatArgsDeclaration(String type, String name) {
		return type + " " + name + RE_FORMAT; 
	}
	
	/*
	 * a method which using reFormatArgsDeclaration method in order to
	 * iterate over all arguments that had been declared in the method declaration line
	 * and pass them one by one to VarReader in order to add them
	 * to the variable table of the method's scope
	 * @param varReader the VarReader instance that was built for this scope
	 * @param types array of Strings stores all the types of the arguments.
	 * @param names array of Strings stores all the names of the arguments, respectively to the types arry
	 * @param lineNum the cuurent number of line.
	 * @throws VariableException in case some variable name or type are illegal.
	 */
	private void addArgsToLocalTable(VarReader varReader, String[] types, String[] names, int lineNum)
			throws VariableException {
		
		Pattern tempVarPattern = Tools.getVarPattern();
		Matcher tempVarMatcher;
		for (int i = 0; i < types.length; i++) {
			String toAdd = reFormatArgsDeclaration(types[i], names[i]);
			tempVarMatcher = tempVarPattern.matcher(toAdd);
			tempVarMatcher.matches();
			varReader.analyzeLine(toAdd, tempVarMatcher, lineNum);
		}
	}
		
}
