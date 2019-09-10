package com.qx.lang.v2.parsing;

import com.qx.lang.v2.ParsingException;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.ScopeType;

public class PrimitiveScope extends ParsingScope {

	
	public abstract static class Enclosing {
		
		public abstract void set(String value) throws Ws3dParsingException, ParsingException;
	}
	
	private Enclosing enclosing;
	
	public PrimitiveScope(Enclosing enclosing) {
		super();
		this.enclosing = enclosing;
	}
	
	
	public void define(String value) throws Ws3dParsingException, ParsingException {
		enclosing.set(value);
	}

	@Override
	public ParsingScope enter(String declarator) throws Ws3dParsingException {
		return null;
	}
	

	@Override
	public ScopeType getType() {
		return ScopeType.PRIMITIVE;
	}


	@Override
	public boolean isClosedBy(char c) {
		return true;
	}
}
