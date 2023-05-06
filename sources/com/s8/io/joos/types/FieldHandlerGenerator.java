package com.s8.io.joos.types;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.s8.io.joos.JOOS_Append;
import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_Get;
import com.s8.io.joos.JOOS_PrimitiveExtension;
import com.s8.io.joos.JOOS_Set;
import com.s8.io.joos.fields.FieldHandler;
import com.s8.io.joos.fields.ListFieldHandler;
import com.s8.io.joos.fields.MapFieldHandler;
import com.s8.io.joos.fields.SimpleFieldHandler;
import com.s8.io.joos.fields.FieldHandler.Kind;
import com.s8.io.joos.fields.FieldHandlerFactory;
import com.s8.io.joos.fields.SimpleFieldHandler.Builder;
import com.s8.io.joos.fields.arrays.BooleanArrayFieldHandler;
import com.s8.io.joos.fields.arrays.DoubleArrayFieldHandler;
import com.s8.io.joos.fields.arrays.DoubleListFieldHandler;
import com.s8.io.joos.fields.arrays.FloatArrayFieldHandler;
import com.s8.io.joos.fields.arrays.IntegerArrayFieldHandler;
import com.s8.io.joos.fields.arrays.LongArrayFieldHandler;
import com.s8.io.joos.fields.arrays.ShortArrayFieldHandler;
import com.s8.io.joos.fields.arrays.StringArrayFieldHandler;
import com.s8.io.joos.fields.objects.ObjectFieldHandler;
import com.s8.io.joos.fields.objects.ObjectsArrayFieldHandler;
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
import com.s8.io.joos.parsing.JOOS_ParsingException;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldHandlerGenerator {




	private final List<JOOS_PrimitiveExtension<?>> extensions;

	public FieldHandlerGenerator() {
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
	public FieldHandler.Builder create(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		if(method.isAnnotationPresent(JOOS_Set.class)) {
			create_Set(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Get.class)) {
			create_Get(method, fieldBuilders);
		}
		if(method.isAnnotationPresent(JOOS_Append.class)) {
			create_Append(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Get.class)) {
			create_Get(method, fieldBuilders);
		}
		
	}

	/**
	 * 
	 * @param method
	 * @param fieldBuilders
	 * @throws JOOS_CompilingException 
	 */
	private void create_Set(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 1){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Setter must have only one parameter");
		}

		Class<?> fieldType = method.getParameters()[0].getType();

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		SimpleFieldHandler.Builder simpleFieldBuilder = null;

		if(fieldBuilder == null) {
			simpleFieldBuilder = FieldHandlerFactory.createSimpleFieldBuilder(fieldType, name);
			fieldBuilders.put(name, simpleFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.SIMPLE) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			simpleFieldBuilder = (Builder) fieldBuilder;
		}

		simpleFieldBuilder.setSetter(method);

	}
	
	
	/**
	 * 
	 * @param method
	 * @param fieldBuilders
	 * @throws JOOS_CompilingException 
	 */
	private static void create_Get(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 0){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Setter must have no parameters");
		}

		Class<?> fieldType = method.getReturnType();

		JOOS_Get annotation = method.getAnnotation(JOOS_Get.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		SimpleFieldHandler.Builder simpleFieldBuilder = null;

		if(fieldBuilder == null) {
			simpleFieldBuilder = FieldHandlerFactory.createSimpleFieldBuilder(fieldType, name);
			fieldBuilders.put(name, simpleFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.SIMPLE) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			simpleFieldBuilder = (Builder) fieldBuilder;
		}

		simpleFieldBuilder.setGetter(method);
	}
	
	
	
	/**
	 * 
	 * @param method
	 * @param fieldBuilders
	 * @throws JOOS_CompilingException 
	 */
	private void create_Append(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 1){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Append must have only one parameter");
		}

		Class<?> fieldType = method.getParameters()[0].getType();

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		SimpleFieldHandler.Builder simpleFieldBuilder = null;

		if(fieldBuilder == null) {
			simpleFieldBuilder = FieldHandlerFactory.createSimpleFieldBuilder(fieldType, name);
			fieldBuilders.put(name, simpleFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.SIMPLE) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			simpleFieldBuilder = (Builder) fieldBuilder;
		}

		simpleFieldBuilder.setSetter(method);

	}
	
	
	/**
	 * 
	 * @param method
	 * @param fieldBuilders
	 * @throws JOOS_CompilingException 
	 */
	private void create_Iterate(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 1){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Iterate must have only one parameter");
		}

		
		
		Class<?> paramType = method.getParameters()[0].getType();

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		SimpleFieldHandler.Builder simpleFieldBuilder = null;

		if(fieldBuilder == null) {
			simpleFieldBuilder = FieldHandlerFactory.createSimpleFieldBuilder(fieldType, name);
			fieldBuilders.put(name, simpleFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.SIMPLE) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			simpleFieldBuilder = (Builder) fieldBuilder;
		}

		simpleFieldBuilder.setSetter(method);

	}



	private void create_Put(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 2){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Put metghod must have exactly two parameters");
		}

		if(method.getParameters()[0].getType() != String.class){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": first parameter must be a String key");
		}

		Class<?> fieldType = method.getParameters()[1].getType();

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		MapFieldHandler.Builder mapFieldBuilder = null;

		if(fieldBuilder == null) {
			mapFieldBuilder = FieldHandlerFactory.createMapFieldBuilder(fieldType, name);
			fieldBuilders.put(name, mapFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.MAP) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			mapFieldBuilder = (MapFieldHandler.Builder) fieldBuilder;
		}

		mapFieldBuilder.setPutter(method);
	}
	
	
	private void create_Traverse(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 2){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Put metghod must have exactly two parameters");
		}

		if(method.getParameters()[0].getType() != String.class){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": first parameter must be a String key");
		}

		Class<?> fieldType = method.getParameters()[1].getType();

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		MapFieldHandler.Builder mapFieldBuilder = null;

		if(fieldBuilder == null) {
			mapFieldBuilder = createMapFieldBuilder(fieldType, name);
			fieldBuilders.put(name, mapFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.MAP) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			mapFieldBuilder = (MapFieldHandler.Builder) fieldBuilder;
		}

		mapFieldBuilder.setPutter(method);
	}


	private SimpleFieldHandler.Builder createSimpleFieldBuilder(Class<?> fieldType, String name) {
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



	private ListFieldHandler.Builder createListFieldBuilder(Class<?> fieldType, String name) {
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


	private MapFieldHandler.Builder createMapFieldBuilder(Class<?> fieldType, String name) throws JOOS_CompilingException {
		// primitive
		if(fieldType.isPrimitive()){
			throw new RuntimeException("Primitive type not supported "+fieldType.getName());
		}
		else{
			return new ObjectsMapFieldHandler.Builder(name, fieldType);	
		}
	}
}
