package com.s8.io.joos.fields.structures;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.JOOS_Type;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.ListFieldHandler;
import com.s8.io.joos.parsing.ArrayScope;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ObjectScope;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.types.JOOS_CompilingException;
import com.s8.io.joos.types.TypeHandler;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjectsListFieldHandler extends ListFieldHandler {

	
	public static class Builder extends ListFieldHandler.Builder {
		
		
		public ObjectsListFieldHandler handler;
		
		public Builder(String name, Class<?> componentType) {
			handler = new ObjectsListFieldHandler(name, componentType);
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
			
			if(handler.componentType !=null && handler.componentType.getAnnotation(JOOS_Type.class)!=null) {
				context.discover(handler.componentType);	
			}
		}


		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
			handler.componentTypeHandler = lexiconBuilder.getByClassName(handler.componentType);
		}
		
		@Override
		public ListFieldHandler getHandler() {
			return handler;
		}
	}
	
	
	

	private Class<?> componentType;
	
	public TypeHandler componentTypeHandler;

	public ObjectsListFieldHandler(String name, Class<?> componentType) {
		super(name);
		this.componentType = componentType;
	}

	public void add(Object object, Object item) throws JOOS_ParsingException {
		try {
			adder.invoke(object, item);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new JOOS_ParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}



	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, JOOS_ComposingException {

		// retrieve array
		List<Object> list = new ArrayList<>();
		try {
			iterator.invoke(object, list);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new JOOS_ComposingException(e.getMessage());
		}


		if(list.size() > 0) {

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
					typeHandler.compose(item, enclosedScope);					
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
			
			
			@Override
			public ParsingScope openItemScope() throws JOOS_ParsingException {
				return new ObjectScope(componentTypeHandler) {
					@Override
					public void onParsed(Object item) throws JOOS_ParsingException {
						add(object, item);
					}
				};
			}
			
			@Override
			public void close() throws JOOS_ParsingException {
				// do nothing
			}
		};
	}
}