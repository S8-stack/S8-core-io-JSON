package com.qx.lang.v2.parsing;

import java.util.List;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler;
import com.qx.lang.v2.type.FieldHandler.Sort;

public abstract class PrimitivesListSetter extends Setter {

	public abstract void set(List<String> values) throws Ws3dParsingException;
	
	@Override
	public Sort getSort(){
		return Sort.PRIMITIVES_ARRAY;
	}
}
