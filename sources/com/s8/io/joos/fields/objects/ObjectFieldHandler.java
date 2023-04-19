package com.s8.io.joos.fields.objects;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.JOOS_Type;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.FieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ObjectScope;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.types.JOOS_CompilingException;
import com.s8.io.joos.types.TypeHandler;


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
		public void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException {
			if(fieldType.getAnnotation(JOOS_Type.class)!=null) {
				context.discover(fieldType);	
			}
		}
		
		
		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
			handler.fieldType = lexiconBuilder.getByClassName(fieldType);
		}


		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}
	
	

	/**
	 * 
	 */
	public TypeHandler fieldType;

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
	public boolean compose(Object object, ComposingScope scope) throws JOOS_ComposingException, IOException {

		Object value = null;
		try {
			value = field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}

		if(value!=null) {
			// declare type
			scope.newItem();
			scope.append(name);
			scope.append(':');

			// declare type
			TypeHandler typeHandler = scope.getTypeHandler(value);
			typeHandler.compose(value, scope);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public ParsingScope openScope(Object parent) {
		return new ObjectScope(fieldType) {
			public @Override void onParsed(Object child) throws JOOS_ParsingException {
				try {
					ObjectFieldHandler.this.set(parent, child);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}
}
