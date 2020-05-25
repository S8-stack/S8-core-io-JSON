package com.s8.lang.joos.parsing;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.ParsingException;
import com.s8.lang.joos.type.FieldHandler.ScopeType;

public class PrimitiveScope extends ParsingScope {

	
	public abstract static class Enclosing {
		
		public abstract void set(String value) throws JOOS_ParsingException, ParsingException;
	}
	
	private Enclosing enclosing;
	
	public PrimitiveScope(Enclosing enclosing) {
		super();
		this.enclosing = enclosing;
	}
	
	
	public void define(String value) throws JOOS_ParsingException, ParsingException {
		enclosing.set(value);
	}

	@Override
	public ParsingScope enter(String declarator) throws JOOS_ParsingException {
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
