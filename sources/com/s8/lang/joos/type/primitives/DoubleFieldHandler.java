package com.s8.lang.joos.type.primitives;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.lang.joos.composing.ComposingScope;
import com.s8.lang.joos.composing.JOOS_ComposingException;
import com.s8.lang.joos.parsing.JOOS_ParsingException;

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
			throws IOException, JOOS_ComposingException  {
		
		scope.newItem();
		scope.append(name);
		scope.append(": ");
		
		try {
			scope.append(Double.toString(field.getDouble(object)));
		} 
		catch (IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		
		return true;
	}
	

	/*
	@Override
	public String get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return Double.toString(field.getDouble(object));
	}
	*/

}