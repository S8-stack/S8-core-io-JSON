package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.ParsingException;
import com.qx.lang.v2.Ws3dContext;

/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveFieldHandler extends FieldHandler {

	
	
	
	public PrimitiveFieldHandler(String name, Field field) {
		super(name, field);
	}


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
	
	@Override
	public void subDiscover(Ws3dContext context) {
		// nothing to sub-discover
	}
}
