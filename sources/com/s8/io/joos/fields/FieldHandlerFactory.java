package com.s8.io.joos.fields;

import com.s8.io.joos.fields.arrays.BooleanArrayFieldHandler;
import com.s8.io.joos.fields.arrays.DoubleListFieldHandler;
import com.s8.io.joos.fields.arrays.FloatArrayFieldHandler;
import com.s8.io.joos.fields.arrays.IntegerArrayFieldHandler;
import com.s8.io.joos.fields.arrays.LongArrayFieldHandler;
import com.s8.io.joos.fields.arrays.ShortArrayFieldHandler;
import com.s8.io.joos.fields.arrays.StringArrayFieldHandler;
import com.s8.io.joos.fields.objects.ObjectFieldHandler;
import com.s8.io.joos.fields.primitives.BooleanFieldHandler;
import com.s8.io.joos.fields.primitives.DoubleFieldHandler;
import com.s8.io.joos.fields.primitives.EnumFieldHandler;
import com.s8.io.joos.fields.primitives.FloatFieldHandler;
import com.s8.io.joos.fields.primitives.IntegerFieldHandler;
import com.s8.io.joos.fields.primitives.LongFieldHandler;
import com.s8.io.joos.fields.primitives.ShortFieldHandler;
import com.s8.io.joos.fields.primitives.StringFieldHandler;
import com.s8.io.joos.fields.structures.ObjectsListFieldHandler;
import com.s8.io.joos.fields.structures.ObjectsMapFieldHandler;
import com.s8.io.joos.types.JOOS_CompilingException;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldHandlerFactory {



	public static SimpleFieldHandler.Builder createSimpleFieldBuilder(Class<?> fieldType, String name) {
		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == boolean.class){
				return new BooleanFieldHandler.Builder(name);
			}
			else if(fieldType == short.class){
				return new ShortFieldHandler.Builder(name);
			}
			else if(fieldType == int.class){
				return new IntegerFieldHandler.Builder(name);
			}
			else if(fieldType == long.class){
				return new LongFieldHandler.Builder(name);
			}
			else if(fieldType == float.class){
				return new FloatFieldHandler.Builder(name);
			}
			else if(fieldType == double.class){
				return new DoubleFieldHandler.Builder(name);
			}
			else{
				throw new RuntimeException("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			return new StringFieldHandler.Builder(name);
		}
		// enum
		else if(fieldType.isEnum()){
			return new EnumFieldHandler.Builder(name, fieldType);
		}
		else{
			return new ObjectFieldHandler.Builder(name, fieldType);	
		}
	}



	public static ListFieldHandler.Builder createListFieldBuilder(Class<?> fieldType, String name) {
		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == boolean.class){
				return new BooleanArrayFieldHandler.Builder(name);
			}
			else if(fieldType == short.class){
				return new ShortArrayFieldHandler.Builder(name);
			}
			else if(fieldType == int.class){
				return new IntegerArrayFieldHandler.Builder(name);
			}
			else if(fieldType == long.class){
				return new LongArrayFieldHandler.Builder(name);
			}
			else if(fieldType == float.class){
				return new FloatArrayFieldHandler.Builder(name);
			}
			else if(fieldType == double.class){
				return new DoubleListFieldHandler.Builder(name);
			}
			else{
				throw new RuntimeException("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			return new StringArrayFieldHandler.Builder(name);
		}
		else{
			return new ObjectsListFieldHandler.Builder(name, fieldType);	
		}
	}


	public static MapFieldHandler.Builder createMapFieldBuilder(Class<?> fieldType, String name) throws JOOS_CompilingException {
		// primitive
		if(fieldType.isPrimitive()){
			throw new JOOS_CompilingException(fieldType, "Primitive type not supported "+fieldType.getName());
		}
		else{
			return new ObjectsMapFieldHandler.Builder(name, fieldType);	
		}
	}
}
