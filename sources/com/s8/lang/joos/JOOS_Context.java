package com.s8.lang.joos;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.lang.joos.composing.Composer;
import com.s8.lang.joos.composing.JOOS_ComposingException;
import com.s8.lang.joos.composing.JOOS_Writer;
import com.s8.lang.joos.parsing.JOOS_ParsingException;
import com.s8.lang.joos.parsing.JOOS_Reader;
import com.s8.lang.joos.parsing.Parser;
import com.s8.lang.joos.parsing.StreamReader;
import com.s8.lang.joos.type.FieldHandlerFactory;
import com.s8.lang.joos.type.JOOS_CompilingException;
import com.s8.lang.joos.type.TypeHandler;



public class JOOS_Context {

	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, TypeHandler> types = new HashMap<String, TypeHandler>();

	private Map<String, TypeHandler> typesByClassName = new HashMap<String, TypeHandler>();

	private final FieldHandlerFactory fieldHandlerFactory;

	public JOOS_Context(){
		super();
		fieldHandlerFactory = new FieldHandlerFactory();
	}


	public TypeHandler discover(Class<?> type) throws JOOS_CompilingException{
		if(type==null){
			throw new RuntimeException("[Ws3dContext] Type is null");
		}
		
		JOOS_Type annotation = type.getAnnotation(JOOS_Type.class);
		if(annotation==null){
			throw new RuntimeException("[Ws3dContext] Type is not annotated: "+type);
		}
		
		String name = annotation.name();
		TypeHandler typeHandler = types.get(name);
		if(typeHandler == null){
			typeHandler = new TypeHandler(type);
			
			// registration
			types.put(name, typeHandler);
			typesByClassName.put(type.getName(), typeHandler);
			
			typeHandler.initialize(this);
		}
		return typeHandler;
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
		TypeHandler typeHandler = types.get(name);
		return typeHandler;
	}


	public TypeHandler get(String name){
		return types.get(name);
	}
	
	
	public TypeHandler getByClassName(Class<?> type){
		return typesByClassName.get(type.getName());
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
