package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.Ws3dContext;

public class PrimitivesArrayFieldHandler extends FieldHandler {

	private Class<?> componentType;
	
	public PrimitivesArrayFieldHandler(String name, Field field) {
		super(name, field);
		componentType = field.getType().getComponentType();
	}

	/**
	 * 
	 * @param object
	 * @param values the array of values
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object values) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, values);
	}
	

	@Override
	public Class<?> getSubType() {
		return componentType;
	}
	
	@Override
	public ScopeType getSort() {
		return ScopeType.PRIMITIVES_ARRAY;
	}

	@Override
	public void subDiscover(Ws3dContext context) {
		// nothing to sub-discover
	}
}
