package com.s8.blocks.joos.parsing;

public class JOOS_ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7615059428258340927L;

	public int line;
	
	public int column;
	
	private boolean isCauseCharDefined;
	private char cause;
	
	public JOOS_ParsingException(String message) {
		super(message);
	}
	
	public JOOS_ParsingException(char c, int line, int column, String message) {
		super(message(c,line, column)+" , due to:"+message);
		this.cause = c;
		this.isCauseCharDefined = true;
		this.line = line;
		this.column = column;
	}
	
	public JOOS_ParsingException(int line, int column, String message) {
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
		return "Parsing failed on line:"+line+" ,column:"+column+" , while reading char: >"+c; 
	}
	
	private static String message(int line, int column) {
		return "Parsing failed on line:"+line+" ,column:"+column; 
	}
}
