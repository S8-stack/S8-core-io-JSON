package com.qx.lang.v2;

public class Ws3dParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7615059428258340927L;

	public int line;
	
	public int column;
	
	public Ws3dParsingException(String message) {
		super(message);
	}
	
	public Ws3dParsingException(int line, int column, String message) {
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

	public void acquire(Ws3dStreamReader reader) {
		this.line = reader.line;
		this.column = reader.column;
	}

}
