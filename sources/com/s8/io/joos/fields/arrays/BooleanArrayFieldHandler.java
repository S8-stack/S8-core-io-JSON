package com.s8.io.joos.fields.arrays;

import java.io.IOException;
import java.lang.reflect.Array;

import com.s8.io.joos.ParsingException;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.fields.PrimitivesArrayFieldHandler;
import com.s8.io.joos.parsing.AlphaNumericScope;
import com.s8.io.joos.parsing.ArrayScope;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class BooleanArrayFieldHandler extends PrimitivesArrayFieldHandler {


	public static class Builder extends PrimitivesArrayFieldHandler.Builder {

		public Builder(String name) {
			super(boolean.class);
			handler = new BooleanArrayFieldHandler(name);
		}
	}


	public BooleanArrayFieldHandler(String name) {
		super(name);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true;
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Boolean.toString(Array.getBoolean(array, index)));
	}

	@Override
	public ParsingScope openScope(Object object) {
		
		return new ArrayScope() {
			
			@Override
			public ParsingScope openItemScope() throws JOOS_ParsingException {
				return new AlphaNumericScope() {
					@Override
					public void setValue(String value) throws JOOS_ParsingException, ParsingException {
						add(object, Boolean.valueOf(value));
					}
				};
			}

			@Override
			public void close() throws JOOS_ParsingException {
				// do nothing
			}
		};
	}

	

}
