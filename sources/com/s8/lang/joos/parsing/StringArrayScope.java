package com.s8.lang.joos.parsing;

import com.s8.lang.joos.ParsingException;

public class StringArrayScope extends PrimitivesArrayScope<String> {

	public StringArrayScope(OnParsedObject callback) {
		super(callback, String.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(value);
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		String[] array = new String[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}
}
