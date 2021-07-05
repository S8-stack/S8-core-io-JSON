package com.s8.blocks.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.composing.JOOS_ComposingException;
import com.s8.blocks.joos.fields.PrimitiveFieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.PrimitiveScope;

public class ShortFieldHandler extends PrimitiveFieldHandler {
	
	
	public ShortFieldHandler(String name, Field field) {
		super(name, field);
	}


	
	@Override
	public ParsingScope openScope(Object object) {
		return new PrimitiveScope() {
			public @Override void setValue(String value) throws JOOS_ParsingException {
				try {
					field.setShort(object, Short.valueOf(value));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JOOS_ParsingException("Cannot set interger due to "+e.getMessage());
				}
			}
		};
	}
	
	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, JOOS_ComposingException  {
		
		scope.newItem();
		scope.append(name);
		scope.append(": ");
		
		try {
			scope.append(Short.toString(field.getShort(object)));
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
		return Short.toString(field.getShort(object));
	}
	*/
}
