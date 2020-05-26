package com.s8.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.composing.ComposingScope;

public class StringFieldHandler extends PrimitiveFieldHandler {

	public StringFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void parse(Object object, String value) throws JOOS_ParsingException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot deserialize String due to: "+e.getMessage());
		}
	}
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException  {
		String value = (String) field.get(object);
		if(value!=null) {
			
			scope.newLine();
			scope.append(name);
			scope.append(':');
			
			scope.append('(');
			scope.append(Integer.toString(value.length()));
			scope.append(')');
			scope.append(value);
			
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return (String) field.get(object);
	}
	*/
}

