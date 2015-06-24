package oop.ex6.variableReader;
//import Method;


public class Variable {

	/* constants */
	
	
	/* data members */
	private String isFinal;
	private String type;
	private String name;
	private String value;
	private int rowDecalred;
	private boolean isDefined; // means variable was assigned value;
	
	
	/**
	 * Constructor
	 * @param givenFinal
	 * @param givenType
	 * @param givenName
	 * @param givenValue
	 * @throws Exception
	 */
	public Variable(String givenFinal, String givenType, String givenName, String givenValue, int givenRowDecalred) throws VariableException {
		isFinal = givenFinal;
		type = givenType;
		name = givenName;
		value = givenValue;
		rowDecalred = givenRowDecalred;
		isDefined = false;
		
//		System.out.println(isFinal);
		
		if (getIsFinal() && value == null) {
			throw new VariableFinalIsNotInitializedException(rowDecalred);
		}
		
		
		// Check for variable type mismatch exception
		if (value != null) {
			isDefined = true;
			
			/*
			 * If variables are given the function as parameters
			 * The program treats them as defined variables and
			 * dont check for type mismatch.
			 */
			if (value.equals("DIMC")) { // DIMC = Defined in method call 
//				value = null;
				// Dont check for type mismatch
				
			} else {
				switch (type) {
				case "int":
					if (value.matches("-?\\d+") == false) {
						throw new VariableTypeMismatchException(rowDecalred);
					};
					break;
				case "double":
					if (value.matches("-?\\d+|-?\\d+(\\.)\\d+") == false) {
						throw new VariableTypeMismatchException(rowDecalred);
					}
					break;
				case "String":
					if (!(value.startsWith("\"") && value.endsWith("\""))) {
						throw new VariableTypeMismatchException(rowDecalred);
					}
					break;
				case "boolean":
					if  (! ( (value.equals("false") ) || (value.equals("true") || value.matches("-?\\d+|-?\\d+(\\.)\\d+")) ) ) {
						throw new VariableTypeMismatchException(rowDecalred);
					}
					break;
				case "char":
					if (!(value.startsWith("\'") && value.endsWith("\'") && (value.length()==3))) {
						throw new VariableTypeMismatchException(rowDecalred);
					}
					break;
				default: throw new VariableTypeMismatchException(rowDecalred);
				}
			}
		}

//		System.out.println(toString());
		
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
	
	public void setIsDefined(boolean value) {
		isDefined = value;
	}
	
	
	public String toString() {
		return "Variable name is: " + name + " Type is: " + type +  " Value is: " + value + " Is final: " + getIsFinal() + " isDefined:" + getIsDefined();
		
	}
	
}
