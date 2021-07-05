package com.s8.blocks.joos.fields.primitives;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.fields.PrimitivesArrayFieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.parsing.ParsingScope.OnParsedObject;
import com.s8.blocks.joos.parsing.primitives.ShortArrayScope;

public class ShortArrayFieldHandler extends PrimitivesArrayFieldHandler {

	public ShortArrayFieldHandler(String name, Field field) {
		super(name, field);
	}

	@Override
	public boolean isItemValid(Object array, int index) {
		return true; // always valid
	}
	
	@Override
	public void composeItem(Object array, int index, ComposingScope scope) 
			throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		scope.append(Short.toString(Array.getShort(array, index)));
	}
	
	
	@Override
	public ParsingScope openScope(Object object) {
		return new ShortArrayScope(new OnParsedObject() {
			@Override
			public void set(Object value) throws JOOS_ParsingException {
				try {
					ShortArrayFieldHandler.this.set(object, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new JOOS_ParsingException("Failed to set object due to "+e.getMessage());
				}
			}	
		});
	}

}
