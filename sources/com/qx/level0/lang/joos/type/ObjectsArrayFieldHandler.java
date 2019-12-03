package com.qx.level0.lang.joos.type;


import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.level0.lang.joos.JOOS_Context;
import com.qx.level0.lang.joos.JOOS_ParsingException;
import com.qx.level0.lang.joos.JOOS_Type;
import com.qx.level0.lang.joos.composing.ComposingScope;


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

	public void set(Object object, Object valuesArray) throws JOOS_ParsingException {
		try {
			field.set(object, valuesArray);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot set Object array due to "+e.getMessage());
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
		
		/*
		 * discover component type if annotated (compatibility with generic)
		 */
		if(componentType.getAnnotation(JOOS_Type.class)!=null) {
			context.discover(componentType);	
		}
	}

	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, IllegalArgumentException, IllegalAccessException {

		// retrieve array
		Object array = field.get(object);
		if(array!=null) {

			// field description
			scope.newLine();
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