package oop.ex6.globalReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import oop.ex6.localScope.*;
import oop.ex6.variableReader.VarReader;
import oop.ex6.variableReader.VariableException;




/**
 * Class of the main program reader.
 * 
 * @author Omer and Ron
 *
 */
public class Reader {

	// Data Members:
	private int currentLineNumber;
	private String currentLineData;
	static Matcher variableMatcher;
	private BufferedReader br;
	private MethodReader methodReader;
	private VarReader varReader;

	
	/**
	 * Constructor.
	 * @throws ReaderException Any exception regarding Reader.
	 * @throws VariableException Any exception regarding Variable.
	 * @throws FileNotFoundException File not found Exception.
	 * @throws IOException IO exception.
	 * @throws LocalScopeException Any exception regarding LocalScope.
	 */
	public Reader() throws ReaderException, VariableException, FileNotFoundException,
		IOException, LocalScopeException {
		methodReader = new MethodReader();
		varReader = new VarReader();
	}
	
	
	/**
	 * The main function of the reader. <br>
	 * iterates line line and checks if its legal sjava line.
	 * 
	 * @param fileAbsoulotePath - String of file absolute path.
	 * @return true if exception was not thrown.
	 * @throws IOException - Case of error with BufferedReader.
	 * @throws VariableException - Variable exception.
	 * @throws ReaderUnknownRowException - Case of illegal sjava line.
	 * @throws LocalScopeException - Case of error regarding the method.
	 */
	public boolean read (String fileAbsoulotePath) throws IOException,
		VariableException, ReaderUnknownRowException, LocalScopeException {
		
		try {
			initializeNewBufferedReader(fileAbsoulotePath);
			
			// First Iteration
			while (notEndOfFile(currentLineData)) { 
				int lineType = Tools.checkLine(currentLineData, currentLineNumber);
				switch (lineType) {
				case (Tools.METHOD_DECLARTION):
					br = methodReader.checkScope(currentLineData, br, currentLineNumber);
					currentLineNumber = methodReader.getLineNumber();
					break;
				case (Tools.IF_OR_WHILE):
					throw new LocalScopeException("invalid method declaration in a local scope. line : " + currentLineNumber);
				case (Tools.EMPTY_LINE):
					break;
				case (Tools.COMMENT):
					break;
				case (Tools.VARIABLE):
					varReader.analyzeLine(currentLineData, Tools.getVarMatcher(), currentLineNumber);
					break;
				default:
					throw new ReaderUnknownRowException(currentLineNumber);
				}
				readNextLineAndUpdateLineNumber();		
			}
			initializeNewBufferedReader(fileAbsoulotePath);
			
			//Second Iteration
			while (notEndOfFile(currentLineData)) {
				int lineType = Tools.checkLine(currentLineData, currentLineNumber);
				switch (lineType) {
				case (Tools.METHOD_DECLARTION):
					br = methodReader.readMethodScope(currentLineData, br, currentLineNumber, varReader.getGlobalTable());
					currentLineNumber = methodReader.getLineNumber();
					currentLineNumber++;
				break;
				case (Tools.IF_OR_WHILE):
					throw new LocalScopeException("invalid method declaration in a local scope. line : " + currentLineNumber);
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
					throw new ReaderUnknownRowException(currentLineNumber);
			}
			readNextLineAndUpdateLineNumber();	
		}
		return true;
		}
		finally {
			br.close();
		}
	}
	
	
	/**
	 * Return the number of rows in document.
	 * @return currentLineNumber
	 */
	public int getcurrentLineNumber() {
		return currentLineNumber;
	}

	
	/**
	 * Checks if the current line is null;
	 * @param line Current line in file.
	 * @return true or false
	 */
	private boolean notEndOfFile(String line) {
		return line != null;
	}
	
	
	/**
	 * Initialize new BufferedReader, reads the first line and updates line index. <br>
	 * 
	 * @throws IOException Errors of IO.
	 */
	private void initializeNewBufferedReader(String fileAbsoulotePath) throws IOException {
		br = new BufferedReader(new FileReader(fileAbsoulotePath));
		currentLineData = br.readLine();
		currentLineNumber = 1;
	}
	
	
	/**
	 * Read next line(in BufferReader) and update line number.
	 * @throws IOException 
	 */
	private void readNextLineAndUpdateLineNumber() throws IOException{
		currentLineData = br.readLine();
		currentLineNumber++;
	}
	
	
} 