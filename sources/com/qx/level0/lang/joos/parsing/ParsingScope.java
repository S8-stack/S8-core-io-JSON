package com.qx.level0.lang.joos.parsing;

import com.qx.level0.lang.joos.JOOS_ParsingException;
import com.qx.level0.lang.joos.type.FieldHandler.ScopeType;

public abstract class ParsingScope {
	
	public abstract ParsingScope enter(String declarator) throws JOOS_ParsingException;
	
	public abstract ScopeType getType();
	
	public abstract boolean isClosedBy(char c);
	
}
