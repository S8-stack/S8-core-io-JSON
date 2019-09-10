package com.qx.lang.v2.type;

import com.qx.lang.v2.ParsingException;

/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveFieldHandler extends FieldHandler {

	
	/**
	 * 
	 * @param object
	 * @param value
	 * @throws ParsingException 
	 * @throws Exception
	 */
	public abstract void set(Object object, String value) throws ParsingException;
	

	public abstract String get(Object object) throws IllegalArgumentException, IllegalAccessException;

	@Override
	public Class<?> getSubType() {
		return null;
	}
	
	@Override
	public ScopeType getSort() {
		return ScopeType.PRIMITIVE;
	}
}
