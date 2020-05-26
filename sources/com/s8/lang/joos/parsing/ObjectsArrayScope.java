package com.s8.lang.joos.parsing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.s8.lang.joos.JOOS_ParsingException;

public class ObjectsArrayScope extends ListedScope {


	private OnParsedObject callback;
	
	private Class<?> componentType;
	
	private List<Object> values;
	

	public ObjectsArrayScope(Class<?> componentType, OnParsedObject callback) {
		super();
		this.callback = callback;
		this.componentType = componentType;
		this.values = new ArrayList<Object>();
	}
	
	
	public Class<?> getComponentType(){
		return componentType;
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new ObjectScope(new ObjectScope.OnParsedObject() {
			public @Override void set(Object value) {
				values.add(value);
			}
		});
	}


	@Override
	public boolean isClosedBy(char c) {
		return c==']';
	}


	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		Object array = Array.newInstance(componentType, length);
		for(int index=0; index<length; index++) {
			Array.set(array, index, values.get(index));
		}
		callback.set(array);
	}


	@Override
	public boolean isDefinable() {
		return false;
	}


	@Override
	public void define(String definition) {
		// node definition
	}

}
