package com.s8.blocks.joos.parsing;

import java.util.ArrayList;
import java.util.List;

public abstract class PrimitivesArrayScope<T> extends ListedScope {


	protected List<T> values;
	
	
	protected Object object;


	//protected OnParsedObject callback;

	protected Class<?> componentType;



	public PrimitivesArrayScope(Class<?> componentType, Object object) {
		super();
		this.componentType = componentType;
		this.object = object;
		this.values = new ArrayList<T>();
	}



	@Override
	public boolean isClosedBy(char c) {
		return c==']';
	}
	

	@Override
	public boolean isDefinable() {
		return false;
	}

	@Override
	public void define(String definition) {
		// no definition allowed
	}
	
	public abstract void setValue(Object array) throws JOOS_ParsingException;
	
}
