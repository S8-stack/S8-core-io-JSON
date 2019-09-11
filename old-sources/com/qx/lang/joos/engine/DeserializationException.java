package com.qx.lang.joos.engine;

public class DeserializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6066459485992721695L;

	private int line;
	
	public DeserializationException(int line, String message){
		super(message);
		this.line = line;
	}
	
	public int getLine(){
		return line;
	}
}
