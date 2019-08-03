package com.qx.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Setter_DoubleArray extends Setter_PrimitiveArray {

	@Override
	public void set(Object object, List<String> values, int line) throws DeserializationException {
		try {
			int n = values.size();
			double[] array  = new double[n];
			for(int i=0; i<n; i++){
				array[i] = Double.valueOf(values.get(i));
			}
			method.invoke(object, array);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(line, "Cannot set double array due to: "+e.getMessage());
		}
	}
}
