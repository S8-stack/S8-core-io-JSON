package com.qx.lang.v2.type;


import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.lang.v2.JOOS_Context;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.composing.ComposingScope;


/**
 * 
 * @author pc
 *
 */
public class ObjectsArrayFieldHandler extends FieldHandler {


	private Class<?> componentType;


	public ObjectsArrayFieldHandler(String name, Field field) {
		super(name, field);
		this.componentType = field.getType().getComponentType();
	}

	public void set(Object object, Object valuesArray) throws Ws3dParsingException {
		try {
			field.set(object, valuesArray);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new Ws3dParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}


	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}


	@Override
	public Class<?> getSubType() {
		return componentType;
	}

	@Override
	public ScopeType getScopeType() {
		return ScopeType.OBJECTS_ARRAY;
	}

	@Override
	public void subDiscover(JOOS_Context context) {
		context.discover(componentType);
	}

	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, IllegalArgumentException, IllegalAccessException {

		// retrieve array
		Object array = field.get(object);
		if(array!=null) {

			// field description
			
			scope.append(name);
			scope.append(':');
			
			int length = Array.getLength(array);
			scope.append('(');
			scope.append(Integer.toString(length));
			scope.append(')');

			ComposingScope enclosedScope = scope.enterSubscope('[', ']');
			
			enclosedScope.open();
			Object item;
			for(int index=0; index<length; index++) {
				item = Array.get(array, index);
				if(item!=null) {
					
					enclosedScope.newLine();
					enclosedScope.append(Integer.toString(index));
					enclosedScope.append(':');

					TypeHandler typeHandler = enclosedScope.getTypeHandler(item);
					typeHandler.compose(item, enclosedScope);					
				}			
			}
			enclosedScope.close();
			return true;
		}
		else {
			return false;
		}
	}
}