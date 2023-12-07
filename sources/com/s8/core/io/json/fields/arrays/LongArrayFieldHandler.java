package com.s8.core.io.json.fields.arrays;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.s8.core.io.json.ParsingException;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.fields.PrimitivesArrayFieldHandler;
import com.s8.core.io.json.parsing.ArrayScope;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.ParsingScope;
import com.s8.core.io.json.parsing.StringScope;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class LongArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public static class Builder extends PrimitivesArrayFieldHandler.Builder {

		public Builder(String name, Field field) {
			super(field);
			handler = new LongArrayFieldHandler(name, field);
		}
	}
	
	
	public LongArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true; // always valid
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Long.toString(Array.getLong(array, index)));
	}



	@Override
	public ParsingScope openScope(Object object) {
		
		return new ArrayScope() {
			private List<Long> values = new ArrayList<>();
			
			@Override
			public ParsingScope openItemScope() throws JSON_ParsingException {
				return new StringScope() {
					@Override
					public void setValue(String value) throws JSON_ParsingException, ParsingException {
						values.add(Long.valueOf(value));
					}
				};
			}

			@Override
			public void close() throws JSON_ParsingException {
				int length = values.size();
				long[] array = new long[length];
				for(int i=0; i<length; i++) {
					array[i] = values.get(i);
				}
				try {
					LongArrayFieldHandler.this.set(object, array);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JSON_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}
}
