package com.s8.core.io.json.fields.simples;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.JSON_Type;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.ObjectScope;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.types.TypeHandler;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ObjectFieldHandler extends FieldHandler {
	
	
	public static class Builder extends FieldHandler.Builder  {
	
		public Class<?> fieldType;
		
		public final ObjectFieldHandler handler;
		
		public Builder(String name, Field field) {
			super();
			handler = new ObjectFieldHandler(name, field);
			this.fieldType = field.getType();
		}
		

		@Override
		public Class<?> getSubType() {
			return fieldType;
		}
		
		@Override
		public void subDiscover(JSON_Lexicon.Builder context) throws JSON_CompilingException {
			if(fieldType.getAnnotation(JSON_Type.class)!=null) {
				context.discover(fieldType);	
			}
		}
		
		
		@Override
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
			handler.fieldTypeHandler = lexiconBuilder.getByClassName(fieldType);
		}


		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}
	
	

	/**
	 * Default type
	 */
	public TypeHandler fieldTypeHandler;

	private ObjectFieldHandler(String name, Field field) {
		super(name, field);
	}

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}

	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}

	

	@Override
	public boolean compose(Object object, ComposingScope scope) throws JSON_ComposingException, IOException {

		Object value = null;
		try {
			value = field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JSON_ComposingException(e.getMessage());
		}

		if(value!=null) {
			// declare type
			scope.newItem();
			scope.append(name);
			scope.append(':');

			// declare type
			TypeHandler typeHandler = scope.getTypeHandler(value);
			typeHandler.compose(value, scope, typeHandler == this.fieldTypeHandler);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public ParsingScope openScope(Object parent) {
		return new ObjectScope(fieldTypeHandler) {
			public @Override void onParsed(Object child) throws JSON_ParsingException {
				try {
					ObjectFieldHandler.this.set(parent, child);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JSON_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}
}
