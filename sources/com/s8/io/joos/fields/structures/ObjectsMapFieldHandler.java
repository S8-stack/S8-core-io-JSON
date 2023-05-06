package com.s8.io.joos.fields.structures;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.JOOS_Type;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.MapFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.MapScope;
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
 * 
 */
public class ObjectsMapFieldHandler extends MapFieldHandler {
	
	
	public static class Builder extends MapFieldHandler.Builder {



		/**
		 * 
		 */
		public Class<?> valueType;
		
		public final ObjectsMapFieldHandler handler;
		
		public Builder(String name, Class<?> valueType) {
			handler = new ObjectsMapFieldHandler(name);
			this.valueType = valueType;
		}
		

		@Override
		public Class<?> getSubType() {
			return valueType;
		}

		@Override
		public void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException {
			if(valueType!=null && valueType.getAnnotation(JOOS_Type.class)!=null) {
				context.discover(valueType);	
			}
		}


		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
			handler.defaultTypeHandler = lexiconBuilder.getByClassName(valueType);
		}
		
		@Override
		public MapFieldHandler getHandler() {
			return handler;
		}

	}
	
	
	
	public TypeHandler defaultTypeHandler;


	public ObjectsMapFieldHandler(String name) {
		super(name);
	}
	

	public void put(Object object, String key, Object value) throws JOOS_ParsingException {
		try {
			putter.invoke(object, key, value);
		} 
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new JOOS_ParsingException(e.getMessage());
		}
	}


	@Override
	public boolean compose(Object object, ComposingScope scope) throws JOOS_ComposingException, IOException {


		// retrieve array
		Map<String, Object> map = new HashMap<>();
		
		BiConsumer<String, Object> consumer = new BiConsumer<String, Object>() {
			@Override
			public void accept(String key, Object value) {
				map.put(key, value);
			}
		};
		
		try {
			traverser.invoke(object, consumer);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new JOOS_ComposingException(e.getMessage());
		}


		if(map.size() > 0) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');

			ComposingScope enclosedScope = scope.enterSubscope('{', '}', true);

			enclosedScope.open();
			Object value;
			for(Entry<String, Object> entry : map.entrySet()) {

				enclosedScope.newItem();
				scope.append(entry.getKey());
				scope.append(": ");
				
				value = entry.getValue();
				TypeHandler typeHandler = enclosedScope.getTypeHandler(value);
				typeHandler.compose(value, enclosedScope);				
			};
			enclosedScope.close();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public ParsingScope openScope(Object object) {
		return new MapScope() {

			@Override
			public ParsingScope openEntry(String key) throws JOOS_ParsingException {
				return new ObjectScope(defaultTypeHandler) {
					@Override
					public void onParsed(Object value) throws JOOS_ParsingException {
						put(object, key, value);	
					}
				};
			}

			@Override
			public void onExhausted() throws JOOS_ParsingException {
				// do nothing
			}
		};
	}


}
