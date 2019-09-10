package com.qx.lang.v2.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.qx.lang.v2.JOOS_Context;
import com.qx.lang.v2.composing.ComposingScope;

public class ObjectFieldHandler extends FieldHandler {

	/**
	 * 
	 */
	public Class<?> fieldType;

	public ObjectFieldHandler(String name, Field field) {
		super(name, field);
		this.fieldType = field.getType();
	}

	public void set(Object object, Object child) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, child);
	}

	@Override
	public Class<?> getSubType() {
		return fieldType;
	}

	@Override
	public ScopeType getScopeType() {
		return ScopeType.OBJECT;
	}

	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}

	@Override
	public void subDiscover(JOOS_Context context) {
		context.discover(fieldType);
	}

	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, IllegalArgumentException, IllegalAccessException {

		Object value = field.get(object);
		if(value!=null) {
			// declare type
			scope.append(name);
			scope.append(':');

			// declare type
			TypeHandler typeHandler = scope.getTypeHandler(value);
			typeHandler.compose(value, scope);
			return true;
		}
		else {
			return false;
		}


	}
}
