package com.s8.core.io.json.fields.lists;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.JSON_Type;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.parsing.ArrayScope;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.ObjectScope;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.types.TypeHandler;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjectsListFieldHandler extends FieldHandler {

	
	public static class Builder extends FieldHandler.Builder {
		
		
		public ObjectsListFieldHandler handler;
		
		public Builder(String name, Field field, Class<?> componentType) {
			handler = new ObjectsListFieldHandler(name, field, componentType);
		}
		

		@Override
		public Class<?> getSubType() {
			return handler.componentType;
		}


		@Override
		public void subDiscover(JSON_Lexicon.Builder context) throws JSON_CompilingException {

			/*
			 * discover component type if annotated (compatibility with generic)
			 */
			
			if(handler.componentType !=null && handler.componentType.getAnnotation(JSON_Type.class)!=null) {
				context.discover(handler.componentType);	
			}
		}


		@Override
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
			handler.componentTypeHandler = lexiconBuilder.getByClassName(handler.componentType);
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}
	
	
	

	private Class<?> componentType;
	
	public TypeHandler componentTypeHandler;

	public ObjectsListFieldHandler(String name, Field field, Class<?> componentType) {
		super(name, field);
		this.componentType = componentType;
	}

	public void set(Object object, Object value) throws JSON_ParsingException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JSON_ParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}


	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, JSON_ComposingException {

		// retrieve array
		List<Object> list = null;
		try {
			list = (List<Object>) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new JSON_ComposingException(e.getMessage());
		}


		if(list!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');

			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);

			enclosedScope.open();
			for(Object item : list) {
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
			public ParsingScope openItemScope() throws JSON_ParsingException {
				return new ObjectScope(componentTypeHandler) {
					@Override
					public void onParsed(Object object) throws JSON_ParsingException {
						values.add(object);
					}
				};
			}
			
			@Override
			public void close() throws JSON_ParsingException {
				ObjectsListFieldHandler.this.set(object, values);
			}
		};
	}
}