package com.s8.lang.joos.parsing.primitives;

import com.s8.lang.joos.ParsingException;
import com.s8.lang.joos.parsing.JOOS_ParsingException;
import com.s8.lang.joos.parsing.ParsingScope;
import com.s8.lang.joos.parsing.PrimitiveScope;
import com.s8.lang.joos.parsing.PrimitivesArrayScope;

public class BooleanArrayScope extends PrimitivesArrayScope<Boolean> {

	public BooleanArrayScope(OnParsedObject callback) {
		super(callback, double.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(Boolean.valueOf(value));
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		boolean[] array = new boolean[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}

}
