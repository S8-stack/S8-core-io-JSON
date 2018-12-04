package com.qx.lang.v2;

import java.util.List;

import com.qx.lang.v2.Ws3dFieldHandler.Sort;

public abstract class ObjectsListSetter extends Setter {

	public abstract void set(List<Object> value) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.OBJECTS_LIST;
	}
}
