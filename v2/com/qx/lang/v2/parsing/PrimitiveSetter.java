package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler;
import com.qx.lang.v2.type.FieldHandler.Sort;

public abstract class PrimitiveSetter extends Setter {

	public abstract void set(String value) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.PRIMITIVE;
	}
}
