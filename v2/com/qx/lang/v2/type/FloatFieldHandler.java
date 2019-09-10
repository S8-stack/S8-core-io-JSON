package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.ParsingException;

public class FloatFieldHandler extends PrimitiveFieldHandler {
	
	public FloatFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void set(Object object, String value) throws ParsingException {
		try {
			field.setFloat(object, Float.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot deserialize double due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Float.toString(field.getFloat(object));
	}

}