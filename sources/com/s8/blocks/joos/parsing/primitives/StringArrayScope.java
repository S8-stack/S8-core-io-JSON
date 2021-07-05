package com.s8.blocks.joos.parsing.primitives;

import com.s8.blocks.joos.ParsingException;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.PrimitiveScope;
import com.s8.blocks.joos.parsing.PrimitivesArrayScope;

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
