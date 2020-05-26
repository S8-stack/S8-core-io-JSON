package com.s8.lang.joos.parsing;

import com.s8.lang.joos.ParsingException;

public class ShortArrayScope extends PrimitivesArrayScope<Short> {

	public ShortArrayScope(OnParsedObject callback) {
		super(callback, short.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(Short.valueOf(value));
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		short[] array = new short[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}
}
