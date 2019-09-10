package com.qx.lang.v2.type;

public abstract class PrimitivesListFieldHandler extends FieldHandler {

	/**
	 * 
	 * @param object
	 * @param values the array of values
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public abstract void set(Object object, Object values) throws IllegalArgumentException, IllegalAccessException;


}
