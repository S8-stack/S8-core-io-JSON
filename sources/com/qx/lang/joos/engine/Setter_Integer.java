package com.qx.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;

public class Setter_Integer extends Setter_Primitive {
	@Override
	public void set(Object object, String value, int line) throws DeserializationException{
		try {
			method.invoke(object, Integer.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot set interger due to "+e.getMessage());
		}
	}
}
