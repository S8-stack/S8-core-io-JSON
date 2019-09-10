package com.qx.lang.v2.type;


import java.lang.reflect.Field;

import com.qx.lang.v2.Ws3dContext;
import com.qx.lang.v2.Ws3dParsingException;


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

	@Override
	public Class<?> getSubType() {
		return componentType;
	}

	@Override
	public ScopeType getSort() {
		return ScopeType.OBJECTS_ARRAY;
	}
	
	@Override
	public void subDiscover(Ws3dContext context) {
		context.discover(componentType);
	}
}