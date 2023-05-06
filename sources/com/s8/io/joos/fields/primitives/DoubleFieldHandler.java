package com.s8.io.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.AlphaNumericScope;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DoubleFieldHandler extends PrimitiveFieldHandler {

	public static class Builder extends PrimitiveFieldHandler.Builder {

		public Builder(String name) {
			super();
			handler = new DoubleFieldHandler(name);
		}
	}

	public DoubleFieldHandler(String name) {
		super(name);
	}


	@Override
	public ParsingScope openScope(Object object) {
		return new AlphaNumericScope() {

			@Override
			public void setValue(String value) throws JOOS_ParsingException {
				try {
					setter.invoke(object, Double.valueOf(value));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new JOOS_ParsingException("Cannot deserialize double due to: "+e.getMessage());
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
			double value = (double) getter.invoke(object, new Object[]{});
			scope.append(Double.toString(value));
		} 
		catch (IllegalArgumentException | IllegalAccessException | IOException | InvocationTargetException e) {
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