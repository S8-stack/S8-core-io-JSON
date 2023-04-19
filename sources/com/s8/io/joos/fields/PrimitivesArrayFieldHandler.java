package com.s8.io.joos.fields;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class PrimitivesArrayFieldHandler extends FieldHandler {
	
	public static class Builder extends FieldHandler.Builder {
		
		public PrimitivesArrayFieldHandler handler;
		
		private Class<?> componentType;

		public Builder(Field field) {
			super();
			componentType = field.getType().getComponentType();
		}

		@Override
		public Class<?> getSubType() {
			return componentType;
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
	
	

	

	public PrimitivesArrayFieldHandler(String name, Field field) {
		super(name, field);
		
	}

	/**
	 * 
	 * @param object
	 * @param values the array of values
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object values) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, values);
	}


	/**
	 * 
	 * @param object the parent object
	 * @param writer
	 * @throws IOException
	 * @throws JOOS_ComposingException 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public boolean compose(Object object, ComposingScope scope) throws IOException, JOOS_ComposingException {

		// retrieve array
		Object array;
		try {
			array = field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		
		if(array!=null) {

			scope.newItem();

			// field description

			scope.append(name);
			scope.append(": ");
			
			int length = Array.getLength(array);
			
			ComposingScope enclosedScope = scope.enterSubscope('[', ']', false);


			// write field
			enclosedScope.open();
			for(int index=0; index<length; index++) {

				if(isItemValid(array, index)) {
					enclosedScope.newItem();
					composeItem(array, index, enclosedScope);					
				}			
			}
			enclosedScope.close();

			return true;
		}
		else {
			return false;
		}
	}


	/**
	 * @param array
	 * @param index 
	 * @return flag indicating whether has produced something
	 */
	public abstract boolean isItemValid(Object array, int index);

	/**
	 * 
	 * @param array
	 * @param index
	 * @param writer
	 * @throws IOException 
	 */
	public abstract void composeItem(Object array, int index, ComposingScope writer) throws IOException;
}
