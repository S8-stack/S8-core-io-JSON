package com.qx.lang.v2;


public class DoubleWs3dFieldHandler extends PrimitiveWs3dFieldHandler {
	
	@Override
	public void set(Object object, String value) throws DeserializationException {
		try {
			field.setDouble(object, Double.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new DeserializationException("Cannot deserialize double due to: "+e.getMessage());
		}
	}
	

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Double.toString(field.getDouble(object));
	}

}