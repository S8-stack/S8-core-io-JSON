package com.qx.lang.v2.type;

public class ObjectFieldHandler extends FieldHandler {

	/**
	 * 
	 */
	public Class<?> fieldType;

	public ObjectFieldHandler(Class<?> fieldType) {
		super();
		this.fieldType = fieldType;
	}

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}

	@Override
	public Class<?> getSubType() {
		return fieldType;
	}

	@Override
	public Sort getSort() {
		return Sort.OBJECT;
	}

	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}
}
