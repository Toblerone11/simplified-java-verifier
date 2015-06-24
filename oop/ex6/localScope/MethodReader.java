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
 * 
 * @author Omer and Ron
 * 
 */
public class MethodReader extends LocalScope {

	/* constants */
	//private static final Pattern METHOD_PATTERN = Pattern.compile(Constants.METHOD_CALL);

	private static final int TYPE = 0, NAME = 1;
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
	
	/**
	 * 
	 * @param currentLine
	 * @param bReader
	 * @param lineNum
	 * @param varsTable
	 * @return
	 * @throws IOException
	 * @throws LocalScopeException
	 * @throws ReaderUnknownRowException
	 * @throws VariableException
	 */
	public BufferedReader readMethodScope(String currentLine, BufferedReader bReader, int lineNum, HashMap<String, Variable> varsTable) 
			throws IOException, LocalScopeException, ReaderUnknownRowException, VariableException {
		VarReader varReader = new VarReader(varsTable);
		matcher = methodPattern.matcher(currentLine);
		matcher.find();
		String type = matcher.group(1);
		String name = matcher.group(2);
		
		String[] types = this.methodsTable.get(name).getMethodArgTypes();
		String[] names = this.methodsTable.get(name).getMethodArgNames();
		
		addArgsToLocalTable(varReader, types, names, lineNum);
		return super.readScope(currentLine, bReader, lineNum, varReader);
		
	}
		
	
	private String reFormatArgsDeclaration(String type, String name) {
		return type + " " + name + RE_FORMAT; 
	}
	
	private void addArgsToLocalTable(VarReader varReader, String[] types, String[] names, int lineNum) throws VariableException {
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
