package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.qx.lang.joos.JOOS_Context;
import com.qx.lang.joos.composing.ComposingScope;



public abstract class FieldHandler {
	
	public enum ScopeType {
		PRIMITIVE, OBJECT, PRIMITIVES_ARRAY, OBJECTS_ARRAY;
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

	/**
	 * 
	 * @return the type to be explored next by the TypeHandler constructor.
	 */
	public abstract Class<?> getSubType();


	public abstract ScopeType getScopeType();


	public String getName() {
		return name;
	}


	public abstract void subDiscover(JOOS_Context context);

	public abstract boolean compose(Object object, ComposingScope scope) 
			throws IOException, IllegalArgumentException, IllegalAccessException;

}
