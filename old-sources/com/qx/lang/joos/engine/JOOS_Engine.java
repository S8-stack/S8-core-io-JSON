package com.qx.lang.joos.engine;


import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.qx.lang.joos.JOOS_NodeObject;


public class JOOS_Engine {
	

	/**
	 * the types retrievable with deserialized names
	 */
	private HashMap<String, JOOS_TypeHandler> types_ByJAVAName = new HashMap<String, JOOS_TypeHandler>(10);
	
	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, JOOS_TypeHandler> rootTypes = new HashMap<String, JOOS_TypeHandler>();

	/**
	 * the parser
	 */
	private Serializer serializer;
	
	/**
	 * the parser
	 */
	private Deserializer deserializer;
	
	
	
	
	public JOOS_Engine() throws Exception{
		super();
		serializer = new Serializer(types_ByJAVAName);
		deserializer = new Deserializer(rootTypes);
	}
	
	
	
	
	public void discover(Class<?> rootType) throws Exception{
		JOOS_TypeHandler.discoverChildType(rootType, types_ByJAVAName, rootTypes);
	}
	
	
	
	
	
	public String getJOOSTypeName(Class<?> type){
		return types_ByJAVAName.get(type.getName()).getAnnotatedTypeName();
	}
	
	
	public boolean isRegistered(Class<?> type){
		return types_ByJAVAName.containsKey(type.getName());
	}
	
	
	

	/**
	 * 
	 * @param source: source code to be deserialized
	 * @return
	 * @throws Exception
	 */
	public Deserializer.Result deserialize(String source){
		return deserializer.deserialize(source);
	}
	
	
	/**
	 * 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public Deserializer.Result deserializeFile(String pathname) throws Exception{
		return deserializer.deserializeFile(pathname);
	}

	
	/**
	 * 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public void serialize(JOOS_NodeObject object, OutputStreamWriter writer) throws Exception{
		serializer.serialize(object, writer);
	}
	
}
