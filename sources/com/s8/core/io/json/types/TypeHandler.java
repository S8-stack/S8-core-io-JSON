package com.s8.core.io.json.types;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.JSON_Type;
import com.s8.core.io.json.composing.ComposingScope;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.fields.FieldHandlerFactory;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class TypeHandler {


	public class Builder {


		private JSON_Type typeAnnotation;
		
		Class<?>[] subTypes;

		private List<FieldHandler.Builder> fieldBuilders;

		public Builder() {
			super();

			// get object annotation
			typeAnnotation = type.getAnnotation(JSON_Type.class);

			if(typeAnnotation==null){
				throw new RuntimeException("Missing annotation in type: "+type.getName());
			}
			
			subTypes = typeAnnotation.sub();
		}


		public void build(FieldHandlerFactory factory) throws JSON_CompilingException {

			name = typeAnnotation.name();

			// get object annotation
			/*
			JOOS_Doc docAnnotation = type.getAnnotation(JOOS_Doc.class);
			if(docAnnotation!=null){
				doc = docAnnotation.text();
			}
			 */

			// constructor
			if(!type.isInterface()){
				try {
					constructor = type.getConstructor(new Class<?>[]{});
				}
				catch (NoSuchMethodException | SecurityException e) {
					throw new JSON_CompilingException(type, "[Ws3dTypeHandler] No constructor for type: "+type.getName());
				}
			}


			/* <fields> */

			FieldHandler.Builder fieldBuilder;
			JSON_Field fieldAnnotation;

			fieldBuilders = new ArrayList<>();

			// for each method
			for(Field field : type.getFields()){

				// look for input (setter)
				fieldAnnotation = field.getAnnotation(JSON_Field.class);
				if(fieldAnnotation!=null){

					// check if already existing
					if(fieldHandlers.get(fieldAnnotation.name())!=null){
						throw new JSON_CompilingException(type, "A field is already defined with name: "+fieldAnnotation.name());
					}

					// create field handler
					fieldBuilder = factory.create(field);

					fieldBuilders.add(fieldBuilder);

					fieldHandlers.put(fieldAnnotation.name(), fieldBuilder.getHandler());
				}
			}
		}


		/**
		 * 
		 * @param builder
		 * @throws JSON_CompilingException 
		 */
		public void discover(JSON_Lexicon.Builder lexiconBuilder) throws JSON_CompilingException {

			if(subTypes!=null){
				for(Class<?> subType : subTypes){ lexiconBuilder.discover(subType); }
			}

			// sub-discover by fields
			for(FieldHandler.Builder fieldBuilder : fieldBuilders) {
				fieldBuilder.subDiscover(lexiconBuilder);
			}
		}


		/**
		 * 
		 * @param builder
		 */
		public void compile(JSON_Lexicon.Builder lexiconBuilder) {
			
			if(subTypes!=null){
				for(Class<?> subType : subTypes){ 
					subTypeHandlers.add(lexiconBuilder.getByClassName(subType)); 
				}
			}
			
			fieldBuilders.forEach(fieldBuilder -> fieldBuilder.compile(lexiconBuilder));
		}


		/**
		 * 
		 * @return
		 */
		public TypeHandler getHandler() {
			return TypeHandler.this;
		}

	}

	/**
	 * name of the type
	 */
	private String name;

	/**
	 * constructor for new instances
	 */
	private Constructor<?> constructor;

	/**
	 * child types
	 */
	public List<TypeHandler> subTypeHandlers = new ArrayList<>();

	/**
	 * field handlers
	 */
	public Map<String, FieldHandler> fieldHandlers = new HashMap<String, FieldHandler>();



	/**
	 * 
	 */
	private Class<?> type;


	/** Type doc exposed to client */
	//private String doc;



	/**
	 * 
	 * @param type
	 * @param types
	 * @throws Exception
	 */
	public TypeHandler(Class<?> type){
		super();
		this.type = type;
	}






	public String getName(){
		return name;
	}

	public Object createInstance()
			throws
			InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		return constructor.newInstance(new Object[]{});
	}


	public FieldHandler getFieldHandler(String name) {
		return fieldHandlers.get(name);
	}


	public void compose(Object object, ComposingScope scope, boolean isDefault) 
			throws JSON_ComposingException, IOException {

		// declare type
		if(!isDefault) {
			scope.append(name);
			scope.append('(');			
		}

		// begin body
		ComposingScope enclosedScope = scope.enterSubscope('{', '}', true);

		// write field
		enclosedScope.open();
		for(FieldHandler fieldHandler : fieldHandlers.values()) {
			fieldHandler.compose(object, enclosedScope);
		}
		enclosedScope.close();
		if(!isDefault) {
			scope.append(')');	
		}
	}

}
