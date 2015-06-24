package oop.ex6.main;


import java.io.IOException;
import oop.ex6.globalReader.Reader;
import oop.ex6.globalReader.ReaderException;
import oop.ex6.localScope.LocalScopeException;
import oop.ex6.variableReader.VariableException;


/**
 * The main class of the program. checks for legality of given sjava file.
 * @author Omer and Ron
 *
 */
public class Sjavac {
	
	// Constants
	public static final String CODE_LEGAL_MESSAGE = "0";
	public static final String CODE_ILLEGAL_MESSAGE = "1";
	public static final String IO_ERROR_MESSAGE = "2";
	
	/**
	 * The class running the program.
	 * @param arguments
	 */
	public static void main(String[] arguments) {

		try {
			if (arguments.length == 0) {
				throw new SjavacInvalidUsageException(); 
			}
			
			Reader reader = new Reader();
			if (reader.read(arguments[0])) {	// arguments[0] = file absolute Path.
				System.out.println(CODE_LEGAL_MESSAGE);
			}
		} catch(SjavacException e) {	// no arguments given.
			
		} catch(ReaderException|VariableException|LocalScopeException e){
			System.out.println(CODE_ILLEGAL_MESSAGE);
		} catch (IOException e) {
			System.out.println(IO_ERROR_MESSAGE);
		}
	}
}