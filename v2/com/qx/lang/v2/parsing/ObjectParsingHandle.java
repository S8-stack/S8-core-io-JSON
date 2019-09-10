package com.qx.lang.v2.parsing;

import java.util.List;

import com.qx.lang.v2.ParsingException;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.ObjectFieldHandler;
import com.qx.lang.v2.type.ObjectsArrayFieldHandler;
import com.qx.lang.v2.type.PrimitiveFieldHandler;
import com.qx.lang.v2.type.PrimitivesListFieldHandler;
import com.qx.lang.v2.type.TypeHandler;
import com.qx.lang.v2.type.FieldHandler;

public class ObjectParsingHandle extends ParsingHandle {


	public TypeHandler handler;

	public Object object;



	public ObjectParsingHandle(TypeHandler handler) {
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
		
		
		FieldHandler fieldHandler = handler.getFieldHandler(name);
		if(fieldHandler==null){
			throw new Ws3dParsingException("Unknown field: "+name);
		}
		
		switch(fieldHandler.getSort()){

		case PRIMITIVE: return new PrimitiveSetter() {
			@Override
			public void set(String value) throws Ws3dParsingException {
				try {
					((PrimitiveFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | ParsingException e) {
					throw new Ws3dParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};

		case OBJECT: return new ObjectSetter() {
			@Override
			public void set(Object value) throws Ws3dParsingException {
				try {
					((ObjectFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Ws3dParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};

		case PRIMITIVES_ARRAY: return new PrimitivesListSetter() {
			@Override
			public void set(List<String> values) throws Ws3dParsingException {
				try {
					((PrimitivesListFieldHandler) fieldHandler).set(object, values);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Ws3dParsingException(e.getMessage());
				}
			}
		};

		case OBJECTS_ARRAY: return new ObjectsArraySetter() {
			@Override
			public void set(Object array) throws Ws3dParsingException {
				((ObjectsArrayFieldHandler) fieldHandler).set(object, array);
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
