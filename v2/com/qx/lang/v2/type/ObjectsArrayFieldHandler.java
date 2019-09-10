package com.qx.lang.v2.type;


import com.qx.lang.v2.Ws3dParsingException;


/**
 * 
 * @author pc
 *
 */
public class ObjectsArrayFieldHandler extends FieldHandler {


	private Class<?> componentType;


	public ObjectsArrayFieldHandler(Class<?> componentType) {
		super();
		this.componentType = componentType;
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
	public Sort getSort() {
		return Sort.OBJECTS_ARRAY;
	}
}