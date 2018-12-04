package com.qx.lang.v2;

/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveWs3dFieldHandler extends Ws3dFieldHandler {

	
	/**
	 * 
	 * @param object
	 * @param value
	 * @throws DeserializationException 
	 * @throws Exception
	 */
	public abstract void set(Object object, String value) throws DeserializationException;
	

	public abstract String get(Object object) throws IllegalArgumentException, IllegalAccessException;

	@Override
	public Class<?> getSubType() {
		return null;
	}
	
	@Override
	public Sort getSort() {
		return Sort.PRIMITIVE;
	}
}
