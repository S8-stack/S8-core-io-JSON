package com.qx.lang.v2;


import java.util.List;


/**
 * 
 * @author pc
 *
 */
public class ObjectsListWs3dFieldHandler extends Ws3dFieldHandler {


	private Class<?> componentType;


	public ObjectsListWs3dFieldHandler(Class<?> componentType) {
		super();
		this.componentType = componentType;
	}

	public void set(Object object, List<Object> values) throws Ws3dParsingException {
		try {
			field.set(object, values);
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
		return Sort.OBJECTS_LIST;
	}

}