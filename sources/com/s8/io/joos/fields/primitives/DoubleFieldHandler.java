package com.s8.io.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.PrimitiveScope;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DoubleFieldHandler extends PrimitiveFieldHandler {
	
	public DoubleFieldHandler(String name, Field field) {
		super(name, field);
	}

	
	@Override
	public ParsingScope openScope(Object object) {
		return new PrimitiveScope() {
			
			@Override
			public void setValue(String value) throws JOOS_ParsingException {
				try {
					field.setDouble(object, Double.valueOf(value));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JOOS_ParsingException("Cannot deserialize double due to: "+e.getMessage());
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
			scope.append(Double.toString(field.getDouble(object)));
		} 
		catch (IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		
		return true;
	}
	

	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Double.toString(field.getDouble(object));
	}
	*/

}