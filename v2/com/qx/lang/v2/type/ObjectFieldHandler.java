package com.qx.lang.v2.type;

import java.lang.reflect.Field;

import com.qx.lang.v2.Ws3dContext;

public class ObjectFieldHandler extends FieldHandler {

	/**
	 * 
	 */
	public Class<?> fieldType;

	public ObjectFieldHandler(String name, Field field) {
		super(name, field);
		this.fieldType = field.getType();
	}

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}

	@Override
	public Class<?> getSubType() {
		return fieldType;
	}

	@Override
	public ScopeType getSort() {
		return ScopeType.OBJECT;
	}

	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}
	
	@Override
	public void subDiscover(Ws3dContext context) {
		context.discover(fieldType);
	}
}
