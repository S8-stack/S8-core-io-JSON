package com.s8.io.joos.parsing;

import java.io.IOException;

import com.s8.io.joos.ParsingException;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * @author pierreconvert
 *
 */
public abstract class AlphaNumericScope extends ParsingScope {


	
	public AlphaNumericScope() {
		super();
		state = new ParsingState() {
			
			@Override
			public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
					throws JOOS_ParsingException, IOException {

				reader.skip('\n', '\t', ' ');
				if(!reader.isAlphanumeric()){
					throw new JOOS_ParsingException(reader.line, reader.column, "Must be alpha numeric chain");
				}
				String value = reader.readAlphanumericChain();

				/*
				 *  alphanumeric chain has been consumed 
				 *  + 1 char (first non alphanumeric char)
				 */
				
				try{
					setValue(value);
				}
				catch (Exception e) {
					throw new JOOS_ParsingException(reader.line, reader.column, 
							"Cannot set Object due to "+e.getMessage()+", on string="+value);
				}
				
				/* pop this scope */
				parser.popScope();
				
				/* stop reading */
				return false;
			}
		};
	}
	
	
	/**
	 * 
	 * @param value
	 * @throws JOOS_ParsingException
	 * @throws ParsingException
	 */
	public abstract void setValue(String value) throws JOOS_ParsingException, ParsingException;
	

	@Override
	public ScopeType getType() {
		return ScopeType.PRIMITIVE;
	}



}
