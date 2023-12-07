package com.s8.core.io.json.fields.maps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.JSON_Type;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.MapScope;
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
 * 
 */
public class ObjectsMapFieldHandler extends MapFieldHandler {
	
	
	public static class Builder extends FieldHandler.Builder {



		/**
		 * 
		 */
		public Class<?> componentType;
		
		public final ObjectsMapFieldHandler handler;
		
		public Builder(String name, Field field, Class<?> componentType) throws JSON_CompilingException {
			handler = new ObjectsMapFieldHandler(name, field);
			this.componentType = componentType;
		}
		

		@Override
		public Class<?> getSubType() {
			return componentType;
		}

		@Override
		public void subDiscover(JSON_Lexicon.Builder context) throws JSON_CompilingException {
			if(componentType!=null && componentType.getAnnotation(JSON_Type.class)!=null) {
				context.discover(componentType);	
			}
		}


		@Override
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
			handler.defaultTypeHandler = lexiconBuilder.getByClassName(componentType);
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}

	}
	
	
	
	public TypeHandler defaultTypeHandler;


	public ObjectsMapFieldHandler(String name, Field field) throws JSON_CompilingException {
		super(name, field);
	}
	

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}




	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}

	

	@Override
	public ParsingScope openScope(Object object) {
		return new MapScope() {

			private HashMap<String, Object> entries = new HashMap<String, Object>();
			
			@Override
			public ParsingScope openEntry(String key) throws JSON_ParsingException {
				return new ObjectScope(defaultTypeHandler) {
					@Override
					public void onParsed(Object object) throws JSON_ParsingException {
						entries.put(key, object);	
					}
				};
			}

			@Override
			public void onExhausted() throws JSON_ParsingException {
				try {
					ObjectsMapFieldHandler.this.set(object, entries);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JSON_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}


	@Override
	public void composeValue(ComposingScope enclosedScope, Object value) throws IOException {
		TypeHandler typeHandler = enclosedScope.getTypeHandler(value);
		typeHandler.compose(value, enclosedScope, typeHandler == this.defaultTypeHandler);				
	}
}
