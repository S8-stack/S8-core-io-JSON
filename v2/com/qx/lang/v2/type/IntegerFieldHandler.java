package com.qx.lang.v2.type;

import com.qx.lang.v2.ParsingException;

public class IntegerFieldHandler extends PrimitiveFieldHandler {
	
	
	@Override
	public void set(Object object, String value) throws ParsingException{
		try {
			field.setInt(object, Integer.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot set interger due to "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Integer.toString(field.getInt(object));
	}

}
