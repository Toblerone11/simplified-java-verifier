package oop.ex6.variableReader;

import oop.ex6.globalReader.Constants;

/**
 * Class representing any sjava variable.
 * @author Omer and Ron
 *
 */
public class Variable {

	/* data members */
	protected String isFinal;
	protected String type;
	protected String name;
	protected String value;
	protected int rowDecalred;
	protected boolean isDefined; // means value was assigned;
	
	
	/**
	 * Constructor
	 * @param givenFinal
	 * @param givenType
	 * @param givenName
	 * @param givenValue
	 * @throws Exception
	 */
	public Variable(String givenFinal, String givenType, String givenName, String givenValue, int givenRowDecalred)
			throws VariableException {
		isFinal = givenFinal;
		type = givenType;
		name = givenName;
		value = givenValue;
		rowDecalred = givenRowDecalred;
		isDefined = false;
		
		
		if (getIsFinal() && value == null) {
			throw new VariableFinalIsNotInitializedException(rowDecalred);
		}
		
		
		if (value != null) {
			isDefined = true;
			
			if (value.equals(Constants.DIMC)) { // DIMC = Defined in method call. More in README.
				// Do not check for type mismatch
				
			} else { // Check for type mismatch.
				checkIfValueInCorrectType();
			}
		}

		
	}
	
	/* methods */
	
	
	/**
	 * type getter
	 * @return the type of the variable;
	 */
	public String getType() {
		return type;
	}
	
	
	/**
	 * name getter
	 * @return the name of the variable;
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * isFinal getter
	 * @return true if the variable is mutable, false otherwise.
	 */
	public boolean getIsFinal() {
		if (isFinal == null) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * value getter.
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	
	
	/**
	 * isDefined getter.
	 * @return isDefined
	 */
	public boolean getIsDefined() {
		return isDefined;
	}
	
	
	/**
	 * Setter for isDefined.
	 * @param value
	 */
	public void setIsDefined(boolean value) {
		isDefined = value;
	}
	
	
	/**
	 * Checks if value given to variable is in the correct format.
	 * @throws VariableTypeMismatchException
	 */
	private void checkIfValueInCorrectType() throws VariableTypeMismatchException {
		switch (type) {
		case Constants.INT_TYPE:
			if (value.matches(Constants.INT_TYPE_REGEX) == false ) {
				throw new VariableTypeMismatchException(rowDecalred);
			};
			break;
		case Constants.DOUBLE_TYPE:
			if (value.matches(Constants.DOUBLE_TYPE_REGEX) == false ) {
				throw new VariableTypeMismatchException(rowDecalred);
			}
			break;
		case Constants.STRING_TYPE:
			if (!(value.startsWith(Constants.STRING_BEGINNING_CHAR) && value.endsWith(Constants.STRING_ENDING_CHAR))) {
				throw new VariableTypeMismatchException(rowDecalred);
			}
			break;
		case Constants.BOOLEAN_TYPE:
			if  (! ( (value.equals(Constants.FALSE) ) || (value.equals(Constants.TRUE) || 
					value.matches(Constants.DOUBLE_TYPE_REGEX)) ) ) {
				throw new VariableTypeMismatchException(rowDecalred);
			}
			break;
		case Constants.CHAR_TYPE:
			if (!(value.startsWith(Constants.CHAR_BEGINNING_CHAR) && value.endsWith(Constants.CHAR_ENDING_CHAR) &&
					(value.length()==3))) {
				throw new VariableTypeMismatchException(rowDecalred);
			}
			break;
		default: throw new VariableTypeMismatchException(rowDecalred);
		}
	}
	

	public String toString() {
		return "Variable name is: " + name + " Type is: " + 
				type +  " Value is: " + value + " Is final: " + getIsFinal() +
				" isDefined:" + getIsDefined();
	}
	
	
}
