package com.s8.io.joos.parsing;

public enum ScopeType {

	/** when in map-type scope, expected declarator before all definition */
	MAPPED, 
	
	/** direct list (no declarator) */
	LISTED,
	
	/** */
	PRIMITIVE,
	
	/** */
	OBJECT;
}
