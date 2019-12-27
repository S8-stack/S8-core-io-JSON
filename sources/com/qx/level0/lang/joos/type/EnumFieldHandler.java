package com.qx.level0.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.joos.ParsingException;
import com.qx.level0.lang.joos.composing.ComposingScope;

public class EnumFieldHandler extends PrimitiveFieldHandler {

	private Map<String, Object> map;

	public EnumFieldHandler(String name, Field field) {
		super(name, field);
		Class<?> enumType = field.getType();
		map = new HashMap<>();
		for(Object enumInstance : enumType.getEnumConstants()){
			map.put(enumInstance.toString(), enumInstance);
		}
	}


	@Override
	public void parse(Object object, String value) throws ParsingException{
		try {
			field.set(object, map.get(value));
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
		Object item = field.get(object);
		if(item!=null) {
			scope.append(item.toString());
		}
		else {
			scope.append("NONE");	
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
