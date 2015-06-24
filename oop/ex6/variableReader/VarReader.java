package oop.ex6.variableReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Class handles variable sjava line.
 * @author Omer and Ron
 *
 */
public class VarReader {

	// Data Members:
	protected static final String EMPTY_LINE = "";
	protected HashMap<String, Variable> globalVariablesHashMap;
	protected HashMap<String, Variable> localVariablesHashMap;
	protected String variableIsFinal, variableType, caseOfVariables, variableName, variableValue;
	private int i;
	private String[] variablesSplitedByComma;
	private String[] arrayOfOneVariableAndHisValue;
	private ArrayList<Variable> variablesArray;
	
	/**
	 * Default Constructor.
	 */
	public VarReader() {
		globalVariablesHashMap = new HashMap<String, Variable>();
		localVariablesHashMap = new HashMap<String, Variable>();;
		variablesArray = new ArrayList<Variable>();
	}
	
	
	/**
	 * Constructor in case of given hash map.
	 * @param givenGlobalVariablesHashMap
	 */
	public VarReader(HashMap<String, Variable> givenGlobalVariablesHashMap) {
		globalVariablesHashMap = givenGlobalVariablesHashMap;
		localVariablesHashMap = new HashMap<String, Variable>();
		variablesArray = new ArrayList<Variable>();
	}
	
	
	/**
	 * The main method of the class. <br>
	 * Analyze given variables line, if something is wrong,
	 * throws necessary exception.
	 * 
	 * @param givenLine
	 * @param variableMatcher
	 * @param lineNumber
	 * @throws VariableException
	 */
	public void analyzeLine(String givenLine, Matcher variableMatcher, int lineNumber)
			throws VariableException {
		
		organizeCapturingGroups(variableMatcher);
		
		if (variableType == null) { // Means we are updating exist variable
			if (isVariableInsideTable(localVariablesHashMap, variableName)) {
				if (checkIfFinal(localVariablesHashMap,variableName)) {
					throw new VariableFinalCantBeAssignedException(lineNumber);
				} 
			} else {
				if (isVariableInsideTable(globalVariablesHashMap, variableName)) {
					if (checkIfFinal(globalVariablesHashMap,variableName)) {
						throw new VariableFinalCantBeAssignedException(lineNumber);
					}
				} else {
					throw new VariableNotExistException(lineNumber);
				}
			}
			} else {
				if (oneVariableLineCase(variableMatcher)) {

					addVariableToTable(localVariablesHashMap, variableIsFinal, variableType, 
							variableName, variableValue, lineNumber);
					
				} else { // NOT oneVariableLineCase()
					createVariablesArray(localVariablesHashMap, lineNumber);
					addVariablesFromArrayToTable(localVariablesHashMap, lineNumber);
				}
			}
	}
	
	
	// analyzeLine assistant functions - BEGINNING //
	
	
	/**
	 * There are 6 catching groups:
	 * 1.(index = 0) The whole string.
	 * 2.(index = 1) final if exists else null.
	 * 3.(index = 2) the type if exists else null.
	 * 4.(index = 3) Empty(= "") if there is only one variable.
	 * 				 holds all the variables, if there are more than one.
	 * 5.(index = 4) the last variable's name;
	 * 6.(index = 5) the value if the last variable.
	 */
	private void organizeCapturingGroups(Matcher variableMatcher){
		variableIsFinal = variableMatcher.group(1);
		variableType = variableMatcher.group(2);
		caseOfVariables = variableMatcher.group(3);
		variableName = variableMatcher.group(4);
		variableValue = variableMatcher.group(5);
	}
	
	
	/**
	 * Checks if variable has been already inserted into hash map.
	 * @param variableName
	 * @return true/false
	 */
	private boolean isVariableInsideTable(HashMap<String, Variable> table, String variableName) {
		return table.containsKey(variableName);
	}
	
	
	/**
	 * Checks if variable is final.
	 * @param variableName
	 * @return true or false
	 */
	private boolean checkIfFinal(HashMap<String, Variable> table, String variableName) {
		return table.get(variableName).getIsFinal();
	}
	
	
	/**
	 * Case there is only one declared variable in line.
	 * @return true/false.
	 */
	private boolean oneVariableLineCase(Matcher variableMatcher) {
		return variableMatcher.group(3).equals(EMPTY_LINE);
	}
	
	
	/**
	 * Create variable and add it to given table.
	 * @param table
	 * @param variableIsFinal
	 * @param variableType
	 * @param variableName
	 * @param variableValue
	 * @param numberOfRows
	 * @throws VariableException
	 */
	private void addVariableToTable(HashMap<String, Variable> table, String variableIsFinal, String variableType, 
			String variableName, String variableValue, int givenLine) throws VariableException {
		
		if (isVariableInsideTable(table, variableName)) {
			throw new VariableDeclaredBeforeException(givenLine);
			
		} else {
			
			variableValue = checkAndUpdateVariableValueOfAnotherVariable( variableValue, givenLine);
			
			insertVariable(table, createVariable(variableIsFinal,
					variableType,variableName,variableValue, givenLine));
			
			if (table != globalVariablesHashMap) {
				insertVariable(globalVariablesHashMap, createVariable(variableIsFinal,
					variableType,variableName,variableValue, givenLine));
			}
		}
	}
	
	
	/**
	 * Checks the following case: <br>
	 *  type a = value; <br>
	 *  type b = a; <br>
	 *  And does the transfer to the following: <br>
	 *  type b = value;
	 * @return variableValue
	 * @throws VariableUnableToIntializeException 
	 */
	private String checkAndUpdateVariableValueOfAnotherVariable (String variableValue, int givenLine) 
			throws VariableUnableToIntializeException {
		
		if (isVariableInsideTable(globalVariablesHashMap, variableValue)) {
			variableValue = getVariableObjFromHashMapByName(globalVariablesHashMap, variableValue).getValue();
			if (variableValue == null) {
				throw new VariableUnableToIntializeException(givenLine);
			}
		}
		
		return variableValue;
	}
	
	
	/**
	 * Does as its name says.
	 * @return variable
	 */
	private Variable getVariableObjFromHashMapByName(HashMap<String, Variable> table, String variableName) {
		return table.get(variableName);
	}
	
	
	/**
	 * Creates new variable.
	 * @param variableIsFinal
	 * @param variableType
	 * @param variableName
	 * @param variableValue
	 * @param numberOfRows
	 * @return variable.
	 * @throws VariableException
	 */
	private Variable createVariable(String variableIsFinal,String variableType,String variableName,
			String variableValue, int numberOfRows) throws VariableException {
		return new Variable(variableIsFinal,variableType,variableName,variableValue, numberOfRows);
	}

	
	/**
	 * Inserts variable into hash map.
	 * @param givenVariavle
	 */
	private void insertVariable(HashMap<String, Variable> table, Variable givenVariavle) {
		table.put(givenVariavle.getName(), givenVariavle); // Turn into a method
	}
	
	
	/**
	 * Create array of variables if given sjava line contains more than 1 variable.
	 * @param table
	 * @param givenLine
	 * @throws VariableException
	 */
	private void createVariablesArray(HashMap<String, Variable> table, int givenLine) throws VariableException {

		splitDifferentVaribles();
			
		for (i = 0; i < variablesSplitedByComma.length; i++) { // Running over the variables.
			if (variablesSplitedByComma[i].contains("=")) {	// Case variable has value.
				arrayOfOneVariableAndHisValue = variablesSplitedByComma[i].split("=");
				variableValue = arrayOfOneVariableAndHisValue[1];
				if (isVariableInsideTable(table, arrayOfOneVariableAndHisValue[1])) {
					variableValue = getVariableObjFromHashMapByName(table,
							arrayOfOneVariableAndHisValue[1]).getValue();
				}
				variablesArray.add(i, new Variable(variableIsFinal, variableType,
						arrayOfOneVariableAndHisValue[0], variableValue, givenLine));
			} else {	// Case variable do not have value.
				variablesArray.add(i, new Variable(variableIsFinal,variableType, 
						variablesSplitedByComma[i], null, givenLine));
			}
		}
	}
	
	/**
	 * Helper function for createVariablesArray().
	 */
	private void splitDifferentVaribles() {
		if (variableValue == null) {
			caseOfVariables = caseOfVariables + variableName;
		} else {
			caseOfVariables = caseOfVariables + variableName + '=' + variableValue;
		}
		caseOfVariables = caseOfVariables.replaceAll("\\s+", "");
		variablesSplitedByComma = caseOfVariables.split(",");
	}
	
	
	/**
	 * 
	 * @param table
	 * @param givenLine
	 * @throws VariableException
	 */
	private void addVariablesFromArrayToTable(HashMap<String, Variable> table, int givenLine) throws VariableException {
		for (Variable variable : variablesArray) {
			addVariableToTable(table, null, variableType, variable.getName(),
					variable.getValue(), givenLine);
		}
	}
	
	/**
	 * Getter for global variables table.
	 * @return globalVariablesHashMap
	 */
	public HashMap<String, Variable> getGlobalTable() {	
		return globalVariablesHashMap;
	}
	
	// analyzeLine assistant functions - END //
}


