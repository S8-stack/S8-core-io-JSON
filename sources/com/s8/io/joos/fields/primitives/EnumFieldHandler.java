package com.s8.io.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.AlphaNumericScope;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;


/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class EnumFieldHandler extends PrimitiveFieldHandler {


	public static class Builder extends PrimitiveFieldHandler.Builder {

		public Builder(String name, Class<?> enumType) {
			super();
			handler = new EnumFieldHandler(name, enumType);
		}
	}


	private Map<String, Object> map;

	private EnumFieldHandler(String name, Class<?> enumType) {
		super(name);

		map = new HashMap<>();
		for(Object enumInstance : enumType.getEnumConstants()){
			map.put(enumInstance.toString(), enumInstance);
		}
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new AlphaNumericScope() {

			@Override
			public void setValue(String value) throws JOOS_ParsingException {
				try {
					setter.invoke(object, map.get(value));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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

		Object item;
		try {
			item = getter.invoke(object, new Object[]{});
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
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
