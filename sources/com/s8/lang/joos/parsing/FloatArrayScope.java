package com.s8.lang.joos.parsing;

import com.s8.lang.joos.ParsingException;

public class FloatArrayScope extends PrimitivesArrayScope<Float> {

	public FloatArrayScope(OnParsedObject callback) {
		super(callback, double.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(Float.valueOf(value));
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		float[] array = new float[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}
}
