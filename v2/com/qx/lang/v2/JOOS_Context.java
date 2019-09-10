package com.qx.lang.v2;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.qx.lang.v2.composing.Composer;
import com.qx.lang.v2.parsing.Parser;
import com.qx.lang.v2.parsing.StreamReader;
import com.qx.lang.v2.type.TypeHandler;



public class JOOS_Context {

	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, TypeHandler> types = new HashMap<String, TypeHandler>();

	private Map<String, TypeHandler> typesByClassName = new HashMap<String, TypeHandler>();



	public JOOS_Context(){
		super();
	}


	public void discover(Class<?> type){
		get(type);
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

	
	public Object parse(Reader reader, boolean isVerbose) throws Ws3dParsingException, IOException {
		Parser parser = new Parser(this, new StreamReader(reader), isVerbose);
		return parser.parse();
	}
	
	public void compose(Writer writer, Object object, String indentSequence,  boolean isVerbose) 
			throws IllegalArgumentException, IllegalAccessException, IOException {
		Composer composer = new Composer(this, writer, indentSequence, isVerbose);
		composer.compose(object);
	}
}
