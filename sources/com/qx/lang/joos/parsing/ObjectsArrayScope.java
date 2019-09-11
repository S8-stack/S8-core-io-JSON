package com.qx.lang.joos.parsing;

import java.lang.reflect.Array;

import com.qx.lang.joos.JOOS_ParsingException;
import com.qx.lang.joos.type.FieldHandler.ScopeType;

public class ObjectsArrayScope extends ParsingScope {

	public abstract static class Enclosing {

		public abstract void set(Object value) throws JOOS_ParsingException;
	}

	
	private Enclosing enclosing;
	
	private Class<?> componentType;
	
	private Object array;
	

	public ObjectsArrayScope(Enclosing enclosing, Class<?> componentType) throws JOOS_ParsingException {
		super();
		this.enclosing = enclosing;
		this.componentType = componentType;
	}
	

	public void define(int length) throws JOOS_ParsingException {
		array = Array.newInstance(componentType, length);
		enclosing.set(array);
	}
	
	@Override
	public ParsingScope enter(String declarator) throws JOOS_ParsingException {
		
		int index = Integer.valueOf(declarator);
		
		return new ObjectScope(new ObjectScope.Enclosing() {
			
			@Override
			public void set(Object value) throws JOOS_ParsingException {
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
