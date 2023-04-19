package com.s8.io.joos.fields;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.types.JOOS_CompilingException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class FieldHandler {
	
	
	public static abstract class Builder {
		

		/**
		 * 
		 * @return the type to be explored next by the TypeHandler constructor.
		 */
		public abstract Class<?> getSubType();


		public abstract void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException;
		
		
		/**
		 * 
		 * @param lexiconBuilder
		 */
		public abstract void compile(JOOS_Lexicon.Builder lexiconBuilder);


		/**
		 * 
		 * @return
		 */
		public abstract FieldHandler getHandler();
		
	}
	

	/**
	 * 
	 */
	public String name;

	/**
	 * 
	 */
	public Field field;

	
	public FieldHandler(String name, Field field) {
		super();
		this.name = name;
		this.field = field;
	}
	

	/**
	 * 
	 * @param deserializer
	 * @param object
	 * @param buffer
	 * @param lineCount 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws Exception
	 */
	
	/*
	public abstract void set(
			Deserializer deserializer,
			Map<String, Ws3dTypeHandler> handlers,
			Object object,
			Ws3dStreamReader buffer) throws
			DeserializationException,
			IllegalAccessException,
			IllegalArgumentException;
			*/

	public String getName() {
		return name;
	}


	
	public abstract ParsingScope openScope(Object object);

	public abstract boolean compose(Object object, ComposingScope scope) 
			throws IOException, JOOS_ComposingException;

}
