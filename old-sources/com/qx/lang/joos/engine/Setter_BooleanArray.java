package com.qx.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Setter_BooleanArray extends Setter_PrimitiveArray {

	@Override
	public void set(Object object, List<String> values, int line) throws DeserializationException {
	
		try {
			int n = values.size();
			boolean[] array  = new boolean[n];
			for(int i=0; i<n; i++){
				array[i] = Boolean.valueOf(values.get(i));
			}
			method.invoke(object, array);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot set boolean array due to "+e.getMessage());
		}
	}
}
