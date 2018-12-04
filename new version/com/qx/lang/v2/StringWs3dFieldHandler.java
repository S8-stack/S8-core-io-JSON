package com.qx.lang.v2;

public class StringWs3dFieldHandler extends PrimitiveWs3dFieldHandler {

	@Override
	public void set(Object object, String value) throws DeserializationException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new DeserializationException("Cannot deserialize String due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return (String) field.get(object);
	}

}

