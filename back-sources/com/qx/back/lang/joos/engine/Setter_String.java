package com.qx.back.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;


public class Setter_String extends Setter_Primitive {
	@Override
	public void set(Object object, String value, int line) throws DeserializationException {
		try {
			method.invoke(object, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot deserialize String due to: "+e.getMessage());
		}
	}
}

