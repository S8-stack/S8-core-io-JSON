package com.qx.lang.v2.type;

import com.qx.lang.v2.ParsingException;

public class BooleanFieldHandler extends PrimitiveFieldHandler {
	
	
	@Override
	public void set(Object object, String value) throws ParsingException {
		try {
			field.setBoolean(object, Boolean.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot deserialize boolean due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Boolean.toString(field.getBoolean(object));
	}	
}