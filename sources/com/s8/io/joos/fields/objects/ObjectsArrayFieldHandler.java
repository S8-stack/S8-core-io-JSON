package com.s8.io.joos.fields.objects;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class ObjectsArrayFieldHandler extends ListFieldHandler {
	
	public static class Builder extends ListFieldHandler.Builder {
		
		public ObjectsArrayFieldHandler handler;
		
		public Builder(String name, Class<?> componentType) {
			super();
			this.handler = new ObjectsArrayFieldHandler(name, componentType);
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
		public ListFieldHandler getHandler() {
			return handler;
		}
	}


	public Class<?> componentType;
	
	public TypeHandler componentTypeHandler;


	public ObjectsArrayFieldHandler(String name, Class<?> componentType) {
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
	public boolean compose(Object object, ComposingScope scope) throws IOException, JOOS_ComposingException {

		// retrieve array
		List<Object> list = new ArrayList<>();
		Consumer<Object> consumer = new Consumer<Object>() {
			@Override
			public void accept(Object t) {
				list.add(t);
			}
		};
 		try {
			iterator.invoke(object, consumer);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		
		if(list.size() > 0) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');
			
			int length = list.size();

			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);
			
			enclosedScope.open();
			Object item;
			for(int index=0; index<length; index++) {
				item = list.get(index);
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
				// nothing to do here
			}
		};
	}
}