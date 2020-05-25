package com.s8.lang.joos.parsing;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.type.FieldHandler.ScopeType;

public class RootScope extends ParsingScope {

	public Object result;

	public RootScope() {
		super();
	}

	@Override
	public ParsingScope enter(String name) throws JOOS_ParsingException {
		
		if(name.equals("root")){
			return new ObjectScope(new ObjectScope.Enclosing() {

				@Override
				public void set(Object value) throws JOOS_ParsingException {
					result = value;
				}
				
			});
		}
		else{
			throw new JOOS_ParsingException("First declaration must be root");
		}
	}

	@Override
	public ScopeType getType() {
		return ScopeType.OBJECT;
	}

	@Override
	public boolean isClosedBy(char c) {
		return '}'==c;
	}

}
