package com.s8.core.io.json.fields.maps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.MapScope;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.parsing.StringScope;
import com.s8.core.io.json.types.JSON_CompilingException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 */
public class IntegerMapFieldHandler extends MapFieldHandler {
	
	
	public static class Builder extends FieldHandler.Builder {



		/**
		 * 
		 */
		public final IntegerMapFieldHandler handler;
		
		public Builder(String name, Field field) throws JSON_CompilingException {
			handler = new IntegerMapFieldHandler(name, field);
		}
		

		@Override
		public Class<?> getSubType() {
			return null;
		}

		@Override
		public void subDiscover(JSON_Lexicon.Builder context) throws JSON_CompilingException {
			// nothing to sub-discover
		}


		@Override
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
			/* nothing to compile */
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}

	}
	
	
	

	public IntegerMapFieldHandler(String name, Field field) throws JSON_CompilingException {
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

			private HashMap<String, Integer> entries = new HashMap<String, Integer>();
			
			@Override
			public ParsingScope openEntry(String key) throws JSON_ParsingException {
				return new StringScope() {
					@Override
					public void setValue(String value) throws JSON_ParsingException {
						entries.put(key, Integer.decode(value));	
					}
				};
			}

			@Override
			public void onExhausted() throws JSON_ParsingException {
				try {
					IntegerMapFieldHandler.this.set(object, entries);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JSON_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}


	@Override
	public void composeValue(ComposingScope scope, Object item) throws JSON_ComposingException, IOException {
		scope.append(Integer.toString((int) item));
	}
}
