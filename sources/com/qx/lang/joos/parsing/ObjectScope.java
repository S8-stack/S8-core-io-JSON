package com.qx.lang.joos.parsing;

import com.qx.lang.joos.JOOS_ParsingException;
import com.qx.lang.joos.ParsingException;
import com.qx.lang.joos.type.FieldHandler;
import com.qx.lang.joos.type.ObjectFieldHandler;
import com.qx.lang.joos.type.ObjectsArrayFieldHandler;
import com.qx.lang.joos.type.PrimitiveFieldHandler;
import com.qx.lang.joos.type.PrimitivesArrayFieldHandler;
import com.qx.lang.joos.type.TypeHandler;
import com.qx.lang.joos.type.FieldHandler.ScopeType;

public class ObjectScope extends ParsingScope {

	public abstract static class Enclosing {

		public abstract void set(Object value) throws JOOS_ParsingException; 

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
	public ParsingScope enter(String name) throws JOOS_ParsingException {


		FieldHandler fieldHandler = handler.getFieldHandler(name);
		if(fieldHandler==null){
			throw new JOOS_ParsingException("Unknown field: "+name);
		}

		switch(fieldHandler.getScopeType()){

		case PRIMITIVE: return new PrimitiveScope(new PrimitiveScope.Enclosing() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				((PrimitiveFieldHandler) fieldHandler).parse(object, value);
			}
		});

		case OBJECT: return new ObjectScope(new Enclosing() {
			@Override
			public void set(Object value) throws JOOS_ParsingException {
				try {
					((ObjectFieldHandler) fieldHandler).set(object, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
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
			public void set(Object value) throws JOOS_ParsingException {
				((ObjectsArrayFieldHandler) fieldHandler).set(object, value);
			}

		}, ((ObjectsArrayFieldHandler) fieldHandler).getSubType());


		default : throw new JOOS_ParsingException("Impossible to generate setter");
		
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
