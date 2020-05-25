package com.s8.lang.joos.parsing;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.type.FieldHandler.ScopeType;

public abstract class ParsingScope {
	
	public abstract ParsingScope enter(String declarator) throws JOOS_ParsingException;
	
	public abstract ScopeType getType();
	
	public abstract boolean isClosedBy(char c);
	
}
