package com.qx.back.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Setter_StringArray extends Setter_PrimitiveArray {

	@Override
	public void set(Object object, List<String> values, int line) throws DeserializationException {

		try {
			int n = values.size();
			String[] array  = new String[n];
			for(int i=0; i<n; i++){
				array[i] = values.get(i);
			}
			Object array2 = array;

			method.invoke(object, array2);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot set String array due to: "+e.getMessage());
		}
	}
}
