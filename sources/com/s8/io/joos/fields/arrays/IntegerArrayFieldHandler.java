package com.s8.io.joos.fields.arrays;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.s8.io.joos.ParsingException;
import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.fields.PrimitivesArrayFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.PrimitiveScope;
import com.s8.io.joos.parsing.PrimitivesArrayScope;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class IntegerArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public IntegerArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true; // always valid
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Integer.toString(Array.getInt(array, index)));
	}

	@Override
	public ParsingScope openScope(Object object) {
		return new IntegerArrayScope(object);
	}



	private class IntegerArrayScope extends PrimitivesArrayScope<Integer> {

		public IntegerArrayScope(Object object) {
			super(int.class, object);
		}

		@Override
		public ParsingScope openItem() throws JOOS_ParsingException {
			return new PrimitiveScope() {

				@Override
				public void setValue(String value) throws JOOS_ParsingException, ParsingException {
					values.add(Integer.valueOf(value));
				}
			};
		}

		@Override
		public void close() throws JOOS_ParsingException {
			int length = values.size();
			int[] array = new int[length];
			for(int i=0; i<length; i++) {
				array[i] = values.get(i);
			}
			setValue(array);
		}
		
		@Override
		public void setValue(Object value) throws JOOS_ParsingException {
			try {
				IntegerArrayFieldHandler.this.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
			}
		}	
	}

}
