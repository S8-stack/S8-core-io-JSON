package com.s8.io.joos.fields.lists;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.FieldHandler;
import com.s8.io.joos.parsing.ArrayScope;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.QuotedScope;
import com.s8.io.joos.types.JOOS_CompilingException;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class StringListFieldHandler extends FieldHandler {

	public static class Builder extends FieldHandler.Builder {


		public StringListFieldHandler handler;

		public Builder(String name, Field field) {
			handler = new StringListFieldHandler(name, field);
		}


		@Override
		public Class<?> getSubType() {
			return String.class;
		}


		@Override
		public void subDiscover(JOOS_Lexicon.Builder context) throws JOOS_CompilingException {
		}


		@Override
		public void compile(JOOS_Lexicon.Builder lexiconBuilder) {
		}

		@Override
		public FieldHandler getHandler() {
			return handler;
		}
	}



	public StringListFieldHandler(String name, Field field) {
		super(name, field);
	}

	public void set(Object object, Object value) throws JOOS_ParsingException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}


	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, JOOS_ComposingException {

		// retrieve array
		List<String> list = null;
		try {
			list = (List<String>) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new JOOS_ComposingException(e.getMessage());
		}


		if(list!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(" : ");
			
			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);

			enclosedScope.open();
			for(String item : list) {
				if(item!=null) {

					enclosedScope.newItem();
					
					enclosedScope.append(item);			
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

			private List<String> values = new ArrayList<>();

			@Override
			public ParsingScope openItemScope() throws JOOS_ParsingException {
				return new QuotedScope() {
					@Override
					public void setValue(String value) throws JOOS_ParsingException {
						values.add(value);
					}
				};
			}

			@Override
			public void close() throws JOOS_ParsingException {
				set(object, values);
			}
		};
	}

}
