package oop.ex6.localScope;

/**
 * 
 * @author Omer and Ron
 *
 */
public class Method {

	/* constants */
	/* data members */
	private String returnType;
	private String name;
	private String[] methodArgTypes;
	private String[] methodArgNames;
	
	/**
	 * C'tor
	 * @param returnType the type the method should return.
	 * @param name the name of the method
	 * @param args array strings stores the arguments the method should get in the format of
	 * <type> <arg_name>
	 * @throws MethodException in case unknown type was given;
	 */
	public Method(String returnType, String name, String[] argTypes, String[] argNames) throws MethodException {
		this.name = name;
		this.returnType = returnType;
		this.methodArgTypes = argTypes;
		this.methodArgNames = argNames;
		
	}
	
	/**
	 * getter of the returnType variable
	 * @return the integer which symbolize the return type of the method.
	 */
	public String getReturnType() {
		return returnType;
	}
	
	/**
	 * getter of the method's name.
	 * @return the name of the method.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * getter of a specific argument type the method gets
	 * @return the integer symbolize the type of the desired argument
	 */
	public String getMethodArgType(int argIndex) {
		return this.methodArgTypes[argIndex];
	}
	
	/**
	 * getter of a specific argument name the method gets
	 * @return the integer symbolize the name of the desired argument
	 */
	public String getMethodArgName(int argIndex) {
		return this.methodArgNames[argIndex];
	}
	
	/**
	 * getter of array which stores the types of the arguments the method gets.
	 * @return array of integer symbolize types
	 */
	public String[] getMethodArgTypes() {
		return this.methodArgTypes;
	}
	
	/**
	 * getter of array which stores the names of the arguments the method gets.
	 * @return array of integer symbolize names
	 */
	public String[] getMethodArgNames() {
		return this.methodArgNames;
	}
}
