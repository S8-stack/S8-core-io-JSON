package com.s8.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.lang.joos.ParsingException;
import com.s8.lang.joos.composing.ComposingScope;

public class BooleanFieldHandler extends PrimitiveFieldHandler {


	public BooleanFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void parse(Object object, String value) throws ParsingException {
		try {
			field.setBoolean(object, Boolean.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot deserialize boolean due to: "+e.getMessage());
		}
	}

	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Boolean.toString(field.getBoolean(object));
	}
	 */

	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException  {

		scope.newLine();
		scope.append(name);
		scope.append(':');

		scope.append(Boolean.toString(field.getBoolean(object)));

		return true;
	}

}