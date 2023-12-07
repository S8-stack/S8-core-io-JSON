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
import com.s8.core.io.json.parsing.QuotedScope;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class StringArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public static class Builder extends PrimitivesArrayFieldHandler.Builder {

		public Builder(String name, Field field) {
			super(field);
			handler = new StringArrayFieldHandler(name, field);
		}
	}
	
	
	public StringArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return Array.get(array, index)!=null; // always valid
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {

		String value = (String) Array.get(array, index);
		scope.append('"');
		scope.append(value);
		scope.append('"');
	}



	@Override
	public ParsingScope openScope(Object object) {
		
		return new ArrayScope() {
			private List<String> values = new ArrayList<>();
			
			@Override
			public ParsingScope openItemScope() throws JSON_ParsingException {
				return new QuotedScope() {
					@Override
					public void setValue(String value) throws JSON_ParsingException, ParsingException {
						values.add(value);
					}
				};
			}

			@Override
			public void close() throws JSON_ParsingException {
				int length = values.size();
				String[] array = new String[length];
				for(int i=0; i<length; i++) {
					array[i] = values.get(i);
				}
				try {
					StringArrayFieldHandler.this.set(object, array);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JSON_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}
		};
	}

}
