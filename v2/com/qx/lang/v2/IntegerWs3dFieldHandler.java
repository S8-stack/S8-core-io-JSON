package com.qx.lang.v2;

public class IntegerWs3dFieldHandler extends PrimitiveWs3dFieldHandler {
	
	
	@Override
	public void set(Object object, String value) throws DeserializationException{
		try {
			field.setInt(object, Integer.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new DeserializationException("Cannot set interger due to "+e.getMessage());
		}
	}
	
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Integer.toString(field.getInt(object));
	}

}
