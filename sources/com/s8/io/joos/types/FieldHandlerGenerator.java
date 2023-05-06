package com.s8.io.joos.types;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.s8.io.joos.JOOS_Append;
import com.s8.io.joos.JOOS_Get;
import com.s8.io.joos.JOOS_Iterate;
import com.s8.io.joos.JOOS_Put;
import com.s8.io.joos.JOOS_Set;
import com.s8.io.joos.JOOS_Traverse;
import com.s8.io.joos.fields.FieldHandler;
import com.s8.io.joos.fields.FieldHandler.Kind;
import com.s8.io.joos.fields.FieldHandlerFactory;
import com.s8.io.joos.fields.ListFieldHandler;
import com.s8.io.joos.fields.MapFieldHandler;
import com.s8.io.joos.fields.SimpleFieldHandler;
import com.s8.io.joos.fields.SimpleFieldHandler.Builder;
import com.s8.io.joos.parsing.JOOS_ParsingException;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldHandlerGenerator {





	public FieldHandlerGenerator() {
		super();
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
	public void appendMethod(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		if(method.isAnnotationPresent(JOOS_Set.class)) {
			create_Set(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Get.class)) {
			create_Get(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Append.class)) {
			create_Append(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Iterate.class)) {
			create_Iterate(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Put.class)) {
			create_Put(method, fieldBuilders);
		}
		else if(method.isAnnotationPresent(JOOS_Traverse.class)) {
			create_Traverse(method, fieldBuilders);
		}
		// else -> skip
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

		ListFieldHandler.Builder listFieldBuilder = null;

		if(fieldBuilder == null) {
			listFieldBuilder = FieldHandlerFactory.createListFieldBuilder(fieldType, name);
			fieldBuilders.put(name, listFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.SIMPLE) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			listFieldBuilder = (ListFieldHandler.Builder) fieldBuilder;
		}

		listFieldBuilder.setAdder(method);

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
		
		Class<?> paramConsumerType = method.getParameters()[0].getType();
		if(!Consumer.class.isAssignableFrom(paramConsumerType)) {
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Must be a consumer");	
		}

		Type actualComponentType = ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];
		Class<?> componentType = (Class<?>) actualComponentType;

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		ListFieldHandler.Builder listFieldBuilder = null;

		if(fieldBuilder == null) {
			listFieldBuilder = FieldHandlerFactory.createListFieldBuilder(componentType, name);
			fieldBuilders.put(name, listFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.LIST) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			listFieldBuilder = (ListFieldHandler.Builder) fieldBuilder;
		}

		/* set method */
		listFieldBuilder.setIterator(method);
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
	
	
	


	/**
	 * 
	 * @param method
	 * @param fieldBuilders
	 * @throws JOOS_CompilingException 
	 */
	private void create_Traverse(Method method, Map<String, FieldHandler.Builder> fieldBuilders) throws JOOS_CompilingException {

		/* check params count */
		if(method.getParameterCount() != 1){
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Iterate must have only one parameter");
		}
		
		Class<?> paramConsumerType = method.getParameters()[0].getType();
		if(!BiConsumer.class.isAssignableFrom(paramConsumerType)) {
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Must be a consumer");	
		}

		Type[] actualComponentTypes = ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments();
		if(!actualComponentTypes[0].equals(String.class)) {
			throw new JOOS_CompilingException(method.getDeclaringClass(), 
					method.getName()+": Key must be string");
		}
		Class<?> componentType = (Class<?>) actualComponentTypes[1];

		JOOS_Set annotation = method.getAnnotation(JOOS_Set.class);
		String name = annotation.name();

		FieldHandler.Builder fieldBuilder = fieldBuilders.get(name);

		ListFieldHandler.Builder listFieldBuilder = null;

		if(fieldBuilder == null) {
			listFieldBuilder = FieldHandlerFactory.createListFieldBuilder(componentType, name);
			fieldBuilders.put(name, listFieldBuilder);
		}
		else {
			if(fieldBuilder.getHandler().advertise() != Kind.LIST) {
				throw new JOOS_CompilingException(method.getDeclaringClass(), 
						method.getName()+": field is already defined with this name and different type");
			}
			listFieldBuilder = (ListFieldHandler.Builder) fieldBuilder;
		}

		/* set method */
		listFieldBuilder.setIterator(method);
	}
}
