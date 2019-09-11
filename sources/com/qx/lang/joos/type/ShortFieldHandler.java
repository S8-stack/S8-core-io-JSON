package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.qx.lang.joos.ParsingException;
import com.qx.lang.joos.composing.ComposingScope;

public class ShortFieldHandler extends PrimitiveFieldHandler {
	
	
	public ShortFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void parse(Object object, String value) throws ParsingException{
		try {
			field.setShort(object, Short.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new ParsingException("Cannot set interger due to "+e.getMessage());
		}
	}
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException  {
		
		scope.newLine();
		scope.append(name);
		scope.append(':');
		
		scope.append(Short.toString(field.getShort(object)));
		
		return true;
	}

	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Short.toString(field.getShort(object));
	}
	
}
