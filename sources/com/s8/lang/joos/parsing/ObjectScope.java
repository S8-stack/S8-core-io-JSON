package com.s8.lang.joos.parsing;

import java.lang.reflect.InvocationTargetException;

import com.s8.lang.joos.JOOS_Context;
import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.type.FieldHandler;
import com.s8.lang.joos.type.TypeHandler;

public class ObjectScope extends MappedScope {



	private OnParsedObject callback;

	public TypeHandler handler;

	public Object object;



	public ObjectScope(OnParsedObject enclosing) {
		super();
		this.callback = enclosing;
	}



	public Object getValue() {
		return object;
	}

	@Override
	public void define(String definition, JOOS_Context context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		handler = context.get(definition);

		// create object of this scope
		object = handler.createInstance();

	}



	@Override
	public ParsingScope openEntry(String name) throws JOOS_ParsingException {
		FieldHandler fieldHandler = handler.getFieldHandler(name);
		if(fieldHandler==null){
			throw new JOOS_ParsingException("Unknown field: "+name);
		}
		return fieldHandler.openScope(object);
	}



	@Override
	public void close() throws JOOS_ParsingException {
		callback.set(object);
	}



	@Override
	public boolean isDefinable() {
		return true;
	}

}
