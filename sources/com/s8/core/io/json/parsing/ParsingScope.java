package com.s8.core.io.json.parsing;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ParsingScope {
	
	
	
	protected ParsingState state;
	
	

	public ParsingScope() {
		super();
	}



	public abstract ScopeType getType();
	

}
