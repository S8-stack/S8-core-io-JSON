package com.qx.lang.v2.parsing;

import com.qx.lang.v2.Ws3dParsingException;

public class RootParsingHandle extends ParsingHandle {

	public Object result;

	public RootParsingHandle() {
		super();
	}

	@Override
	public Setter getSetter(String name) throws Ws3dParsingException {
		
		if(name.equals("root")){
			return new ObjectSetter() {
				@Override
				public void set(Object value) throws Ws3dParsingException {
					result = value;
				}
			};		
		}
		else{
			throw new Ws3dParsingException("First declaration must be root");
		}
	}



	@Override
	public void close() {
		// nothing to do
	}

	@Override
	public boolean isList() {
		return false;
	}
}
