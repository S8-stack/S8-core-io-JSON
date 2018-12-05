package com.qx.lang.v2;

import java.util.List;

public class ObjectParsingHandle extends ParsingHandle {


	public Ws3dTypeHandler handler;

	public Object object;



	public ObjectParsingHandle(Ws3dTypeHandler handler) {
		super();
		this.handler = handler;
		try {
			this.object = handler.createInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public Setter getSetter(String name) throws Ws3dParsingException {
		
		
		Ws3dFieldHandler fieldHandler = handler.getFieldHandler(name);
		if(fieldHandler==null){
			throw new Ws3dParsingException("Unknown field: "+name);
		}
		
		switch(fieldHandler.getSort()){

		case PRIMITIVE: return new PrimitiveSetter() {
			@Override
			public void set(String value) throws Ws3dParsingException {
				try {
					((PrimitiveWs3dFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | DeserializationException e) {
					throw new Ws3dParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};

		case OBJECT: return new ObjectSetter() {
			@Override
			public void set(Object value) throws Ws3dParsingException {
				try {
					((ObjectWs3dFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Ws3dParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};

		case PRIMITIVES_LIST: return new PrimitivesListSetter() {
			@Override
			public void set(List<String> values) throws Ws3dParsingException {
				try {
					((PrimitivesListWs3dFieldHandler) fieldHandler).set(object, values);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Ws3dParsingException(e.getMessage());
				}
			}
		};

		case OBJECTS_LIST: return new ObjectsListSetter() {
			@Override
			public void set(List<Object> values) throws Ws3dParsingException {
				((ObjectsListWs3dFieldHandler) fieldHandler).set(object, values);
			}
		};

		default : throw new Ws3dParsingException("Impossible to generate setter");
		}
	}



	@Override
	public void close() {
		// nothing to do
	}

	@Override
	public boolean isList() {
		return false;
	}
}
