package com.qx.lang.v2;

import com.qx.lang.v2.type.PrimitiveFieldHandler;

public class StringWs3dFieldHandler extends PrimitiveFieldHandler {

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

