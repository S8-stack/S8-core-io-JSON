package com.s8.blocks.joos.parsing;

import java.lang.reflect.InvocationTargetException;

import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.fields.FieldHandler;
import com.s8.blocks.joos.types.TypeHandler;

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
	public void define(String definition, JOOS_Lexicon context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

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
