package com.s8.io.joos.fields.maps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.JOOS_Type;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.FieldHandler;
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
public class ObjectsMapFieldHandler extends FieldHandler {
	
	
	public static class Builder extends FieldHandler.Builder {



		/**
		 * 
		 */
		public Class<?> componentType;
		
		public final ObjectsMapFieldHandler handler;
		
		public Builder(String name, Field field, Class<?> componentType) throws JOOS_CompilingException {
			handler = new ObjectsMapFieldHandler(name, field);
			this.componentType = componentType;
		}
		

		@Override
		public Class<?> getSubType() {
			return componentType;
		}

		@Override
		public void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException {
			if(componentType!=null && componentType.getAnnotation(JOOS_Type.class)!=null) {
				context.discover(componentType);	
			}
		}


		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
			handler.defaultTypeHandler = lexiconBuilder.getByClassName(componentType);
		}
		
		@Override
		public FieldHandler getHandler() {
			return handler;
		}

	}
	
	
	
	public TypeHandler defaultTypeHandler;


	public ObjectsMapFieldHandler(String name, Field field) throws JOOS_CompilingException {
		super(name, field);
	}
	

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}




	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean compose(Object object, ComposingScope scope) throws JOOS_ComposingException, IOException {


		// retrieve array
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new JOOS_ComposingException(e.getMessage());
		}


		if(map!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(" : ");

			ComposingScope enclosedScope = scope.enterSubscope('{', '}', true);

			enclosedScope.open();
			Object value;
			for(Entry<String, Object> entry : map.entrySet()) {

				enclosedScope.newItem();
				
				scope.append('"');
				scope.append(entry.getKey());
				scope.append('"');
				scope.append(" : ");
				
				value = entry.getValue();
				TypeHandler typeHandler = enclosedScope.getTypeHandler(value);
				typeHandler.compose(value, enclosedScope, typeHandler == this.defaultTypeHandler);				
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

			private HashMap<String, Object> entries = new HashMap<String, Object>();
			
			@Override
			public ParsingScope openEntry(String key) throws JOOS_ParsingException {
				return new ObjectScope(defaultTypeHandler) {
					@Override
					public void onParsed(Object object) throws JOOS_ParsingException {
						entries.put(key, object);	
					}
				};
			}

			@Override
			public void onExhausted() throws JOOS_ParsingException {
				try {
					ObjectsMapFieldHandler.this.set(object, entries);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}
}
