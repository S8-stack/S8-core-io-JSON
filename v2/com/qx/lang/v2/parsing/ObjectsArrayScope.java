package com.qx.lang.v2.parsing;

import java.lang.reflect.Array;

import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler.ScopeType;

public class ObjectsArrayScope extends ParsingScope {

	public abstract static class Enclosing {

		public abstract void set(Object value) throws Ws3dParsingException;
	}

	
	private Enclosing enclosing;
	
	private Class<?> componentType;
	
	private Object array;
	

	public ObjectsArrayScope(Enclosing enclosing, Class<?> componentType) throws Ws3dParsingException {
		super();
		this.enclosing = enclosing;
		this.componentType = componentType;
	}
	

	public void define(int length) throws Ws3dParsingException {
		array = Array.newInstance(componentType, length);
		enclosing.set(array);
	}
	
	@Override
	public ParsingScope enter(String declarator) throws Ws3dParsingException {
		
		int index = Integer.valueOf(declarator);
		
		return new ObjectScope(new ObjectScope.Enclosing() {
			
			@Override
			public void set(Object value) throws Ws3dParsingException {
				Array.set(array, index, value);
			}
		});
	}


	@Override
	public ScopeType getType() {
		return ScopeType.OBJECTS_ARRAY;
	}

	@Override
	public boolean isClosedBy(char c) {
		return c==']';
	}

}
