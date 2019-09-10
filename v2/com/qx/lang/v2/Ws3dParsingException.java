package com.qx.lang.v2;

import com.qx.lang.v2.parsing.StreamReader;

public class Ws3dParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7615059428258340927L;

	public int line;
	
	public int column;
	
	private boolean isCauseCharDefined;
	private char cause;
	
	public Ws3dParsingException(String message) {
		super(message);
	}
	
	public Ws3dParsingException(char c, int line, int column, String message) {
		super(message(c,line, column)+" , due to:"+message);
		this.cause = c;
		this.isCauseCharDefined = true;
		this.line = line;
		this.column = column;
	}
	
	public Ws3dParsingException(int line, int column, String message) {
		super(message(line, column)+" , due to:"+message);
		this.line = line;
		this.column = column;
	}
	
	
	public boolean isCauseCharDefined() {
		return isCauseCharDefined;
	}
	
	public char getFaultyChar() {
		return cause;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getColumn(){
		return column;
	}

	public void acquire(StreamReader reader) {
		this.line = reader.line;
		this.column = reader.column;
	}

	private static String message(char c, int line, int column) {
		return "Parsing failed on line:"+line+" ,column:"+column+" , while reading char:"+c; 
	}
	
	private static String message(int line, int column) {
		return "Parsing failed on line:"+line+" ,column:"+column; 
	}
}
