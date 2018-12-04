package com.qx.lang.v2;

import java.util.List;

import com.qx.lang.v2.Ws3dFieldHandler.Sort;

public abstract class PrimitivesListSetter extends Setter {

	public abstract void set(List<String> values) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.PRIMITIVES_LIST;
	}
}
