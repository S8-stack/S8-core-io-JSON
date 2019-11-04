package com.qx.level0.lang.joos;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.joos.composing.Composer;
import com.qx.level0.lang.joos.parsing.Parser;
import com.qx.level0.lang.joos.parsing.StreamReader;
import com.qx.level0.lang.joos.type.FieldHandlerFactory;
import com.qx.level0.lang.joos.type.TypeHandler;



public class JOOS_Context {

	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, TypeHandler> types = new HashMap<String, TypeHandler>();

	private Map<String, TypeHandler> typesByClassName = new HashMap<String, TypeHandler>();

	private FieldHandlerFactory fieldHandlerFactory = new FieldHandlerFactory();

	public JOOS_Context(){
		super();
	}


	public void discover(Class<?> type){
		get(type);
	}
	
	public void addFieldExtension(FieldHandlerFactory.Extension extension) {
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
	 * @throws Exception 
	 */
	public TypeHandler get(Class<?> type){
		
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
	public Object parse(Reader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {
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
	 */
	public void compose(Writer writer, Object object, String indentSequence,  boolean isVerbose) 
			throws IllegalArgumentException, IllegalAccessException, IOException {
		Composer composer = new Composer(this, writer, indentSequence, isVerbose);
		composer.compose(object);
	}
}
