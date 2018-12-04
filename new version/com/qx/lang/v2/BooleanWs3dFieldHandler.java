package com.qx.lang.v2;

public class BooleanWs3dFieldHandler extends PrimitiveWs3dFieldHandler {
	
	
	@Override
	public void set(Object object, String value) throws DeserializationException {
		try {
			field.setBoolean(object, Boolean.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new DeserializationException("Cannot deserialize boolean due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Boolean.toString(field.getBoolean(object));
	}	
}