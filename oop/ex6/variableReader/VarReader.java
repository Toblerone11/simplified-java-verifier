package oop.ex6.variableReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class VarReader {

	// Data Members:
	HashMap<String, Variable> globalVariablesHashMap;
	HashMap<String, Variable> localVariablesHashMap;
	private String variableIsFinal, variableType, caseOfVariables, variableName, variableValue;
	
	/* 
	 * Variables for the case of multi-variables declaration
	 *  Examples:
	 *  double a = 1, b = 2, c, d = 4, e=5;
	 */
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
	}
	
	
	public void analyzeLine(String givenLine, Matcher variableMatcher, int lineNumber) throws VariableException {
		organizeCapturingGroups(variableMatcher);
		
		/*
		 * 1.variableType is null
		 * 		1.1. check if in the localTable
		 * 			1.1.1. YES --> check if is final;
		 *  		1.1.2. No -->  check in the globalTable;
		 *  			1.2.1 Yes --> Check if is final
		 *  
		 *  # Works only on local table.
		 *  2. variableType is NOT null
		 *  	2.1 Check in local table, throw exception.
		 * 
		 */
		
		
		
		if (variableType == null) { // Means we are updating exist variable
			if (isVariableInsideTable(localVariablesHashMap, variableName)) {
				if (checkIfFinal(localVariablesHashMap,variableName)) {
					throw new VariableFinalCantBeAssignedException(lineNumber);
				} else {
					// Change variable value in local table.
				}
			} else {
				if (isVariableInsideTable(globalVariablesHashMap, variableName)) {
					if (checkIfFinal(globalVariablesHashMap,variableName)) {
						throw new VariableFinalCantBeAssignedException(lineNumber);
					} else {
						// Change variable value in global table.
					}
				} else {
					throw new VariableNotExistException(lineNumber);
				}
			}
			} else {
				if (oneVariableLineCase(variableMatcher)) {

//					System.out.println("givenLine " + givenLine);
					addVariableToTable(localVariablesHashMap, variableIsFinal, variableType, 
							variableName, variableValue, lineNumber);
				} else { // NOT oneVariableLineCase()
					createVariablesArray(localVariablesHashMap, lineNumber);
					addVariablesFromArrayToTable(localVariablesHashMap, lineNumber);
				}
			}
		
			
		
//		Debugging: print all variables
//		for (i = 1; i < variableMatcher.groupCount()+1; i++) {
//			System.out.println(i+": " + variableMatcher.group(i));
//		}

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
		return variableMatcher.group(3).equals("");
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
			variableValue = checkAndUpdateVariableValueOfAnotherVariable(table, variableValue, givenLine);
//			System.out.println(createVariable(variableIsFinal,
//					variableType,variableName,variableValue, givenLine));
//			System.out.println("bla " + variableValue);
			
			insertVariable(table, createVariable(variableIsFinal,
					variableType,variableName,variableValue, givenLine));
			
			// 
			insertVariable(globalVariablesHashMap, createVariable(variableIsFinal,
					variableType,variableName,variableValue, givenLine));
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
	private String checkAndUpdateVariableValueOfAnotherVariable
	(HashMap<String, Variable> table, String variableValue, int givenLine) 
			throws VariableUnableToIntializeException {
		
//		System.out.println(variableValue); 
//		System.out.println(isVariableInsideTable(localVariablesHashMap, variableValue)); 
//		System.out.println(isVariableInsideTable(globalVariablesHashMap, variableValue)); 
		
		if (isVariableInsideTable(table, variableValue)) {
			variableValue = getVariableObjFromHashMapByName(table, variableValue).getValue();
			if (variableValue == null) {
				throw new VariableUnableToIntializeException(givenLine);
			}
		}
		
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
	 * 
	 * @param table
	 * @param givenLine
	 * @throws VariableException
	 */
	private void createVariablesArray(HashMap<String, Variable> table, int givenLine) throws VariableException {
		if (variableValue == null) {
			caseOfVariables = caseOfVariables + variableName;
		} else {
			caseOfVariables = caseOfVariables + variableName + '='
					+ variableValue;
		}

		caseOfVariables = caseOfVariables.replaceAll("\\s+", "");
		variablesSplitedByComma = caseOfVariables.split(",");

		for (i = 0; i < variablesSplitedByComma.length; i++) {
			if (variablesSplitedByComma[i].contains("=")) {
				arrayOfOneVariableAndHisValue = variablesSplitedByComma[i]
						.split("=");

				variableValue = arrayOfOneVariableAndHisValue[1];
				if (isVariableInsideTable(table, arrayOfOneVariableAndHisValue[1])) {
					variableValue = getVariableObjFromHashMapByName(table,
							arrayOfOneVariableAndHisValue[1]).getValue();
				}
				
//				System.out.println("i " + i);
//				System.out.println("variableIsFinal " + variableIsFinal);
//				System.out.println("variableType " + variableType);
//				System.out.println("arrayOfOneVariableAndHisValue[0] " + arrayOfOneVariableAndHisValue[0]);
//				System.out.println("variableValue " + variableValue);
//				System.out.println("variablesArray " + variablesArray);
				
				variablesArray.add(i, new Variable(variableIsFinal,
						variableType, arrayOfOneVariableAndHisValue[0],
						variableValue, givenLine));
			} else {
				variablesArray.add(i, new Variable(variableIsFinal,
						variableType, variablesSplitedByComma[i], null,
						givenLine));
			}
		}
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
	
	public HashMap<String, Variable> getGlobalTable() {
		

		
		return globalVariablesHashMap;
	}
	
	// analyzeLine assistant functions - END //
}



































//package oop.ex6.variableReader;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.regex.Matcher;
//
//public class VarReader {
//
//	// Data Members:
//	HashMap<String, Variable> globalVariablesHashMap;
//	HashMap<String, Variable> localVariablesHashMap;
//	private String variableIsFinal, variableType, caseOfVariables, variableName, variableValue;
//	
//	/* 
//	 * Variables for the case of multi-variables declaration
//	 *  Examples:
//	 *  double a = 1, b = 2, c, d = 4, e=5;
//	 */
//	private int i;
//	private String[] variablesSplitedByComma;
//	private String[] arrayOfOneVariableAndHisValue;
//	private ArrayList<Variable> variablesArray;
//	
//	/**
//	 * Default Constructor.
//	 */
//	public VarReader() {
//		globalVariablesHashMap = new HashMap<String, Variable>();
//		localVariablesHashMap = new HashMap<String, Variable>();;
//	}
//	
//	
//	/**
//	 * Constructor in case of given hash map.
//	 * @param givenGlobalVariablesHashMap
//	 */
//	public VarReader(HashMap<String, Variable> givenGlobalVariablesHashMap) {
//		globalVariablesHashMap = givenGlobalVariablesHashMap;
//		localVariablesHashMap = new HashMap<String, Variable>();
//	}
//	
//	
//	public void analyzeLine(String givenLine, Matcher variableMatcher, int lineNumber) throws VariableException {
//		organizeCapturingGroups(variableMatcher);
//		
//		/*
//		 * 1.variableType is null
//		 * 		1.1. check if in the localTable
//		 * 			1.1.1. YES --> check if is final;
//		 *  		1.1.2. No -->  check in the globalTable;
//		 *  			1.2.1 Yes --> Check if is final
//		 *  
//		 *  # Works only on local table.
//		 *  2. variableType is NOT null
//		 *  	2.1 Check in local table, throw exception.
//		 * 
//		 */
//		
//		
//		
//		if (variableType == null) { // Means we are updating exist variable
//			if (isVariableInsideTable(localVariablesHashMap, variableName)) {
//				if (checkIfFinal(localVariablesHashMap,variableName)) {
//					throw new VariableFinalCantBeAssignedException(lineNumber);
//				} else {
//					// Change variable value in local table.
//				}
//			} else {
//				if (isVariableInsideTable(globalVariablesHashMap, variableName)) {
//					if (checkIfFinal(globalVariablesHashMap,variableName)) {
//						throw new VariableFinalCantBeAssignedException(lineNumber);
//					} else {
//						// Change variable value in global table.
//					}
//				} else {
//					throw new VariableNotExistException(lineNumber);
//				}
//			}
//			} else {
//				if (oneVariableLineCase(variableMatcher)) {
//
////					System.out.println("givenLine " + givenLine);
//					addVariableToTable(localVariablesHashMap, variableIsFinal, variableType, 
//							variableName, variableValue, lineNumber);
//				} else { // NOT oneVariableLineCase()
//					createVariablesArray(localVariablesHashMap, lineNumber);
//					addVariablesFromArrayToTable(localVariablesHashMap, lineNumber);
//				}
//			}
//		
//			
//		
////		Debugging: print all variables
////		for (i = 1; i < variableMatcher.groupCount()+1; i++) {
////			System.out.println(i+": " + variableMatcher.group(i));
////		}
//
//	}
//	
//	// analyzeLine assistant functions - BEGINNING //
//	
//	/**
//	 * There are 6 catching groups:
//	 * 1.(index = 0) The whole string.
//	 * 2.(index = 1) final if exists else null.
//	 * 3.(index = 2) the type if exists else null.
//	 * 4.(index = 3) Empty(= "") if there is only one variable.
//	 * 				 holds all the variables, if there are more than one.
//	 * 5.(index = 4) the last variable's name;
//	 * 6.(index = 5) the value if the last variable.
//	 */
//	private void organizeCapturingGroups(Matcher variableMatcher){
//		variableIsFinal = variableMatcher.group(1);
//		variableType = variableMatcher.group(2);
//		caseOfVariables = variableMatcher.group(3);
//		variableName = variableMatcher.group(4);
//		variableValue = variableMatcher.group(5);
//	}
//	
//	
//	/**
//	 * Checks if variable has been already inserted into hash map.
//	 * @param variableName
//	 * @return true/false
//	 */
//	private boolean isVariableInsideTable(HashMap<String, Variable> table, String variableName) {
//		return table.containsKey(variableName);
//	}
//	
//	
//	/**
//	 * Checks if variable is final.
//	 * @param variableName
//	 * @return true or false
//	 */
//	private boolean checkIfFinal(HashMap<String, Variable> table, String variableName) {
//		return table.get(variableName).getIsFinal();
//	}
//	
//	
//	/**
//	 * Case there is only one declared variable in line.
//	 * @return true/false.
//	 */
//	private boolean oneVariableLineCase(Matcher variableMatcher) {
//		return variableMatcher.group(3).equals("");
//	}
//	
//	
//	/**
//	 * Create variable and add it to given table.
//	 * @param table
//	 * @param variableIsFinal
//	 * @param variableType
//	 * @param variableName
//	 * @param variableValue
//	 * @param numberOfRows
//	 * @throws VariableException
//	 */
//	private void addVariableToTable(HashMap<String, Variable> table, String variableIsFinal, String variableType, 
//			String variableName, String variableValue, int givenLine) throws VariableException {
//		
//		if (isVariableInsideTable(table, variableName)) {
//			throw new VariableDeclaredBeforeException(givenLine);
//		} else {
//			Variable oldVariable = table.get(variableName);
//			variableValue = checkAndUpdateVariableValueOfAnotherVariable(table, oldVariable, variableValue, givenLine);
////			System.out.println(createVariable(variableIsFinal,
////					variableType,variableName,variableValue, givenLine));
////			System.out.println("bla " + variableValue);
//			
//			insertVariable(table, createVariable(variableIsFinal,
//					variableType,variableName,variableValue, givenLine));
//			
//			// 
//			insertVariable(globalVariablesHashMap, createVariable(variableIsFinal,
//					variableType,variableName,variableValue, givenLine));
//		}
//	}
//	
//	//HashMap<String, Variable> table, Variable originalVariable, String variableValue, int givenLine
//	
//	
//	/**
//	 * Checks the following case: <br>
//	 *  type a = value; <br>
//	 *  type b = a; <br>
//	 *  And does the transfer to the following: <br>
//	 *  type b = value;
//	 * @return variableValue
//	 * @throws VariableUnableToIntializeException 
//	 * @throws VariableTypeMismatchException 
//	 */
////	private String checkAndUpdateVariableValueOfAnotherVariable(HashMap<String, Variable> table, String variableValue, int givenLine, String typeOfOriginalVar) throws VariableUnableToIntializeException {
////		
//////		System.out.println(variableValue); 
//////		System.out.println(isVariableInsideTable(localVariablesHashMap, variableValue)); 
//////		System.out.println(isVariableInsideTable(globalVariablesHashMap, variableValue)); 
////		
////		if (isVariableInsideTable(table, variableValue)) {
////			if (typeOfOriginalVar.equals(anObject)) {
////				variableValue = getVariableObjFromHashMapByName(table, variableValue).getValue();
////				if (variableValue == null) {
////					throw new VariableUnableToIntializeException(givenLine);
////			} else {
////				throw new VariableTypeMismatchException(givenLine);
////			}
////			}
////
////		}
////		
////		if (isVariableInsideTable(globalVariablesHashMap, variableValue)) {
////			variableValue = getVariableObjFromHashMapByName(globalVariablesHashMap, variableValue).getValue();
////			if (variableValue == null) {
////				throw new VariableUnableToIntializeException(givenLine);
////			}
////		}
////		
////		return variableValue;
////	}
//		
//		private String checkAndUpdateVariableValueOfAnotherVariable
//			(HashMap<String, Variable> table, Variable originalVariable, String variableValue, int givenLine)
//					throws VariableUnableToIntializeException, VariableTypeMismatchException {
//			
//			if (isVariableInsideTable(table, variableValue)) {
//				Variable newVarailbe = table.get(variableValue);
//				System.out.println(originalVariable);
//				if ((originalVariable.getType()).equals(newVarailbe.getType())) {
//					if (newVarailbe.getValue() != null) {
//						variableValue = getVariableObjFromHashMapByName(table, variableValue).getValue();
//						originalVariable.setIsDefined(true);
//						return variableValue;
//					} else {
//						throw new VariableUnableToIntializeException(givenLine);
//					}
//				} else {
//					throw new VariableTypeMismatchException(givenLine);
//				}
//			}
//			
//			if (isVariableInsideTable(globalVariablesHashMap, variableValue)) {
//				Variable newVarailbe = globalVariablesHashMap.get(variableValue);
//				if ((originalVariable.getType()).equals(newVarailbe.getType())) {
//					if (newVarailbe.getValue() != null) {
//						variableValue = getVariableObjFromHashMapByName(globalVariablesHashMap, variableValue).getValue();
//						originalVariable.setIsDefined(true);
//						return variableValue;
//					} else {
//						throw new VariableUnableToIntializeException(givenLine);
//					}
//				} else {
//					throw new VariableTypeMismatchException(givenLine);
//				}
//			}
//			return variableValue;
//			
//		}
//		
//		
//		
//
//	
//	
//	/**
//	 * Does as its name says.
//	 * @return variable
//	 */
//	private Variable getVariableObjFromHashMapByName(HashMap<String, Variable> table, String variableName) {
//		return table.get(variableName);
//	}
//	
//	
//	/**
//	 * Creates new variable.
//	 * @param variableIsFinal
//	 * @param variableType
//	 * @param variableName
//	 * @param variableValue
//	 * @param numberOfRows
//	 * @return variable.
//	 * @throws VariableException
//	 */
//	private Variable createVariable(String variableIsFinal,String variableType,String variableName,
//			String variableValue, int numberOfRows) throws VariableException {
//		return new Variable(variableIsFinal,variableType,variableName,variableValue, numberOfRows);
//	}
//
//	
//	/**
//	 * Inserts variable into hash map.
//	 * @param givenVariavle
//	 */
//	private void insertVariable(HashMap<String, Variable> table, Variable givenVariavle) {
//		table.put(givenVariavle.getName(), givenVariavle); // Turn into a method
//	}
//	
//	
//	/**
//	 * 
//	 * @param table
//	 * @param givenLine
//	 * @throws VariableException
//	 */
//	private void createVariablesArray(HashMap<String, Variable> table, int givenLine) throws VariableException {
//		if (variableValue == null) {
//			caseOfVariables = caseOfVariables + variableName;
//		} else {
//			caseOfVariables = caseOfVariables + variableName + '='
//					+ variableValue;
//		}
//
//		caseOfVariables = caseOfVariables.replaceAll("\\s+", "");
//		variablesSplitedByComma = caseOfVariables.split(",");
//
//		for (i = 0; i < variablesSplitedByComma.length; i++) {
//			if (variablesSplitedByComma[i].contains("=")) {
//				arrayOfOneVariableAndHisValue = variablesSplitedByComma[i]
//						.split("=");
//
//				variableValue = arrayOfOneVariableAndHisValue[1];
//				if (isVariableInsideTable(table, arrayOfOneVariableAndHisValue[1])) {
//					variableValue = getVariableObjFromHashMapByName(table,
//							arrayOfOneVariableAndHisValue[1]).getValue();
//				}
//
//				variablesArray.add(i, new Variable(variableIsFinal,
//						variableType, arrayOfOneVariableAndHisValue[0],
//						variableValue, givenLine));
//			} else {
//				variablesArray.add(i, new Variable(variableIsFinal,
//						variableType, variablesSplitedByComma[i], null,
//						givenLine));
//			}
//		}
//	}
//
//	
//	/**
//	 * 
//	 * @param table
//	 * @param givenLine
//	 * @throws VariableException
//	 */
//	private void addVariablesFromArrayToTable(HashMap<String, Variable> table, int givenLine) throws VariableException {
//		for (Variable variable : variablesArray) {
//			addVariableToTable(table, null, variableType, variable.getName(),
//					variable.getValue(), givenLine);
//		}
//	}
//	
//	public HashMap<String, Variable> getGlobalTable() {
//		
//
//		
//		return globalVariablesHashMap;
//	}
//	
//	// analyzeLine assistant functions - END //
//}
