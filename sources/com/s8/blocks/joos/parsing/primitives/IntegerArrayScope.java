package com.s8.blocks.joos.parsing.primitives;

import com.s8.blocks.joos.ParsingException;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.PrimitiveScope;
import com.s8.blocks.joos.parsing.PrimitivesArrayScope;

public class IntegerArrayScope extends PrimitivesArrayScope<Integer> {

	public IntegerArrayScope(OnParsedObject callback) {
		super(callback, int.class);
	}

	@Override
	public ParsingScope openItem() throws JOOS_ParsingException {
		return new PrimitiveScope(new OnParsedValue() {

			@Override
			public void set(String value) throws JOOS_ParsingException, ParsingException {
				values.add(Integer.valueOf(value));
			}
		});
	}

	@Override
	public void close() throws JOOS_ParsingException {
		int length = values.size();
		int[] array = new int[length];
		for(int i=0; i<length; i++) {
			array[i] = values.get(i);
		}
		callback.set(array);
	}
}
