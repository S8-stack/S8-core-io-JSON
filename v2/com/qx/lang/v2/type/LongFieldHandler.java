package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.ParsingException;

public class LongFieldHandler extends PrimitiveFieldHandler {
	
	
	public LongFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void set(Object object, String value) throws ParsingException{
		try {
			field.setLong(object, Long.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot set interger due to "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Long.toString(field.getLong(object));
	}

}
