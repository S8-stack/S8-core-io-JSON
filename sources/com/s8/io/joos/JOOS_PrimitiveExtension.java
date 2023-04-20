package com.s8.io.joos;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.joos.composing.ComposingScope;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.fields.PrimitiveFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.ParsingScope;
import com.s8.io.joos.parsing.AlphaNumericScope;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * @param <T>
 */
public abstract class JOOS_PrimitiveExtension<T> {

	private Class<?> type;

	public JOOS_PrimitiveExtension(Class<?> type){
		super();
		this.type = type;
	}

	public boolean isMatching(Class<?> fieldType) {
		return type.equals(fieldType);
	}

	public abstract String serialize(T value);

	public abstract T deserialize(String str);
	
	private class GeneratedFieldHandler extends PrimitiveFieldHandler {
		
		
		public GeneratedFieldHandler(String name, Field field) {
			super(name, field);
		}

		@Override
		public ParsingScope openScope(Object object) {
			return new AlphaNumericScope() {
				
				@Override
				public void setValue(String value) throws JOOS_ParsingException {
					try {
						field.set(object, deserialize(value));
					} 
					catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						throw new JOOS_ParsingException(e.getMessage());
					}
				}
			};
		}
		

		@SuppressWarnings("unchecked")
		@Override
		public boolean compose(Object object, ComposingScope scope) throws IOException, JOOS_ComposingException {
			
			scope.newItem();
			scope.append(name);
			scope.append(": ");
			
			try {
				scope.append(serialize((T) field.get(object)));
			} 
			catch (IllegalArgumentException | IllegalAccessException | IOException e) {
				e.printStackTrace();
				throw new JOOS_ComposingException(e.getMessage());
			}
			
			return true;
		}

	}


	public PrimitiveFieldHandler createFieldHandler(String name, Field field) {
		return new GeneratedFieldHandler(name, field);	
	}
}
