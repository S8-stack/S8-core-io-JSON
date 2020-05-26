package com.s8.lang.joos.type;

public class JOOS_CompilingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748319713950090307L;

	private Class<?> type;
	
	public JOOS_CompilingException(Class<?> type, String message) {
		super(message+", for type="+type);
		this.type = type;
	}
	
	public Class<?> getType(){
		return type;
	}
}
