package com.s8.core.io.joos.fields.arrays;


import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.s8.core.io.joos.JOOS_Lexicon;
import com.s8.core.io.joos.JOOS_Type;
import com.s8.core.io.joos.composing.ComposingScope;
import com.s8.core.io.joos.composing.JOOS_ComposingException;
import com.s8.core.io.joos.fields.FieldHandler;
import com.s8.core.io.joos.parsing.ArrayScope;
import com.s8.core.io.joos.parsing.JOOS_ParsingException;
import com.s8.core.io.joos.parsing.ObjectScope;
import com.s8.core.io.joos.parsing.ParsingScope;
import com.s8.core.io.joos.types.JOOS_CompilingException;
import com.s8.core.io.joos.types.TypeHandler;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ObjectsArrayFieldHandler extends FieldHandler {
	
	public static class Builder extends FieldHandler.Builder {
		
		public ObjectsArrayFieldHandler handler;
		
		public Builder(String name, Field field) {
			super();
			this.handler = new ObjectsArrayFieldHandler(name, field);
		}
		
		@Override
		public Class<?> getSubType() {
			return handler.componentType;
		}


		@Override
		public void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException {
			
			/*
			 * discover component type if annotated (compatibility with generic)
			 */
			if(handler.componentType.getAnnotation(JOOS_Type.class)!=null) {
				context.discover(handler.componentType);	
			}
		}
		
		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
			handler.componentTypeHandler = lexiconBuilder.getByClassName(handler.componentType);
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}


	public Class<?> componentType;
	
	public TypeHandler componentTypeHandler;


	public ObjectsArrayFieldHandler(String name, Field field) {
		super(name, field);
		this.componentType = field.getType().getComponentType();
	}

	public void set(Object object, Object valuesArray) throws JOOS_ParsingException {
		try {
			field.set(object, valuesArray);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}


	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}


	

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

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');
			
			int length = Array.getLength(array);

			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);
			
			enclosedScope.open();
			Object item;
			for(int index=0; index<length; index++) {
				item = Array.get(array, index);
				if(item!=null) {
					
					enclosedScope.newItem();
					TypeHandler typeHandler = enclosedScope.getTypeHandler(item);
					typeHandler.compose(item, enclosedScope, typeHandler == this.componentTypeHandler);					
				}			
			}
			enclosedScope.close();
			return true;
		}
		else {
			return false;
		}
	}

	
	@Override
	public ParsingScope openScope(Object object) {
		
		return new ArrayScope() {
			
			private List<Object> values = new ArrayList<>();
			
			@Override
			public ParsingScope openItemScope() throws JOOS_ParsingException {
				return new ObjectScope(componentTypeHandler) {
					@Override
					public void onParsed(Object object) throws JOOS_ParsingException {
						values.add(object);
					}
				};
			}
			
			@Override
			public void close() throws JOOS_ParsingException {
				int length = values.size();
				Object array = Array.newInstance(componentType, length);
				for(int index=0; index<length; index++) {
					Array.set(array, index, values.get(index));
				}
				ObjectsArrayFieldHandler.this.set(object, array);
			}
		};
	}
}