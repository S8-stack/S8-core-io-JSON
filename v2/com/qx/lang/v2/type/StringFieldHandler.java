package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.ParsingException;

public class StringFieldHandler extends PrimitiveFieldHandler {

	public StringFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void set(Object object, String value) throws ParsingException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot deserialize String due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return (String) field.get(object);
	}

}

