package com.s8.io.joos.fields.objects;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.JOOS_Type;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.SimpleFieldHandler;
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
public class ObjectFieldHandler extends SimpleFieldHandler {
	
	
	public static class Builder extends SimpleFieldHandler.Builder  {
	
		public Class<?> fieldType;
		
		public final ObjectFieldHandler handler;
		
		public Builder(String name, Class<?> fieldType) {
			super();
			handler = new ObjectFieldHandler(name);
			this.fieldType = fieldType;
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
		public SimpleFieldHandler getHandler() {
			return handler;
		}
	}
	
	

	/**
	 * 
	 */
	public TypeHandler fieldType;

	private ObjectFieldHandler(String name) {
		super(name);
	}

	public void set(Object object, Object child) throws JOOS_ParsingException {
		try {
			setter.invoke(object, child);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new JOOS_ParsingException(e.getMessage());
		}
	}

	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws JOOS_ComposingException
	 */
	public Object get(Object object) throws JOOS_ComposingException {
		try {
			return getter.invoke(object, new Object[]{});
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
	}

	

	@Override
	public boolean compose(Object object, ComposingScope scope) throws JOOS_ComposingException, IOException {

		Object value = get(object);
		
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
				ObjectFieldHandler.this.set(parent, child);
			}
		};
	}
}
