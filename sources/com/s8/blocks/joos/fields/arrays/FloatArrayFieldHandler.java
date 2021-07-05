package com.s8.blocks.joos.fields.arrays;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.s8.blocks.joos.ParsingException;
import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.fields.PrimitivesArrayFieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.PrimitiveScope;
import com.s8.blocks.joos.parsing.PrimitivesArrayScope;

public class FloatArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public FloatArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true; // always valid
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Float.toString(Array.getFloat(array, index)));
	}

	@Override
	public ParsingScope openScope(Object object) {
		return new FloatArrayScope(object);
	}


	private class FloatArrayScope extends PrimitivesArrayScope<Float> {

		public FloatArrayScope(Object object) {
			super(float.class, object);
		}

		@Override
		public ParsingScope openItem() throws JOOS_ParsingException {
			return new PrimitiveScope() {

				@Override
				public void setValue(String value) throws JOOS_ParsingException, ParsingException {
					values.add(Float.valueOf(value));
				}
			};
		}

		@Override
		public void close() throws JOOS_ParsingException {
			int length = values.size();
			float[] array = new float[length];
			for(int i=0; i<length; i++) {
				array[i] = values.get(i);
			}
			setValue(array);
		}
		
		@Override
		public void setValue(Object value) throws JOOS_ParsingException {
			try {
				FloatArrayFieldHandler.this.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
			}
		}	
	}

}
