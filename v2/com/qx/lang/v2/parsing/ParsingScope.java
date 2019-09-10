package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.ScopeType;

public abstract class ParsingScope {
	
	public abstract ParsingScope enter(String declarator) throws Ws3dParsingException;
	
	public abstract ScopeType getType();
	
	public abstract boolean isClosedBy(char c);
	
}
