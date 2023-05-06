package com.s8.io.joos.fields.simples;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.QuotedScope;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class StringFieldHandler extends PrimitiveFieldHandler {


	public static class Builder extends PrimitiveFieldHandler.Builder {

		public Builder(String name, Field field) {
			super();
			handler = new StringFieldHandler(name, field);
		}
	}
	
	public StringFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new QuotedScope() {
			
			@Override
			public void setValue(String value) throws JOOS_ParsingException {
				try {
					field.set(object, value);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JOOS_ParsingException("Cannot deserialize String due to: "+e.getMessage());
				}
			}
		};
	}
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, JOOS_ComposingException  {
		String value;
		try {
			value = (String) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		if(value!=null) {
			
			scope.newItem();
			scope.append(name);
			scope.append(": ");
			
			scope.append('"');
			scope.append(value);
			scope.append('"');
			
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return (String) field.get(object);
	}
	*/
	
	
	
}

