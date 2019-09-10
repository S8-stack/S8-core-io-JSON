package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;

public abstract class ParsingHandle {
	
	public abstract Setter getSetter(String name) throws Ws3dParsingException;

	public abstract void close() throws Ws3dParsingException;

	public abstract boolean isList();
	
}
