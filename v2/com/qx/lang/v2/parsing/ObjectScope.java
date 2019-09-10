package com.qx.lang.v2.parsing;

import com.qx.lang.v2.ParsingException;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.FieldHandler;
import com.qx.lang.v2.type.FieldHandler.ScopeType;
import com.qx.lang.v2.type.ObjectFieldHandler;
import com.qx.lang.v2.type.ObjectsArrayFieldHandler;
import com.qx.lang.v2.type.PrimitiveFieldHandler;
import com.qx.lang.v2.type.PrimitivesArrayFieldHandler;
import com.qx.lang.v2.type.TypeHandler;

public class ObjectScope extends ParsingScope {

	public abstract static class Enclosing {

		public abstract void set(Object value) throws Ws3dParsingException; 

	}


	private Enclosing enclosing;

	public TypeHandler handler;

	public Object object;



	public ObjectScope(Enclosing enclosing) {
		super();
		this.enclosing = enclosing;
	}



	public Object getValue() {
		return object;
	}

	public void define(TypeHandler typeHandler) {
		this.handler = typeHandler;
		try {

			// create object of this scope
			object = typeHandler.createInstance();

			// set object
			enclosing.set(object);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public ParsingScope enter(String name) throws Ws3dParsingException {


		FieldHandler fieldHandler = handler.getFieldHandler(name);
		if(fieldHandler==null){
			throw new Ws3dParsingException("Unknown field: "+name);
		}

		switch(fieldHandler.getScopeType()){

		case PRIMITIVE: return new PrimitiveScope(new PrimitiveScope.Enclosing() {

			@Override
			public void set(String value) throws Ws3dParsingException, ParsingException {
				((PrimitiveFieldHandler) fieldHandler).parse(object, value);
			}
		});

		case OBJECT: return new ObjectScope(new Enclosing() {
			@Override
			public void set(Object value) throws Ws3dParsingException {
				try {
					((ObjectFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Ws3dParsingException("Failed to set object due to "+e.getMessage());
				}
			}	
		});

		case PRIMITIVES_ARRAY: return new PrimitivesArrayScope(new PrimitivesArrayScope.Enclosing() {

			@Override
			public void set(Object value) throws IllegalArgumentException, IllegalAccessException {
				((PrimitivesArrayFieldHandler) fieldHandler).set(object, value);
			}
		}, ((PrimitivesArrayFieldHandler) fieldHandler).getSubType());


		case OBJECTS_ARRAY: return new ObjectsArrayScope(new ObjectsArrayScope.Enclosing() {

			@Override
			public void set(Object value) throws Ws3dParsingException {
				((ObjectsArrayFieldHandler) fieldHandler).set(object, value);
			}

		}, ((ObjectsArrayFieldHandler) fieldHandler).getSubType());


		default : throw new Ws3dParsingException("Impossible to generate setter");
		
		}
	}


	@Override
	public ScopeType getType() {
		return ScopeType.OBJECT;
	}
	
	@Override
	public boolean isClosedBy(char c) {
		return c=='}';
	}
}
