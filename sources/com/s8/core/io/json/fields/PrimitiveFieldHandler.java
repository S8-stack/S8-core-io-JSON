package com.s8.core.io.json.fields;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;

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
		public void subDiscover(JSON_Lexicon.Builder context) {
			// nothing to sub-discover
		}

		@Override
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
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
	 * @throws JSON_ComposingException 
	 */
	@Override
	public abstract boolean compose(Object object, ComposingScope scope) throws IOException, JSON_ComposingException;


	
}
