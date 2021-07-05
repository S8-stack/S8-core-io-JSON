package com.s8.blocks.joos.fields;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.composing.JOOS_ComposingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.types.JOOS_CompilingException;



public abstract class FieldHandler {
	
	

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

	/**
	 * 
	 * @return the type to be explored next by the TypeHandler constructor.
	 */
	public abstract Class<?> getSubType();


	public String getName() {
		return name;
	}


	public abstract void subDiscover(JOOS_Lexicon context) throws JOOS_CompilingException;
	
	
	public abstract ParsingScope openScope(Object object);

	public abstract boolean compose(Object object, ComposingScope scope) 
			throws IOException, JOOS_ComposingException;

}
