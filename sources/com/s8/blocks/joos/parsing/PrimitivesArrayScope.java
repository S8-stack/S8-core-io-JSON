package com.s8.blocks.joos.parsing;

import java.util.ArrayList;
import java.util.List;

public abstract class PrimitivesArrayScope<T> extends ListedScope {


	protected List<T> values;


	protected OnParsedObject callback;

	protected Class<?> componentType;



	public PrimitivesArrayScope(OnParsedObject callback, Class<?> componentType) {
		super();
		this.callback = callback;
		this.componentType = componentType;
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
}
