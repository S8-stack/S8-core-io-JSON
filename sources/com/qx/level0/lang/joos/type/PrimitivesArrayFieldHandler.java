package com.qx.level0.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.level0.lang.joos.JOOS_Context;
import com.qx.level0.lang.joos.composing.ComposingScope;

public abstract class PrimitivesArrayFieldHandler extends FieldHandler {

	private Class<?> componentType;
	
	public PrimitivesArrayFieldHandler(String name, Field field) {
		super(name, field);
		componentType = field.getType().getComponentType();
	}

	/**
	 * 
	 * @param object
	 * @param values the array of values
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object values) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, values);
	}
	

	@Override
	public Class<?> getSubType() {
		return componentType;
	}
	
	@Override
	public ScopeType getScopeType() {
		return ScopeType.PRIMITIVES_ARRAY;
	}

	@Override
	public void subDiscover(JOOS_Context context) {
		// nothing to sub-discover
	}
	
	/**
	 * 
	 * @param object the parent object
	 * @param writer
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public boolean compose(Object object, ComposingScope scope) throws IOException, IllegalArgumentException, IllegalAccessException {

		// retrieve array
		Object array = field.get(object);
		if(array!=null) {
			
			scope.newLine();
	
			// field description
			
			scope.append(name);
			scope.append(':');
			
			int length = Array.getLength(array);
			scope.append('(');
			scope.append(Integer.toString(length));
			scope.append(')');
			

			ComposingScope enclosedScope = scope.enterSubscope('[', ']');
			
			
			// write field
			enclosedScope.open();
			for(int index=0; index<length; index++) {

				if(isItemValid(array, index)) {
			
					enclosedScope.newLine();
					enclosedScope.append(Integer.toString(index));
					enclosedScope.append(':');
					composeItem(array, index, enclosedScope);					
				}			
			}
			enclosedScope.close();
			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * @param array
	 * @param index 
	 * @return flag indicating whether has produced something
	 */
	public abstract boolean isItemValid(Object array, int index);
	
	/**
	 * 
	 * @param array
	 * @param index
	 * @param writer
	 * @throws IOException 
	 */
	public abstract void composeItem(Object array, int index, ComposingScope writer) throws IOException;
}
