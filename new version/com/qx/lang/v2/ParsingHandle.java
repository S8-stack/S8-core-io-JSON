package com.qx.lang.v2;

public abstract class ParsingHandle {
	
	public abstract Setter getSetter(String name) throws Ws3dParsingException;

	public abstract void close() throws Ws3dParsingException;

	public abstract boolean isList();
	
}
