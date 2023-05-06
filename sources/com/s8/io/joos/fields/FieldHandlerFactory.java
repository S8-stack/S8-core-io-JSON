package com.s8.io.joos.fields;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_PrimitiveExtension;
import com.s8.io.joos.fields.arrays.BooleanArrayFieldHandler;
import com.s8.io.joos.fields.arrays.DoubleArrayFieldHandler;
import com.s8.io.joos.fields.arrays.FloatArrayFieldHandler;
import com.s8.io.joos.fields.arrays.IntegerArrayFieldHandler;
import com.s8.io.joos.fields.arrays.LongArrayFieldHandler;
import com.s8.io.joos.fields.arrays.ObjectsArrayFieldHandler;
import com.s8.io.joos.fields.arrays.ShortArrayFieldHandler;
import com.s8.io.joos.fields.arrays.StringArrayFieldHandler;
import com.s8.io.joos.fields.lists.ObjectsListFieldHandler;
import com.s8.io.joos.fields.lists.StringListFieldHandler;
import com.s8.io.joos.fields.maps.ObjectsMapFieldHandler;
import com.s8.io.joos.fields.simples.BooleanFieldHandler;
import com.s8.io.joos.fields.simples.DoubleFieldHandler;
import com.s8.io.joos.fields.simples.EnumFieldHandler;
import com.s8.io.joos.fields.simples.FloatFieldHandler;
import com.s8.io.joos.fields.simples.IntegerFieldHandler;
import com.s8.io.joos.fields.simples.LongFieldHandler;
import com.s8.io.joos.fields.simples.ObjectFieldHandler;
import com.s8.io.joos.fields.simples.ShortFieldHandler;
import com.s8.io.joos.fields.simples.StringFieldHandler;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.types.JOOS_CompilingException;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldHandlerFactory {

	


	private final List<JOOS_PrimitiveExtension<?>> extensions;

	public FieldHandlerFactory() {
		super();
		extensions = new ArrayList<JOOS_PrimitiveExtension<?>>();
	}

	public <T> void add(JOOS_PrimitiveExtension<T> extension) {
		extensions.add(extension);
	}




	/**
	 * 
	 * @param annotation : annotation of the field
	 * @param field : the field itself
	 * @param objectType : type of the parent object owning the field
	 * @return
	 * @throws JOOS_ParsingException 
	 * @throws Exception
	 */
	public FieldHandler.Builder create(Field field) throws JOOS_CompilingException {

		JOOS_Field annotation = field.getAnnotation(JOOS_Field.class);

		String name = annotation.name();
		Class<?> fieldType = field.getType();

		/*
		for(JOOS_PrimitiveExtension<?> extension : extensions) {
			if(extension.isMatching(fieldType)) {
				return extension.createFieldHandler(name, field);
			}
		}
		*/

		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == boolean.class){
				return new BooleanFieldHandler.Builder(name, field);
			}
			else if(fieldType == short.class){
				return new ShortFieldHandler.Builder(name, field);
			}
			else if(fieldType == int.class){
				return new IntegerFieldHandler.Builder(name, field);
			}
			else if(fieldType == long.class){
				return new LongFieldHandler.Builder(name, field);
			}
			else if(fieldType == float.class){
				return new FloatFieldHandler.Builder(name, field);
			}
			else if(fieldType == double.class){
				return new DoubleFieldHandler.Builder(name, field);
			}
			else{
				throw new RuntimeException("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			return new StringFieldHandler.Builder(name, field);
		}
		// enum
		else if(fieldType.isEnum()){
			return new EnumFieldHandler.Builder(name, field);
		}
		// array
		else if(fieldType.isArray()){
			Class<?> componentType = fieldType.getComponentType();

			// array of primitive
			if(componentType.isPrimitive()){
				if(componentType==boolean.class) {
					return new BooleanArrayFieldHandler.Builder(name, field);
				}
				else if(componentType==short.class) {
					return new ShortArrayFieldHandler.Builder(name, field);
				}
				else if(componentType==int.class) {
					return new IntegerArrayFieldHandler.Builder(name, field);
				}
				else if(componentType==long.class) {
					return new LongArrayFieldHandler.Builder(name, field);
				}
				else if(componentType==float.class) {
					return new FloatArrayFieldHandler.Builder(name, field);
				}
				else if(componentType==double.class) {
					return new DoubleArrayFieldHandler.Builder(name, field);
				}
				else {
					throw new JOOS_CompilingException(field.getDeclaringClass(), 
							"Primitives array type not supported "+componentType);
				}
			}
			else if(componentType==String.class) {
				return new StringArrayFieldHandler.Builder(name, field);
			}
			// array of object
			else{
				return new ObjectsArrayFieldHandler.Builder(name, field);
			}
		}
		else if(List.class.isAssignableFrom(fieldType)) {
			Type actualComponentType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			
			Class<?> componentType = null;
			// if type is like: MySubObject<T>
			if(actualComponentType instanceof ParameterizedType) {
				componentType = (Class<?>) ((ParameterizedType) actualComponentType).getRawType();
			}
			// if type is simply like: MySubObject
			else if(actualComponentType instanceof Class<?>){
				componentType = (Class<?>) actualComponentType;
			}
			
			if(componentType==String.class) {
				return new StringListFieldHandler.Builder(name, field);
			}
			// array of object
			else{
				return new ObjectsListFieldHandler.Builder(name, field, componentType);
			}
		}
		else if(Map.class.isAssignableFrom(fieldType)) {
			
			Type[] typeVars = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();


			Type key = typeVars[0];
			if(!key.equals(String.class)) {
				throw new JOOS_CompilingException(field.getType(), "Only String are accetped as keys");
			}

			Type actualValueType = typeVars[1];

			Class<?> componentType = null;
			
			// if type is like: MySubObject<T>
			if(actualValueType instanceof ParameterizedType) {
				componentType = (Class<?>) ((ParameterizedType) actualValueType).getRawType();
			}
			// if type is simply like: MySubObject
			else if(actualValueType instanceof Class<?>){
				componentType = (Class<?>) actualValueType;
			}
			
			return new ObjectsMapFieldHandler.Builder(name, field, componentType);
		}
		// default to object
		else{
			return new ObjectFieldHandler.Builder(name, field);	
		}
	}
}
