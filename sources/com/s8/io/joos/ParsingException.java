package com.s8.io.joos;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6066459485992721695L;

	public int line;
	
	public int column;
	
	public ParsingException(String message){
		super(message);
	}
	
	public ParsingException(int line, int column, String message){
		super(message);
		this.line = line;
		this.column = column;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getColumn(){
		return column;
	}
}
