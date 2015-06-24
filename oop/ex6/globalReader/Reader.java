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
 * Class of the main program reader. this class manages the whole process in practice.
 * this class knows most of the other classes in other packages.
 * it manages the main iteration over the file and decides how to take care on each of each line.
 * it is bounded to the global scope - global variables and methods declarations.
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
	 * this function performing first iteration over the file. its purpose is to store all
	 * global variables and methods which had been declared legally (otherwise it throws exception).
	 * in addition, this function tries to recognize each line it passes in order to
	 * perform a soft check of syntax over the code. 
	 * 
	 * @throws IOException in case of issues with reading a specific line.
	 * @throws VariableException in case of illegal use in variable. 
	 * @throws ReaderUnknownRowException in case of unrecognizable code line.
	 * @throws LocalScopeException in case of illegal code line in some inner scope
	 */
	public void globalCheckOfFile() throws IOException,
	VariableException, ReaderUnknownRowException, LocalScopeException {
	
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
	}
	
	/**
	 * goes over each line and check if it's recognizeable by the program.
	 * if it's a scope declaration (which from here can occur only by method declaration),
	 * this method activate the MethodReader instance in order to get in the scope and check it.
	 * @throws IOException in case of issues with reading a specific line.
	 * @throws VariableException in case of illegal use in variable. 
	 * @throws ReaderUnknownRowException in case of unrecognizeable code line.
	 * @throws LocalScopeException in case of illegal code line in some inner scope
	 */
	public void comprehensiveCheckOfFile() throws IOException,
	VariableException, ReaderUnknownRowException, LocalScopeException {
		
		while (notEndOfFile(currentLineData)) {
			int lineType = Tools.checkLine(currentLineData, currentLineNumber);
			
			switch (lineType) {
			case (Tools.METHOD_DECLARTION):
				br = methodReader.enterScope(currentLineData, br, currentLineNumber, varReader.getGlobalTable());
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
	}
	
	/**
	 * The main function of the reader. <br>
	 * iterates line by line (twice) and checks if it's a legal sjava file.
	 * first iterartion performs soft check of syntax and its purpose is to store
	 * all the global variables and defined methods.
	 * second iteration get in each local scope and check it rigorously.
	 * 
	 * @param fileAbsoulotePath - String of file absolute path.
	 * @return true if exception was not thrown.
	 * @throws IOException - Case of error with BufferedReader.
	 * @throws VariableException - Variable exception.
	 * @throws ReaderUnknownRowException - Case of illegal sjava line.
	 * @throws LocalScopeException - Case of error regarding the method.
	 */
	public boolean read (String fileAbsoulutePath) throws IOException,
		VariableException, ReaderUnknownRowException, LocalScopeException {
		
		try {
			initializeNewBufferedReader(fileAbsoulutePath);
			globalCheckOfFile();
			
			initializeNewBufferedReader(fileAbsoulutePath);
			comprehensiveCheckOfFile();
			
			//file was checked rigorously!
			return true;
		}
		finally {
			if (br != null)
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