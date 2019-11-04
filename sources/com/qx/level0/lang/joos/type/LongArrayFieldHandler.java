package com.qx.level0.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.level0.lang.joos.composing.ComposingScope;

public class LongArrayFieldHandler extends PrimitivesArrayFieldHandler {

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

}
