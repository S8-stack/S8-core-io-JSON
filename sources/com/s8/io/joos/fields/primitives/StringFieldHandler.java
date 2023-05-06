package com.s8.io.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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

		public Builder(String name) {
			super();
			handler = new StringFieldHandler(name);
		}
	}
	
	public StringFieldHandler(String name) {
		super(name);
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new QuotedScope() {
			
			@Override
			public void setValue(String value) throws JOOS_ParsingException {
				try {
					setter.invoke(object, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
			value = (String) getter.invoke(object, new Object[]{});
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
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

