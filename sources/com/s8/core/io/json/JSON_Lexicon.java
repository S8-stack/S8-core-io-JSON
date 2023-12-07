package com.s8.core.io.json;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.s8.core.io.json.composing.Composer;
import com.s8.core.io.json.composing.JSON_ComposingException;
import com.s8.core.io.json.composing.JSON_Writer;
import com.s8.core.io.json.fields.FieldHandlerFactory;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.parsing.JSON_Reader;
import com.s8.core.io.json.parsing.Parser;
import com.s8.core.io.json.parsing.StreamReader;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.types.TypeHandler;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class JSON_Lexicon {
	
	
	public static JSON_Lexicon from(Class<?>... types) throws JSON_CompilingException {
		JSON_Lexicon lexicon = new JSON_Lexicon();
		Builder builder = lexicon.new Builder();
		for(Class<?> type : types) { builder.discover(type); }
		
		builder.build();
		
		return lexicon;
	}


	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public class Builder {


		private final FieldHandlerFactory fieldHandlerFactory;

		public Queue<Class<?>> buffer = new LinkedList<>();

		private Map<String, TypeHandler.Builder> typeBuilders = new HashMap<String, TypeHandler.Builder>();


		public Builder() {
			fieldHandlerFactory = new FieldHandlerFactory();
		}
		
		
		/**
		 * 
		 * @param type
		 * @throws JSON_CompilingException
		 */
		public void discover(Class<?> type) throws JSON_CompilingException{
			if(type==null){
				throw new RuntimeException("[Ws3dContext] Type is null");
			}

			JSON_Type annotation = type.getAnnotation(JSON_Type.class);
			if(annotation==null){
				throw new RuntimeException("[Ws3dContext] Type is not annotated: "+type);
			}
			
			// 
			buffer.add(type);
		}



		public void build() throws JSON_CompilingException{
			
			while(!buffer.isEmpty()) {
				
				Class<?> type = buffer.poll();
				if(!typeBuilders.containsKey(type.getName())) {
					
					TypeHandler handler = new TypeHandler(type);
					TypeHandler.Builder typeBuilder = handler.new Builder();
					
					// store
					typeBuilders.put(type.getName(), typeBuilder);
					
					
					typeBuilder.build(fieldHandlerFactory);
					
					typeBuilder.discover(this);
				}
			}

			
			
			typeBuilders.forEach((nkey, builder) -> {
				builder.compile(this);
			});
			
			
			typeBuilders.forEach((name, typeBuilder) -> {
				TypeHandler typeHandler = typeBuilder.getHandler();
				typeHandlers.put(typeHandler.getName(), typeHandler);
			});
		}




		/**
		 * 
		 * @param extension
		 */
		public <T> void definePrimitiveExtension(JSON_PrimitiveExtension<T> extension) {
			fieldHandlerFactory.add(extension);
		}


		public FieldHandlerFactory getFieldFactory() {
			return fieldHandlerFactory;
		}


		/**
		 * 
		 * @param type
		 * @return
		 */
		public TypeHandler getByClassName(Class<?> type){
			return typeBuilders.get(type.getName()).getHandler();
		}
	}

	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, TypeHandler> typeHandlers = new HashMap<String, TypeHandler>();



	private JSON_Lexicon(){
		super();
	}



	/**
	 * 
	 * @param type
	 * @param JAVAIndexedTypes
	 * @param localTypes
	 * 
	 * @return the type handler and create it if necessary
	 * @throws JSON_ComposingException 
	 * @throws JSON_ParsingException 
	 * @throws Exception 
	 */
	public TypeHandler get(Class<?> type) throws JSON_ComposingException {

		if(type==null){
			throw new JSON_ComposingException("[Ws3dContext] Type is null");
		}

		JSON_Type annotation = type.getAnnotation(JSON_Type.class);
		if(annotation==null){
			throw new JSON_ComposingException("[Ws3dContext] Type is not annotated: "+type);
		}

		String name = annotation.name();
		TypeHandler typeHandler = typeHandlers.get(name);
		return typeHandler;
	}


	public TypeHandler get(String name){
		return typeHandlers.get(name);
	}


	
	
	/**
	 * 
	 * @param reader
	 * @param isVerbose
	 * @return
	 * @throws JSON_ParsingException
	 * @throws IOException
	 */
	public Object parse(JSON_Reader reader, Class<?> rootType, boolean isVerbose) throws JSON_ParsingException, IOException {
		Parser parser = new Parser(this, new StreamReader(reader), isVerbose);
		return parser.parse(rootType);
	}
	
	/**
	 * 
	 * @param reader
	 * @param isVerbose
	 * @return
	 * @throws JSON_ParsingException
	 * @throws IOException
	 */
	public Object parse(JSON_Reader reader, boolean isVerbose) throws JSON_ParsingException, IOException {
		return parse(reader, null, isVerbose);
	}


	/**
	 * 
	 * @param writer
	 * @param object
	 * @param indentSequence
	 * @param isVerbose
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws JSON_CompilingException 
	 * @throws JSON_ComposingException 
	 */
	public void compose(JSON_Writer writer, Object object, String indentSequence,  boolean isVerbose) 
			throws IOException,  JSON_ComposingException {
		Composer composer = new Composer(this, writer, indentSequence, isVerbose);
		composer.compose(object);
	}
}
