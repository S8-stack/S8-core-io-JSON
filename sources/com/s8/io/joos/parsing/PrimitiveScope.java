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
public abstract class PrimitiveScope extends ParsingScope {


	
	public PrimitiveScope() {
		super();
		state = new State() {
			
			@Override
			public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
					throws JOOS_ParsingException, IOException {

				String value = null;
				if(reader.is('"')) {
					reader.readNext();
					boolean isAggregating = true;
					while(isAggregating) {
						String chunk = reader.until(new char[]{'"', '\\'}, null, new char[]{});
						value = value!=null ? value.concat(chunk) : chunk;
						
						if(reader.isCurrent('"')) {
							isAggregating = false;
						}
						else { // must be escape backslash
							reader.readNext();
							// and aggregate from the escaped char (inclusive)
						}
					}
					
					reader.readNext();
				}
				else {
					value = reader.until(new char[]{',', '}', ']'}, null, null);	
				}

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


	@Override
	public boolean isClosedBy(char c) {
		return true;
	}

}
