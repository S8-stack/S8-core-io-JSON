package com.s8.lang.joos.parsing;

import com.s8.lang.joos.ParsingException;

public class LongArrayScope extends PrimitivesArrayScope<Long> {

	public LongArrayScope(OnParsedObject callback) {
		super(callback, long.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(Long.valueOf(value));
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		long[] array = new long[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}
}
