package com.s8.io.joos.fields.simples;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.AlphaNumericScope;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BooleanFieldHandler extends PrimitiveFieldHandler {
	
	public static class Builder extends PrimitiveFieldHandler.Builder {
		
		public Builder(String name, Field field) {
			super();
			handler = new BooleanFieldHandler(name, field);
		}
	}


	private BooleanFieldHandler(String name, Field field) {
		super(name, field);
	}


	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Boolean.toString(field.getBoolean(object));
	}
	 */

	@Override
	public ParsingScope openScope(Object object) {
		return new AlphaNumericScope() {
			public @Override void setValue(String value) throws JOOS_ParsingException {
				try {
					field.setBoolean(object, Boolean.valueOf(value));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JOOS_ParsingException("Cannot deserialize boolean due to: "+e.getMessage());
				}
			}
		};
	}
	
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, JOOS_ComposingException  {

		scope.newItem();
		scope.append(name);
		scope.append(": ");
		
		try {
			scope.append(Boolean.toString(field.getBoolean(object)));
		} 
		catch (IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}

		return true;
	}

}