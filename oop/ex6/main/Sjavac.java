package oop.ex6.main;


import java.io.IOException;

import oop.ex6.globalReader.Reader;
import oop.ex6.globalReader.ReaderException;
import oop.ex6.localScope.LocalScopeException;
import oop.ex6.variableReader.VariableException;


/*
 * pass to variable the method args at declaration for storing at the hash table.
 * getting explicit args at methods.
 * 
 */
public class Sjavac {
	
	public Sjavac() {
		
	}

	public static void main(String[] arguments) {
		//Sjavac manager = new Sjavac();

		try {
			if (arguments.length == 0) {
				throw new SjavacInvalidUsageException(); 
			}
			
			
			Reader reader = new Reader();
			reader.read(arguments[0]); // arguments[0] = file absolute Path.
		
			
		} catch(SjavacException e) {	// no arguments given.
			
		} catch(ReaderException|VariableException|LocalScopeException e){
			System.out.println("1");
		} catch (IOException e) {
			System.out.println("2");
		}
		
		
		


	}
	
		
}