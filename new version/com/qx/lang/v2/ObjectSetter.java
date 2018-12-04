package com.qx.lang.v2;

import com.qx.lang.v2.Ws3dFieldHandler.Sort;

public abstract class ObjectSetter extends Setter {

	public abstract void set(Object value) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.OBJECT;
	}
}
