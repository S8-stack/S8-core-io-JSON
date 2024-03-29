package com.s8.core.io.json.parsing;

import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class JSON_ParsingException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7615059428258340927L;

	public int line;
	
	public int column;
	
	private boolean isCauseCharDefined;
	private char cause;
	
	public JSON_ParsingException(String message) {
		super(message);
	}
	
	public JSON_ParsingException(char c, int line, int column, String message) {
		super(message(c,line, column)+" , due to:"+message);
		this.cause = c;
		this.isCauseCharDefined = true;
		this.line = line;
		this.column = column;
	}
	
	public JSON_ParsingException(int line, int column, String message) {
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
		return "Parsing failed on line:"+line+" ,column:"+column+" , while reading char: >"+c+"(code = "+((int) c)+")"; 
	}
	
	private static String message(int line, int column) {
		return "Parsing failed on line:"+line+" ,column:"+column; 
	}
}
