package com.s8.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.s8.lang.joos.composing.ComposingScope;

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

}
