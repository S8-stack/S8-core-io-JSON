package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.Sort;

public abstract class ObjectsArraySetter extends Setter {

	public abstract void set(Object array) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.OBJECTS_ARRAY;
	}
}
