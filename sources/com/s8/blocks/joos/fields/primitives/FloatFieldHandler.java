package com.s8.blocks.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.composing.JOOS_ComposingException;
import com.s8.blocks.joos.fields.PrimitiveFieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;

public class FloatFieldHandler extends PrimitiveFieldHandler {
	
	public FloatFieldHandler(String name, Field field) {
		super(name, field);
	}


	@Override
	public void parse(Object object, String value) throws JOOS_ParsingException {
		try {
			field.setFloat(object, Float.valueOf(value));
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
			scope.append(Float.toString(field.getFloat(object)));
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
		return Float.toString(field.getFloat(object));
	}
	*/

}