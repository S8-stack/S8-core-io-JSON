package com.s8.lang.joos.type.structures;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.s8.lang.joos.JOOS_Context;
import com.s8.lang.joos.JOOS_Type;
import com.s8.lang.joos.composing.ComposingScope;
import com.s8.lang.joos.composing.JOOS_ComposingException;
import com.s8.lang.joos.parsing.JOOS_ParsingException;
import com.s8.lang.joos.parsing.ObjectsArrayScope;
import com.s8.lang.joos.parsing.ObjectsArrayScope.OnParsed;
import com.s8.lang.joos.type.FieldHandler;
import com.s8.lang.joos.type.JOOS_CompilingException;
import com.s8.lang.joos.type.TypeHandler;
import com.s8.lang.joos.parsing.ParsingScope;


/**
 * 
 * @author pc
 *
 */
public class ObjectsListFieldHandler extends FieldHandler {


	private Class<?> componentType;

	public ObjectsListFieldHandler(String name, Field field) {
		super(name, field);

		Type actualComponentType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		
		// if type is like: MySubObject<T>
		if(actualComponentType instanceof ParameterizedType) {
			componentType = (Class<?>) ((ParameterizedType) actualComponentType).getRawType();
		}
		// if type is simply like: MySubObject
		else {
			componentType = (Class<?>) actualComponentType;
		}
	}

	public void set(Object object, Object value) throws JOOS_ParsingException {
		try {
			field.set(object, value);
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
	public void subDiscover(JOOS_Context context) throws JOOS_CompilingException {

		/*
		 * discover component type if annotated (compatibility with generic)
		 */
		if(componentType.getAnnotation(JOOS_Type.class)!=null) {
			context.discover(componentType);	
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean compose(Object object, ComposingScope scope)
			throws IOException, JOOS_ComposingException {

		// retrieve array
		List<Object> list = null;
		try {
			list = (List<Object>) field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new JOOS_ComposingException(e.getMessage());
		}


		if(list!=null) {

			// field description
			scope.newItem();
			scope.append(name);
			scope.append(':');

			ComposingScope enclosedScope = scope.enterSubscope('[', ']', true);

			enclosedScope.open();
			for(Object item : list) {
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
		return new ObjectsArrayScope(componentType, new OnParsed() {

			@Override
			public void set(List<Object> values) throws JOOS_ParsingException {
				ObjectsListFieldHandler.this.set(object, values);
			}
		});
	}
}