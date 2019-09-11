package com.qx.lang.joos.parsing;

import com.qx.lang.joos.JOOS_ParsingException;
import com.qx.lang.joos.type.FieldHandler.ScopeType;

public abstract class ParsingScope {
	
	public abstract ParsingScope enter(String declarator) throws JOOS_ParsingException;
	
	public abstract ScopeType getType();
	
	public abstract boolean isClosedBy(char c);
	
}
