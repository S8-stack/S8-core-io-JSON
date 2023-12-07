package com.s8.core.io.json.fields.simples;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.PrimitiveFieldHandler;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.parsing.StringScope;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class IntegerFieldHandler extends PrimitiveFieldHandler {

	public static class Builder extends PrimitiveFieldHandler.Builder {

		public Builder(String name, Field field) {
			super();
			handler = new IntegerFieldHandler(name, field);
		}
	}

	public IntegerFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new StringScope() {
			
			@Override
			public void setValue(String value) throws JSON_ParsingException {
				try {
					field.setInt(object, Integer.valueOf(value));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JSON_ParsingException("Cannot set interger due to "+e.getMessage());
				}
			}
		};
	}

	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, JSON_ComposingException  {

		scope.newItem();
		scope.append(name);
		scope.append(": ");
		
		try {
			scope.append(Integer.toString(field.getInt(object)));
		} 
		catch (IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new JSON_ComposingException(e.getMessage());
		}
		return true;
	}


	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Integer.toString(field.getInt(object));
	}
	*/


}
