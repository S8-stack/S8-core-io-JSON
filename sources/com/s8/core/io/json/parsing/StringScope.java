package com.s8.core.io.json.parsing;

import java.io.IOException;

import com.s8.core.io.json.ParsingException;


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
public abstract class StringScope extends ParsingScope {



	public StringScope() {
		super();
		state = new ParsingState() {

			@Override
			public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
					throws JSON_ParsingException, IOException {

				reader.skip('\n', '\t', ' ');
				
				
				String value = null;
				
				/* quoted section */
				if(reader.is('"')) {
					value = reader.readQuotedChain();
				}
				else if(reader.isAlphanumeric()) {
					value = reader.readAlphanumericChain();	
				}
				else {
					throw new JSON_ParsingException(reader.line, reader.column, "Must be a quoted or alpha numeric chain");
				}


				/*
				 *  alphanumeric chain has been consumed 
				 *  + 1 char (first non alphanumeric char)
				 */

				try{
					setValue(value);
				}
				catch (Exception e) {
					throw new JSON_ParsingException(reader.line, reader.column, 
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
	 * @throws JSON_ParsingException
	 * @throws ParsingException
	 */
	public abstract void setValue(String value) throws JSON_ParsingException, ParsingException;


	@Override
	public ScopeType getType() {
		return ScopeType.PRIMITIVE;
	}



}
