package com.qx.lang.v2;

import com.qx.lang.v2.Ws3dFieldHandler.Sort;

public abstract class PrimitiveSetter extends Setter {

	public abstract void set(String value) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.PRIMITIVE;
	}
}
