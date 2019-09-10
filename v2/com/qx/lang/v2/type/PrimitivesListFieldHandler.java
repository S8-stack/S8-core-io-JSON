package com.qx.lang.v2.type;

import java.util.List;

public abstract class PrimitivesListFieldHandler extends FieldHandler {

	public abstract void set(Object object, List<String> values) throws IllegalArgumentException, IllegalAccessException;


}
