package oop.ex6.globalReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.localScope.*;
import oop.ex6.variableReader.VarReader;
import oop.ex6.variableReader.VariableException;




/**
 * Iterates line-line while checks validity.
 * 
 * @author Omer & Ron
 *
 */
public class Reader {


	/* Counts the number of rows on first iteration. */
	private int numberOfRows;

	/* Holds the current line in file. */
	private String currentLine;
	
	/*Holds the matchers for variables and methods. */
	static Matcher variableMatcher;

	private BufferedReader br;
	private MethodReader methodReader;
	private VarReader varReader;

	
	public Reader() throws ReaderException, VariableException, FileNotFoundException, IOException, LocalScopeException {
		numberOfRows = 1;
		methodReader = new MethodReader();
		varReader = new VarReader();
	}
	
	public void read (String fileAbsoulotePath) throws IOException,
		VariableException, ReaderUnknownRowException, LocalScopeException {
		
		try {
			br = new BufferedReader(new FileReader(fileAbsoulotePath));
			currentLine = br.readLine();
			
			// First Iteration
			while (notEndOfFile(currentLine)) { 
				int lineType = Tools.checkLine(currentLine, numberOfRows);
				switch (lineType) {
				case (Tools.METHOD_DECLARTION):
					br = methodReader.checkScope(currentLine, br, numberOfRows);
					numberOfRows = methodReader.getLineNumber();
					break;
				case (Tools.IF_OR_WHILE):
					throw new LocalScopeException("invalid method declaration in a local scope. line : " + numberOfRows);
				case (Tools.EMPTY_LINE):
					break;
				case (Tools.COMMENT):
					break;
				case (Tools.VARIABLE):
					varReader.analyzeLine(currentLine, Tools.getVarMatcher(), numberOfRows);
					break;
				default:
					throw new ReaderUnknownRowException(numberOfRows);
				}
				
				currentLine = br.readLine();
				numberOfRows++;				
			}
		br = new BufferedReader(new FileReader(fileAbsoulotePath));
		currentLine = br.readLine();
		numberOfRows = 1;
		
		//Second Iteration
		while (notEndOfFile(currentLine)) {
			int lineType = Tools.checkLine(currentLine, numberOfRows);
			
			switch (lineType) {
			case (Tools.METHOD_DECLARTION):
				br = methodReader.readMethodScope(currentLine, br, numberOfRows, varReader.getGlobalTable());
				numberOfRows = methodReader.getLineNumber();
				numberOfRows++;
				break;
			case (Tools.IF_OR_WHILE):
				throw new LocalScopeException("invalid method declaration in a local scope. line : " + numberOfRows);
			case (Tools.EMPTY_LINE):
				break;
			case (Tools.COMMENT):
				break;
			case (Tools.VARIABLE):
				break;
			case (Tools.RETURN):
				break;
			case (Tools.METHOD_CALL):
				break;
			default:
				throw new ReaderUnknownRowException(numberOfRows);
			}
			
			currentLine = br.readLine();
			numberOfRows++;		
		}
			
			
		System.out.println("0");
		
		}
		finally {
			br.close();
		}
	}
	
	/**
	 * Return the number of rows in document.
	 * @return numberOfRows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	
	/**
	 * Checks if the current line is null;
	 * @param line
	 * @return true or false
	 */
	private boolean notEndOfFile(String line) {
		return line != null;
	}
	
} 