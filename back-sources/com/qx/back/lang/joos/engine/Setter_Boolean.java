package com.qx.back.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;


public class Setter_Boolean extends Setter_Primitive {
	@Override
	public void set(Object object, String value, int line) throws DeserializationException {
		try {
			method.invoke(object, Boolean.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot deserialize boolean due to: "+e.getMessage());
		}
	}

	
}