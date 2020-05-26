package com.s8.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.composing.ComposingScope;

public class DoubleFieldHandler extends PrimitiveFieldHandler {
	
	public DoubleFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void parse(Object object, String value) throws JOOS_ParsingException {
		try {
			field.setDouble(object, Double.valueOf(value));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot deserialize double due to: "+e.getMessage());
		}
	}
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException  {
		
		scope.newLine();
		scope.append(name);
		scope.append(':');
		
		scope.append(Double.toString(field.getDouble(object)));
		
		return true;
	}
	

	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Double.toString(field.getDouble(object));
	}
	*/

}