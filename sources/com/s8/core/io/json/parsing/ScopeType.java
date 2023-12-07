package com.s8.core.io.json.parsing;

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
