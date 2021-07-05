package com.s8.blocks.joos.fields.objects;


import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.annotations.JOOS_Type;
import com.s8.blocks.joos.composing.ComposingScope;
import com.s8.blocks.joos.composing.JOOS_ComposingException;
import com.s8.blocks.joos.fields.FieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.parsing.ObjectsArrayScope;
import com.s8.blocks.joos.parsing.ParsingScope;
import com.s8.blocks.joos.types.JOOS_CompilingException;
import com.s8.blocks.joos.types.TypeHandler;


/**
 * 
 * @author pc
 *
 */
public class ObjectsArrayFieldHandler extends FieldHandler {


	private Class<?> componentType;


	public ObjectsArrayFieldHandler(String name, Field field) {
		super(name, field);
		this.componentType = field.getType().getComponentType();
	}

	public void set(Object object, Object valuesArray) throws JOOS_ParsingException {
		try {
			field.set(object, valuesArray);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new JOOS_ParsingException("Cannot set Object array due to "+e.getMessage());
		}
	}


	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}


	@Override
	public Class<?> getSubType() {
		return componentType;
	}


	@Override
	public void subDiscover(JOOS_Lexicon context) throws JOOS_CompilingException {
		
		/*
		 * discover component type if annotated (compatibility with generic)
		 */
		if(componentType.getAnnotation(JOOS_Type.class)!=null) {
			context.discover(componentType);	
		}
	}

	@Override
	public boolean compose(Object object, ComposingScope scope) throws IOException, JOOS_ComposingException {

		// retrieve array
		Object array;
		try {
			array = field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new JOOS_ComposingException(e.getMessage());
		}
		
		if(array!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');
			
			int length = Array.getLength(array);

			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);
			
			enclosedScope.open();
			Object item;
			for(int index=0; index<length; index++) {
				item = Array.get(array, index);
				if(item!=null) {
					
					enclosedScope.newItem();
					TypeHandler typeHandler = enclosedScope.getTypeHandler(item);
					typeHandler.compose(item, enclosedScope);					
				}			
			}
			enclosedScope.close();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public ParsingScope openScope(Object object) {
		return new ObjectsArrayScope(getSubType(), new ObjectsArrayScope.OnParsed() {
			@Override
			public void set(List<Object> values) throws JOOS_ParsingException {
				int length = values.size();
				Object array = Array.newInstance(componentType, length);
				for(int index=0; index<length; index++) {
					Array.set(array, index, values.get(index));
				}
				ObjectsArrayFieldHandler.this.set(object, array);
			}
		});
	}
}