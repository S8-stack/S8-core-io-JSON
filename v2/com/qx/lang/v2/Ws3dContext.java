package com.qx.lang.v2;


import java.util.HashMap;
import java.util.Map;

import com.qx.lang.v2.annotation.WebScriptObject;



public class Ws3dContext {

	/**
	 * root Types: the only allowed types to start a joos file in the context of this engine instance
	 */
	private Map<String, Ws3dTypeHandler> types = new HashMap<String, Ws3dTypeHandler>();

	private Map<String, Ws3dTypeHandler> typesByClassName = new HashMap<String, Ws3dTypeHandler>();



	public Ws3dContext(){
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
	public Ws3dTypeHandler get(Class<?> type){
		if(type==null){
			throw new RuntimeException("[Ws3dContext] Type is null");
		}
		WebScriptObject annotation = type.getAnnotation(WebScriptObject.class);
		String name = annotation.name();
		Ws3dTypeHandler typeHandler = types.get(name);
		if(typeHandler == null){
			typeHandler = new Ws3dTypeHandler(type);
			
			// registration
			types.put(name, typeHandler);
			typesByClassName.put(type.getName(), typeHandler);
			
			typeHandler.initialize(this);
		}
		return typeHandler;
	}


	public Ws3dTypeHandler get(String name){
		return types.get(name);
	}
	
	
	public Ws3dTypeHandler getByClassName(Class<?> type){
		return typesByClassName.get(type.getName());
	}

}
