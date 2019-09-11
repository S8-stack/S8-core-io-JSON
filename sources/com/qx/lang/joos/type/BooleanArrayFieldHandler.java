package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.lang.joos.composing.ComposingScope;

public class BooleanArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public BooleanArrayFieldHandler(String name, Field field) {
		super(name, field);
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

}
