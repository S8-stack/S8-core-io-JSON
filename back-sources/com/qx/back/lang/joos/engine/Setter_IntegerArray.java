package com.qx.back.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Setter_IntegerArray extends Setter_PrimitiveArray {

	@Override
	public void set(Object object, List<String> values, int line) throws DeserializationException {

		try {
			int n = values.size();
			int[] array  = new int[n];
			for(int i=0; i<n; i++){
				array[i] = Integer.valueOf(values.get(i));
			}
			method.invoke(object, array);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot set integer array due to "+e.getMessage());
		}
	}
}
