package com.s8.core.io.joos.fields;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.core.io.joos.JOOS_Lexicon;
import com.s8.core.io.joos.composing.ComposingScope;
import com.s8.core.io.joos.composing.JOOS_ComposingException;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveFieldHandler extends FieldHandler {

	public static class Builder extends FieldHandler.Builder {
		
		public PrimitiveFieldHandler handler;
		

		@Override
		public Class<?> getSubType() {
			return null;
		}
		
		@Override
		public void subDiscover(JOOS_Lexicon.Builder context) {
			// nothing to sub-discover
		}

		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @param field
	 */
	protected PrimitiveFieldHandler(String name, Field field) {
		super(name, field);
	}


	/**
	 * 
	 * @param object
	 * @param value
	 * @throws ParsingException 
	 * @throws Exception
	 */
	//public abstract void parse(Object object, String value) throws JOOS_ParsingException;
	
	

	
	
	/**
	 * 
	 * @param object
	 * @param writer
	 * @return has actually output something
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws JOOS_ComposingException 
	 */
	@Override
	public abstract boolean compose(Object object, ComposingScope scope) throws IOException, JOOS_ComposingException;


	
}
