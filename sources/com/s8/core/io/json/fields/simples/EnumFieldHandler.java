package com.s8.core.io.json.fields.simples;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.PrimitiveFieldHandler;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.parsing.StringScope;


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

		public Builder(String name, Field field) {
			super();
			handler = new EnumFieldHandler(name, field);
		}
	}


	private Map<String, Object> map;

	private EnumFieldHandler(String name, Field field) {
		super(name, field);

		Class<?> enumType = field.getType();
		map = new HashMap<>();
		for(Object enumInstance : enumType.getEnumConstants()){
			map.put(enumInstance.toString(), enumInstance);
		}
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new StringScope() {

			@Override
			public void setValue(String value) throws JSON_ParsingException {
				try {
					field.set(object, map.get(value));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new JSON_ParsingException("Cannot set interger due to "+e.getMessage());
				}
			}
		};
	}



	@Override
	public boolean compose(Object object, ComposingScope scope) 
			throws IOException, JSON_ComposingException  {

		scope.newItem();
		scope.append(name);
		scope.append(": ");

		Object item;
		try {
			item = field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JSON_ComposingException(e.getMessage());
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
