package com.qx.lang.v2.type;

import com.qx.lang.v2.ParsingException;

public class DoubleFieldHandler extends PrimitiveFieldHandler {
	
	@Override
	public void set(Object object, String value) throws ParsingException {
		try {
			field.setDouble(object, Double.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot deserialize double due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Double.toString(field.getDouble(object));
	}

}