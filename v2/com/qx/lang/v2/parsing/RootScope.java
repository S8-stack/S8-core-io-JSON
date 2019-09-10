package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.ScopeType;

public class RootScope extends ParsingScope {

	public Object result;

	public RootScope() {
		super();
	}

	@Override
	public ParsingScope enter(String name) throws Ws3dParsingException {
		
		if(name.equals("root")){
			return new ObjectScope(new ObjectScope.Enclosing() {

				@Override
				public void set(Object value) throws Ws3dParsingException {
					result = value;
				}
				
			});
		}
		else{
			throw new Ws3dParsingException("First declaration must be root");
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
