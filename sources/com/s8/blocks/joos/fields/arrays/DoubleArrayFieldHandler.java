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

public class DoubleArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public DoubleArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true; // always valid
	}

	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Double.toString(Array.getDouble(array, index)));
	}

	@Override
	public ParsingScope openScope(Object object) {
		return new DoubleArrayScope(object);
	}


	private class DoubleArrayScope extends PrimitivesArrayScope<Double> {

		public DoubleArrayScope(Object object) {
			super(double.class, object);
		}

		@Override
		public ParsingScope openItem() throws JOOS_ParsingException {
			return new PrimitiveScope(){

				@Override
				public void setValue(String value) throws JOOS_ParsingException, ParsingException {
					values.add(Double.valueOf(value));
				}
			};
		}

		@Override
		public void close() throws JOOS_ParsingException {
			int length = values.size();
			double[] array = new double[length];
			for(int i=0; i<length; i++) {
				array[i] = values.get(i);
			}
			setValue(array);
		}
		
		@Override
		public void setValue(Object value) throws JOOS_ParsingException {
			try {
				DoubleArrayFieldHandler.this.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
			}
		}	
	}

}
