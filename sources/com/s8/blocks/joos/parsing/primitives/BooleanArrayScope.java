package com.s8.blocks.joos.parsing.primitives;

import com.s8.blocks.joos.ParsingException;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.PrimitiveScope;
import com.s8.blocks.joos.parsing.PrimitivesArrayScope;

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
