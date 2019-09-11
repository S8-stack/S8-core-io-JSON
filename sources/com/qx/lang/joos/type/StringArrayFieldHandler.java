package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.qx.lang.joos.composing.ComposingScope;

public class StringArrayFieldHandler extends PrimitivesArrayFieldHandler {

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
		scope.append('(');
		scope.append(Integer.toString(value.length()));
		scope.append(')');
		scope.append(value);
	}

}
