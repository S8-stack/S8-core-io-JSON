package com.s8.io.joos;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.s8.io.joos.composing.Composer;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.composing.JOOS_Writer;
import com.s8.io.joos.fields.FieldHandlerFactory;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.parsing.JOOS_Reader;
import com.s8.io.joos.parsing.Parser;
import com.s8.io.joos.parsing.StreamReader;
import com.s8.io.joos.types.JOOS_CompilingException;
import com.s8.io.joos.types.TypeHandler;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class JOOS_Lexicon {
	
	
	public static JOOS_Lexicon from(Class<?>... types) throws JOOS_CompilingException {
		JOOS_Lexicon lexicon = new JOOS_Lexicon();
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
		 * @throws JOOS_CompilingException
		 */
		public void discover(Class<?> type) throws JOOS_CompilingException{
			if(type==null){
				throw new RuntimeException("[Ws3dContext] Type is null");
			}

			JOOS_Type annotation = type.getAnnotation(JOOS_Type.class);
			if(annotation==null){
				throw new RuntimeException("[Ws3dContext] Type is not annotated: "+type);
			}
			
			// 
			buffer.add(type);
		}



		public void build() throws JOOS_CompilingException{
			
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
		public <T> void definePrimitiveExtension(JOOS_PrimitiveExtension<T> extension) {
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



	private JOOS_Lexicon(){
		super();
	}



	/**
	 * 
	 * @param type
	 * @param JAVAIndexedTypes
	 * @param localTypes
	 * 
	 * @return the type handler and create it if necessary
	 * @throws JOOS_ComposingException 
	 * @throws JOOS_ParsingException 
	 * @throws Exception 
	 */
	public TypeHandler get(Class<?> type) throws JOOS_ComposingException {

		if(type==null){
			throw new JOOS_ComposingException("[Ws3dContext] Type is null");
		}

		JOOS_Type annotation = type.getAnnotation(JOOS_Type.class);
		if(annotation==null){
			throw new JOOS_ComposingException("[Ws3dContext] Type is not annotated: "+type);
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
	 * @throws JOOS_ParsingException
	 * @throws IOException
	 */
	public Object parse(JOOS_Reader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {
		Parser parser = new Parser(this, new StreamReader(reader), isVerbose);
		return parser.parse();
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
	 * @throws JOOS_CompilingException 
	 * @throws JOOS_ComposingException 
	 */
	public void compose(JOOS_Writer writer, Object object, String indentSequence,  boolean isVerbose) 
			throws IOException,  JOOS_ComposingException {
		Composer composer = new Composer(this, writer, indentSequence, isVerbose);
		composer.compose(object);
	}
}
